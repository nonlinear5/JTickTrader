package com.jticktrader.strategy;

import com.jticktrader.platform.optimizer.StrategyParams;
import com.jticktrader.strategy.mes.MESLongSize16SimpleLinear;


/**
 * @author Eugene Kononov
 */
public class MESLongTrueLinearSample extends MESLongSize16SimpleLinear {
    public MESLongTrueLinearSample(StrategyParams optimizationParams) {
        super(optimizationParams);
    }

    @Override
    public void setParams() {
        addParam(PERIOD, 2000, 22000, 6000);
        addParam(ENTRY, 10, 200, 50);
        addParam(INC, 1, 11, 9);
        addParam(EXIT, 0, 120, 10);
    }
}