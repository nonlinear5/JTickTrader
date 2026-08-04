package com.jticktrader.strategy.mes;

import com.ib.client.Contract;
import com.jticktrader.platform.commission.Commission;
import com.jticktrader.platform.commission.CommissionFactory;
import com.jticktrader.platform.optimizer.StrategyParams;
import com.jticktrader.platform.schedule.TradingSchedule;
import com.jticktrader.platform.strategy.Strategy;
import com.jticktrader.platform.util.contract.ContractFactory;

/**
 *
 * This strategy is meant for paper-trading only!
 *
 * @author Eugene Kononov
 */
public abstract class StrategyMESPaperOnly extends Strategy {
    protected long counter;

    // S&P 500 e-mini future
    protected StrategyMESPaperOnly(StrategyParams optimizationParams) {
        super(optimizationParams);
        // Specify the contract to trade
        Contract contract = ContractFactory.makeFutureContract("MES", "CME");

        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("1:20", "22:55", "America/New_York");
        int multiplier = 5;// contract multiplier
        Commission commission = CommissionFactory.getBundledNorthAmericaFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);
    }

    @Override
    public void setIndicators() {
    }

    @Override
    public void onBookSnapshot() {
        counter++;
        if (counter % 20 >= 10) {
           goFlat();
        } else  {
           goLong(1);
        }
    }

}
