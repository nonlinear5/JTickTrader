package com.jticktrader.indicator.volume;

import com.jticktrader.platform.indicator.Indicator;

/**
 * Normalized velocity of price
 *
 * @author Eugene Kononov
 */
public class VolumeWeightedPriceDiff extends Indicator {
    private double sumVolume, sumPriceVolume;
    double openPrice = 0;
    double trend;
    //private double fast, slow;

    public VolumeWeightedPriceDiff() {
        super(1);

    }

    @Override
    public void calculate() {

        double price = marketBook.getSnapshot().getPrice();
        if (openPrice == 0) {
            openPrice = price;
        }
        double volume = marketBook.getSnapshot().getVolume();
        sumVolume += volume;
        double point = price * volume; // Volume-weighted price contribution
        sumPriceVolume += point;
        double volumeWeightedPrice = sumPriceVolume / sumVolume; // Volume-weighted price


        value = 10000 * Math.log(volumeWeightedPrice / price);
        trend = price-openPrice;
    }

    public double getTrend() {
        return trend;
    }

    @Override
    public void reset() {
        sumVolume = sumPriceVolume = openPrice = 0;
    }
}
