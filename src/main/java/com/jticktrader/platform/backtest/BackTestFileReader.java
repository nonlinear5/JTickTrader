package com.jticktrader.platform.backtest;

import com.jticktrader.platform.marketbook.MarketSnapshot;
import com.jticktrader.platform.marketbook.MarketSnapshotFilter;
import com.jticktrader.platform.snapshotwriter.SnapshotWriterManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and validates a data file containing historical market data records.
 * The data file is used for back testing and optimization of trading strategies.
 *
 * @author Eugene Kononov
 */
public class BackTestFileReader {
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final int lineSeparatorSize = LINE_SEP.length();
    private static List<MarketSnapshot> snapshots;
    private static List<MarketSnapshot> snapshotsTripple;
    private static String cacheKey;
    private final MarketSnapshotFilter filter;
    private final String fileName;
    private final long fileSize;
    private final long fileLastModified;
    // ekk private final SnapshotWriterManager snapshotWriterManager;


    public BackTestFileReader(String fileName, MarketSnapshotFilter filter) {
        this.fileName = fileName;
        this.filter = filter;

        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("Could not find file: " + fileName);
        }
        fileSize = file.length();
        fileLastModified = file.lastModified();
        // ekk snapshotWriterManager = new SnapshotWriterManager();

    }


    public List<MarketSnapshot> load(ProgressListener progressListener) {
        String key = fileName + "," + fileSize + "," + fileLastModified;
        if (filter != null) {
            key += ", " + filter;
        }
        if (key.equals(cacheKey) && !snapshots.isEmpty()) {
            return snapshots;
        }

        snapshots = new ArrayList<>();
        snapshotsTripple = new ArrayList<>();
        long sizeRead = 0, linesRead = 0;
        LineParser lineParser = new LineParser(filter);
        String line;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            while ((line = reader.readLine()) != null) {
                sizeRead += (line.length() + lineSeparatorSize);
                linesRead++;
                MarketSnapshot snapshot = lineParser.process(line);

                if (snapshot != null) {
                    double bid = snapshot.getBid();
                    double ask = snapshot.getAsk();
                    if (bid >= ask) {
                        System.out.println("Bid is greater than ask in line " + linesRead + ": " + line);
                        //throw new RuntimeException("Bid > Ask: " + bid + " > Ask: " + ask + " on line " + line);
                    } else {
                        //snapshots.add(snapshot);
                        snapshotsTripple.add(snapshot);
                        if (snapshotsTripple.size() == 3) {
                            if (isTrippleValid()) {
                                MarketSnapshot s = snapshotsTripple.remove(0);
                                snapshots.add(s);
                                // ekk snapshotWriterManager.saveSnapshot(snapshot, "new_recent");

                            } else {
                                snapshotsTripple.remove(1);
                                System.out.println("Tripple is not valid in line " + linesRead + ": " + line);
                            }
                        }
                    }
                }
                if (linesRead % 100000 == 0) {
                    progressListener.setProgress(sizeRead, fileSize, "Loading historical data file");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        cacheKey = key;
        return snapshots;
    }

    private boolean isTrippleValid() {
        MarketSnapshot first = snapshotsTripple.get(0);
        MarketSnapshot second = snapshotsTripple.get(1);
        MarketSnapshot third = snapshotsTripple.get(2);

        double firstPrice = first.getPrice();
        double secondPrice = second.getPrice();
        double thirdPrice = third.getPrice();

        double path = Math.abs(firstPrice - secondPrice) + Math.abs(secondPrice - thirdPrice);
        double displacement = Math.abs(firstPrice - thirdPrice);

        if (displacement < 2 && path > 10) {
            return false;
        } else {
            return true;
        }
    }
}


