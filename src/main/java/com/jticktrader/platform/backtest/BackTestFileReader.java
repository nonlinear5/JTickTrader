package com.jticktrader.platform.backtest;

import com.jticktrader.platform.marketbook.MarketSnapshot;
import com.jticktrader.platform.marketbook.MarketSnapshotFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Reads and validates a data file containing historical market data records.
 * The data file is used for back testing and optimization of trading strategies.
 *
 * @author Eugene Kononov
 */
public class BackTestFileReader {
    private static final Logger LOGGER = Logger.getLogger(BackTestFileReader.class.getName());
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final int LINE_SEPARATOR_SIZE = LINE_SEP.length();
    private static final int PROGRESS_REPORT_INTERVAL = 100_000;
    private static final int SNAPSHOT_BATCH_SIZE = 3;
    private static final double MIN_DISPLACEMENT = 2.0;
    private static final double MIN_PATH = 10.0;
    
    private static List<MarketSnapshot> snapshots;
    private static List<MarketSnapshot> snapshotsTripple;
    private static String cacheKey;
    private final MarketSnapshotFilter filter;
    private final String fileName;
    private final long fileSize;
    private final long fileLastModified;


    public BackTestFileReader(String fileName, MarketSnapshotFilter filter) {
        this.fileName = fileName;
        this.filter = filter;

        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("Could not find file: " + fileName);
        }
        fileSize = file.length();
        fileLastModified = file.lastModified();
    }


    public List<MarketSnapshot> load(ProgressListener progressListener) {
        String key = buildCacheKey();
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
                sizeRead += (line.length() + LINE_SEPARATOR_SIZE);
                linesRead++;
                MarketSnapshot snapshot = lineParser.process(line);

                if (snapshot != null) {
                    processSnapshot(snapshot, line, linesRead);
                }
                if (linesRead % PROGRESS_REPORT_INTERVAL == 0) {
                    progressListener.setProgress(sizeRead, fileSize, "Loading historical data file");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        cacheKey = key;
        return snapshots;
    }

    private String buildCacheKey() {
        String key = fileName + "," + fileSize + "," + fileLastModified;
        if (filter != null) {
            key += ", " + filter;
        }
        return key;
    }

    private void processSnapshot(MarketSnapshot snapshot, String line, long lineNumber) {
        if (!isValidBidAsk(snapshot)) {
            return;
        }
        addToTripple(snapshot, line, lineNumber);
    }

    private boolean isValidBidAsk(MarketSnapshot snapshot) {
        double bid = snapshot.getBid();
        double ask = snapshot.getAsk();
        if (bid >= ask) {
            LOGGER.warning("Bid is greater than or equal to ask: " + bid + " >= " + ask);
            return false;
        }
        return true;
    }

    private void addToTripple(MarketSnapshot snapshot, String line, long lineNumber) {
        snapshotsTripple.add(snapshot);
        if (snapshotsTripple.size() == SNAPSHOT_BATCH_SIZE) {
            if (isTrippleValid()) {
                snapshots.add(snapshotsTripple.remove(0));
            } else {
                snapshotsTripple.remove(1);
                LOGGER.warning("Tripple is not valid in line " + lineNumber + ": " + line);
            }
        }
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

        return isValidPricePattern(displacement, path);
    }

    private boolean isValidPricePattern(double displacement, double path) {
        return displacement >= MIN_DISPLACEMENT || path <= MIN_PATH;
    }
}


