package com.jticktrader.platform.tickerstate;

import com.ib.client.Contract;
import com.jticktrader.platform.marketbook.MarketSnapshot;
import com.jticktrader.platform.model.Dispatcher;
import com.jticktrader.platform.util.ntp.NTPClock;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds history of market snapshots for a trading instrument.
 *
 * @author Eugene Kononov
 */
public class TickerState {
    private final static long minVolume = 1000L; // minimum volume for a contract to be considered liquid
    private final static NTPClock ntpclock = Dispatcher.getInstance().getNTPClock();

    private final Contract contract;
    private final Map<Integer, Integer> volumes;
    private final Map<Integer, String> localSymbols;
    private final String symbol;

    private String localSymbol;
    private double bid, ask;
    private double lastValidBid, lastValidAsk;
    private int previousVolume;
    private long lastSnapshotSeconds;
    private int mostLiquidId;

    public TickerState(Contract contract) {
        this.contract = contract;
        symbol = contract.symbol();
        volumes = new HashMap<>();
        localSymbols = new HashMap<>();
    }

    private void capture() {
        if (ask <= 0 || bid <= 0) {
            return;
        }
        if (ask <= bid) {
            return;
        }
        lastValidBid = bid;
        lastValidAsk = ask;

    }

    public void setAsk(double ask) {
        this.ask = ask;
        capture();
    }

    public void setBid(double bid) {
        this.bid = bid;
        capture();
    }

    public String getSymbol() {
        return symbol;
    }

    public Contract getContract() {
        return contract;
    }

    public int getMostLiquidId() {
        return mostLiquidId;
    }

    public void addLocalSymbol(int id, String localSymbol) {
        localSymbols.put(id, localSymbol);
    }

    public String getLocalSymbol(int id) {
        return localSymbols.get(id);
    }

    public synchronized void processVolume(int tickerId, int volume) {
        volumes.put(tickerId, volume);
        if (mostLiquidId != 0) {
            return;
        }

        if (volumes.size() >= 3) {
            int maxVolume = 0;
            int id = 0;
            for (Map.Entry<Integer, Integer> entry : volumes.entrySet()) {
                int vol = entry.getValue();
                if (vol > maxVolume && vol >= minVolume) {
                    maxVolume = vol;
                    id = entry.getKey();
                }
            }

            if (id != 0) {
                mostLiquidId = id;
                localSymbol = localSymbols.get(mostLiquidId);
            }
        }
    }

    public synchronized MarketSnapshot takeMarketSnapshot() {

        if (!volumes.containsKey(mostLiquidId)) {
            return null;
        }

        if (lastValidBid == 0 || lastValidAsk == 0) {
            return null;
        }

        long timeSeconds = ntpclock.getTime() / 1000L; // drop the millis
        if (timeSeconds <= lastSnapshotSeconds) {
            return null;
        }


        lastSnapshotSeconds = timeSeconds;

        // one second volume
        int volume = volumes.get(mostLiquidId);
        int oneSecondVolume = (previousVolume == 0) ? 0 : Math.max(0, volume - previousVolume);
        previousVolume = volume;


        // create the snapshot
        return new MarketSnapshot(localSymbol, timeSeconds * 1000L, lastValidBid, lastValidAsk, oneSecondVolume);
    }
}