package com.jticktrader.indicator.price;

import com.jticktrader.platform.indicator.Indicator;
import com.jticktrader.platform.marketbook.MarketSnapshot;

/**
 * price tensor
 * The absolute value of this tensor indicates the degree to which the market is either overbought or oversold.
 * The sign of the tensor indicates the direction of the imbalance: positive tension indicates that the market is
 * overbought, while negative tension indicates that the market is oversold.
 *
 * @author Eugene Kononov
 */
public class PriceVelocitySimple extends Indicator {

    private final double alpha;
    private double alphaCount;
    private double avePriceSum;

    public PriceVelocitySimple(int slowPeriod) {
        super(slowPeriod);
        // make sure to avoid the integer division
        alpha = 1 - 2.0 / (slowPeriod + 1);
    }

    /*
     * EMA calculation reference: https://en.wikipedia.org/wiki/Moving_average#Exponential_moving_average
     */
    @Override
    public void calculate() {
        MarketSnapshot snapshot = marketBook.getSnapshot();
        double price = snapshot.getPrice();

        alphaCount = 1 + alpha * alphaCount;
        avePriceSum = price + alpha * avePriceSum;
        double slowPrice = avePriceSum / alphaCount;

        value = -10000d * Math.log(price / slowPrice);
    }

    @Override
    public void reset() {
        alphaCount = 0;
        avePriceSum = 0;
    }
}
