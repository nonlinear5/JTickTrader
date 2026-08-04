package com.jticktrader.strategy.mes;

import com.jticktrader.indicator.price.PriceVelocitySimple;
import com.jticktrader.platform.optimizer.StrategyParams;
import com.jticktrader.strategy.base.StrategyMESext;

/**
 * @author Eugene Kononov
 */
public abstract class MESLongSpecialSize16SimpleLinear4 extends StrategyMESext {
    // Strategy parameters names
    protected static final String PERIOD = "Period";
    protected static final String ENTRY = "Entry";
    protected static final String INC = "Inc";
    protected static final String EXIT = "Exit";

    private final int entry, inc, exit;


    // indicator
    private PriceVelocitySimple priceVelocityInd;

    public MESLongSpecialSize16SimpleLinear4(StrategyParams optimizationParams) {
        super(optimizationParams);

        entry = getParam(ENTRY);
        inc = getParam(INC);
        exit = getParam(EXIT);
    }

    @Override
    public void setIndicators() {
        priceVelocityInd = (PriceVelocitySimple) addIndicator(new PriceVelocitySimple(getParam(PERIOD)));
    }

    @Override
    public void onBookSnapshot() {
        double priceVelocity = priceVelocityInd.getValue();


        if (priceVelocity < exit) {
            goFlat();
        } else if (priceVelocity > entry) {
            double strength = priceVelocity - entry;
            double size = strength / inc;
            if (size < 1) {
                size = 1;
            }
            if (size > 16) {
                size = 16;
            }

            int targetPosition = Math.max(getPositionManager().getCurrentPosition(), (int) size);
            goLong(targetPosition);
        }
    }
}
