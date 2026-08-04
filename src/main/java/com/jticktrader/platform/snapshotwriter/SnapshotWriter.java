package com.jticktrader.platform.snapshotwriter;

import com.jticktrader.platform.marketbook.MarketSnapshot;
import com.jticktrader.platform.startup.JTickTrader;
import com.jticktrader.platform.util.format.NumberFormatterFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


/**
 * Writes historical market data to a file which is used for
 * back testing and optimization of trading strategies.
 *
 * @author Eugene Kononov
 */
public class SnapshotWriter {
    private static final String LINE_SEP = System.getProperty("line.separator");
    private final DecimalFormat df6;
    private final SimpleDateFormat dateFormat;
    private final PrintWriter writer;

    public SnapshotWriter(String fileName) {
        df6 = NumberFormatterFactory.getNumberFormatter(6);
        dateFormat = new SimpleDateFormat("MMddyy,HHmmss");
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
        dateFormat.setTimeZone(timeZone);

        try {
            boolean fileExisted = new File(fileName).exists();
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            if (!fileExisted) {
                StringBuilder header = getHeader();
                writer.println(header);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Could not write to file " + fileName);
        }
    }

    public void write(MarketSnapshot marketSnapshot) {
        String sb = dateFormat.format(marketSnapshot.getTime()) + "," +
                df6.format(marketSnapshot.getBid()) + "," +
                df6.format(marketSnapshot.getAsk()) + "," +
                marketSnapshot.getVolume();

        writer.println(sb);
        writer.flush();
    }

    private StringBuilder getHeader() {
        StringBuilder header = new StringBuilder();
        header.append("# This historical data file was created by ").append(JTickTrader.APP_NAME).append(LINE_SEP);
        header.append("# Each line represents a 1-second snapshot of the market and contains 5 columns").append(LINE_SEP);
        header.append("# 1. date in the MMddyy format").append(LINE_SEP);
        header.append("# 2. time in the HHmmss format").append(LINE_SEP);
        header.append("# 3. best bid").append(LINE_SEP);
        header.append("# 4. best ask").append(LINE_SEP);
        header.append("# 5. volume").append(LINE_SEP);
        header.append(LINE_SEP);
        header.append("timeZone=").append(dateFormat.getTimeZone().getID()).append(LINE_SEP);
        return header;
    }
}
