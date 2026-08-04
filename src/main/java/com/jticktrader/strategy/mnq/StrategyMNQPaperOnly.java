package com.jticktrader.strategy.mnq;

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
public abstract class StrategyMNQPaperOnly extends Strategy {
    protected long counter;

    // S&P 500 e-mini future
    protected StrategyMNQPaperOnly(StrategyParams optimizationParams) {
        super(optimizationParams);
        // Specify the contract to trade
        Contract contract = ContractFactory.makeFutureContract("MNQ", "CME");

        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("1:20", "22:55", "America/New_York");
        int multiplier = 2;// contract multiplier
        Commission commission = CommissionFactory.getBundledNorthAmericaFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);
    }

    @Override
    public void setIndicators() {
    }

    @Override
    public void onBookSnapshot() {
        counter++;
        if (counter % 60 >= 30) {
           goFlat();
        } else  {
           goLong(1);
        }
    }

}
