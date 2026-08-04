package com.jticktrader.strategy.base;

import com.ib.client.Contract;
import com.jticktrader.platform.commission.Commission;
import com.jticktrader.platform.commission.CommissionFactory;
import com.jticktrader.platform.optimizer.StrategyParams;
import com.jticktrader.platform.schedule.TradingSchedule;
import com.jticktrader.platform.strategy.Strategy;
import com.jticktrader.platform.util.contract.ContractFactory;

/**
 * @author marcus
 */
public abstract class StrategyCL extends Strategy {
    // Crude oil future
    protected StrategyCL(StrategyParams optimizationParams) {
        super(optimizationParams);
        // Specify the contract to trade
        Contract contract = ContractFactory.makeFutureContract("CL", "NYMEX");
        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("9:05", "14:05", "America/New_York");
        int multiplier = 1000;// contract 1000 barrels
        Commission commission = CommissionFactory.getNYMEXFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);
    }
}
