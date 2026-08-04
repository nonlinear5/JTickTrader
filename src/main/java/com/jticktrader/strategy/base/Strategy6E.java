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
public abstract class Strategy6E extends Strategy {

    // Euro-USD future
    protected Strategy6E(StrategyParams optimizationParams) {
        super(optimizationParams);
        // Specify the contract to trade
        Contract contract = ContractFactory.makeContract("6E", null, "FUT", "CME", "USD");
        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("2:05", "15:55", "America/New_York");
        int multiplier = 125000;// contract multiplier
        Commission commission = CommissionFactory.getBundledNorthAmericaFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);
    }
}
