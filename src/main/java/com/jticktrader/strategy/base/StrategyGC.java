package com.jticktrader.strategy.base;

import com.ib.client.Contract;
import com.jticktrader.platform.commission.Commission;
import com.jticktrader.platform.commission.CommissionFactory;
import com.jticktrader.platform.optimizer.StrategyParams;
import com.jticktrader.platform.schedule.TradingSchedule;
import com.jticktrader.platform.strategy.Strategy;
import com.jticktrader.platform.util.contract.ContractFactory;

/**
 * @author Eugene Kononov
 */
public abstract class StrategyGC extends Strategy {

    // Gold future
    protected StrategyGC(StrategyParams optimizationParams) {
        super(optimizationParams);
        // Specify the contract to trade
        Contract contract = ContractFactory.makeFutureContract("GC", "NYMEX");
        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("8:32", "15:25", "America/New_York");
        int multiplier = 100;
        Commission commission = CommissionFactory.getNYMEXFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);
    }

}
