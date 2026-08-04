package com.jticktrader.platform.snapshotwriter;

import com.jticktrader.platform.marketbook.MarketSnapshot;
import com.jticktrader.platform.model.Dispatcher;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eugene Kononov
 */
public class SnapshotWriterManager {
    private final Map<String, SnapshotWriter> fileWriters;
    private final String marketDataDir;
    private final TimeFilter timeFilter;

    public SnapshotWriterManager() {
        timeFilter = new TimeFilter(7, 16); //ekk
        fileWriters = new HashMap<>();
        marketDataDir = Dispatcher.getInstance().getMarketDataDir();
        File marketDataDirFile = new File(marketDataDir);
        if (!marketDataDirFile.exists()) {
            boolean isCreated = marketDataDirFile.mkdir();
            if (!isCreated) {
                throw new RuntimeException("Could not create directory " + marketDataDir);
            }
        }
    }

    public void saveSnapshot(MarketSnapshot marketSnapshot, String ticker) {
        if (timeFilter.isRecordable(marketSnapshot.getTime())) {
            SnapshotWriter writer = fileWriters.get(ticker);
            if (writer == null) {
                String fileName = marketDataDir + ticker + ".txt";
                writer = new SnapshotWriter(fileName);
                fileWriters.put(ticker, writer);
            }
            writer.write(marketSnapshot);
        }
    }
}

