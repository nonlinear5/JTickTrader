package com.jticktrader.platform.ibhandler;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.TickType;
import com.jticktrader.platform.marketbook.MarketSnapshot;
import com.jticktrader.platform.model.Dispatcher;
import com.jticktrader.platform.report.EventReport;
import com.jticktrader.platform.snapshotwriter.SnapshotWriterManager;
import com.jticktrader.platform.tickerstate.TickerState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * @author Eugene Kononov
 */
class MarketDataHandler {
    private final EClientSocket socket;
    private final SnapshotWriterManager snapshotWriterManager;
    private final Map<Integer, TickerState> marketStates;
    private final Map<String, TickerState> symbols;
    private final Map<Integer, Contract> contracts;
    private final EventReport eventReport;
    private int requestId;
    private BlockingQueue<MarketSnapshot> queue;

    public MarketDataHandler(EClientSocket socket) {
        this.socket = socket;
        marketStates = new HashMap<>();
        symbols = new HashMap<>();
        contracts = new HashMap<>();
        eventReport = Dispatcher.getInstance().getEventReport();
        snapshotWriterManager = new SnapshotWriterManager();
        requestId = 1000000; // start with a high number which does not clash with order IDs
    }

    public void setQueue(BlockingQueue<MarketSnapshot> queue) {
        this.queue = queue;
    }


    private void subscribe(String localSymbol, TickerState tickerState, Contract contract) {
        requestId++;
        marketStates.put(requestId, tickerState);
        tickerState.addLocalSymbol(requestId, localSymbol);
        contract.localSymbol(localSymbol);
        socket.reqMktData(requestId, contract, "", false, false, null);
    }

    public void unsubscribe() {
        requestId++;
        socket.cancelMktData(requestId);
    }

    public void subscribe(Contract contract) {
        String ticker = contract.symbol();

        if (!symbols.containsKey(ticker)) {
            TickerState tickerState = new TickerState(contract);
            symbols.put(ticker, tickerState);

            requestId++;
            contracts.put(requestId, contract);
            eventReport.report("MarketDataHandler", "Requested contracts for ticker " + ticker);
            socket.reqContractDetails(requestId, contract);
        }
    }

    public void subscribe(int requestId, List<String> localSymbols) {
        Contract contract = contracts.get(requestId);
        String ticker = contract.symbol();
        TickerState tickerState = symbols.get(ticker);

        for (String localSymbol : localSymbols) {
            if (localSymbol.startsWith(ticker)) {
                subscribe(localSymbol, tickerState, contract);
            }
        }
    }

    public void tickPrice(int tickerId, int field, double price) throws InterruptedException {

        TickerState tickerState = marketStates.get(tickerId);

        int mostLiquidId = tickerState.getMostLiquidId();
        if (mostLiquidId != tickerId) {
            return;
        }

        if (field != TickType.BID.index() && field != TickType.ASK.index()) {
            return;
        }

        if (field == TickType.BID.index()) {
            tickerState.setBid(price);
        }

        if (field == TickType.ASK.index()) {
            tickerState.setAsk(price);
        }


        MarketSnapshot snapshot = tickerState.takeMarketSnapshot();

        if (snapshot != null) {
            snapshotWriterManager.saveSnapshot(snapshot, tickerState.getSymbol());
            queue.put(snapshot);
        }

    }


    public void tickSize(int tickerId, int field, int size) {
        TickerState tickerState = marketStates.get(tickerId);
        if (field == TickType.VOLUME.index()) {
            tickerState.processVolume(tickerId, size);
        }
    }

}
