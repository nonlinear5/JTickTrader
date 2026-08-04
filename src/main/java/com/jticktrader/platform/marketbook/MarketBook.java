package com.jticktrader.platform.marketbook;

import com.jticktrader.platform.model.Dispatcher;

/**
 * Holds history of market snapshots for a trading instrument.
 *
 * @author Eugene Kononov
 */
public class MarketBook {
    private static final long ONE_HOUR_MILLIS = 60 * 60 * 1000;
    private static final long LOCK_TIMEOUT_MINUTES = 15;
    private static final long MILLIS_PER_MINUTE = 1000L * 60L;
    private static final double MID_PRICE_DIVISOR = 2d;
    private MarketSnapshot marketSnapshot;
    private String contract;
    private double lastMidPrice;
    private long lastTimePriceChanged;
    private boolean isLocked;

    public boolean isEmpty() {
        return marketSnapshot == null;
    }

    public String getContract() {
        return contract;
    }

    public boolean isGapping(MarketSnapshot newMarketSnapshot) {
        return !isEmpty() && (newMarketSnapshot.getTime() - marketSnapshot.getTime() > ONE_HOUR_MILLIS);
    }

    public MarketSnapshot getSnapshot() {
        return marketSnapshot;
    }

    public void setSnapshot(MarketSnapshot marketSnapshot) {
        double midPrice = (marketSnapshot.getBid() + marketSnapshot.getAsk()) / MID_PRICE_DIVISOR;
        long time = marketSnapshot.getTime();

        if (midPrice != lastMidPrice) {
            lastTimePriceChanged = time;
            lastMidPrice = midPrice;
            if (isLocked) {
                isLocked = false;
                Dispatcher.getInstance().getEventReport().report("MarketBook", "market is unlocked, midprice is " + midPrice);
            }
        } else {
            long timeElapsed = time - lastTimePriceChanged;
            long minutesElapsed = timeElapsed / MILLIS_PER_MINUTE;
            if (minutesElapsed >= LOCK_TIMEOUT_MINUTES) {
                if (!isLocked) {
                    isLocked = true;
                    Dispatcher.getInstance().getEventReport().report("MarketBook", "market is locked, midprice is " + midPrice);
                }
            }
        }
        this.marketSnapshot = marketSnapshot;
        contract = marketSnapshot.getContract();
    }

    public boolean isLocked() {
        return isLocked;
    }
}
