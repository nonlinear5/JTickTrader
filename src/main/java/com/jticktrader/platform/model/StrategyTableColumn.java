package com.jticktrader.platform.model;

/**
 * @author Eugene Kononov
 */
public enum StrategyTableColumn {
    Strategy("Strategy"),
    Ticker("Ticker"),
    Contract("Contract"),
    Bid("Bid"),
    Ask("Ask"),
    Position("Position"),
    Trades("Trades"),
    AveDuration("Duration"),
    MaxSL("MSL"),
    MaxDD("MDD"),
    APD("APD"),
    OG("OG"),
    PI("PI"),
    NetProfit("Net Profit");

    private final String columnName;

    StrategyTableColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
