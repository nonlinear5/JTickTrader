package com.jticktrader.platform.report;

import com.jticktrader.platform.model.Dispatcher;
import com.jticktrader.platform.performance.PerformanceManager;
import com.jticktrader.platform.strategy.Strategy;
import com.jticktrader.platform.util.format.NumberFormatterFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eugene Kononov
 */
public class StatusReport {
    private static final DecimalFormat df0 = NumberFormatterFactory.getNumberFormatter(0);

    public StringBuilder generate() {
        StringBuilder report = new StringBuilder();
        report.append("\r\nStrategy, Ticker, Trades, NetProfit\r\n");

        Dispatcher dispatcher = Dispatcher.getInstance();
        List<Strategy> strategies = new ArrayList<>(dispatcher.getOrderManager().getAssistant().getAllStrategies());
        Collections.sort(strategies);

        for (Strategy strategy : strategies) {
            PerformanceManager pm = strategy.getPerformanceManager();
            report.append(strategy.getName()).append(", ");
            report.append(strategy.getTicker()).append(", ");
            report.append(pm.getTrades()).append(", ");
            report.append(df0.format(pm.getNetProfit()));
            report.append("\r\n");
        }

        return report;
    }
}

