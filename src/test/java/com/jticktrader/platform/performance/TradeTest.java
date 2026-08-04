package com.jticktrader.platform.performance;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Trade class
 */
public class TradeTest {
    private Trade trade;

    @Before
    public void setUp() {
        trade = new Trade(50);
    }

    @Test
    public void testTradeInitialization() {
        assertNotNull(trade);
        assertEquals(0, trade.getQuantityBought());
        assertEquals(0, trade.getQuantitySold());
    }

    @Test
    public void testUpdateTotalBought() {
        trade.updateTotalBought(10, 100.0, 0.5);
        assertEquals(10, trade.getQuantityBought());
        assertEquals(100.0, trade.getAverageBoughtPrice(), 0.01);
    }

    @Test
    public void testUpdateTotalSold() {
        trade.updateTotalSold(5, 105.0, 0.2);
        assertEquals(5, trade.getQuantitySold());
        assertEquals(105.0, trade.getAverageSoldPrice(), 0.01);
    }

    @Test
    public void testAverageBoughtPriceMultipleUpdates() {
        trade.updateTotalBought(10, 100.0, 0.0);
        trade.updateTotalBought(10, 110.0, 0.0);
        assertEquals(105.0, trade.getAverageBoughtPrice(), 0.01);
    }

    @Test
    public void testAverageSoldPriceMultipleUpdates() {
        trade.updateTotalSold(5, 100.0, 0.0);
        trade.updateTotalSold(10, 110.0, 0.0);
        double expected = (5 * 100.0 + 10 * 110.0) / 15.0;
        assertEquals(expected, trade.getAverageSoldPrice(), 0.01);
    }

    @Test
    public void testGetAverageBoughtPriceZeroQuantity() {
        assertEquals(0.0, trade.getAverageBoughtPrice(), 0.01);
    }

    @Test
    public void testGetAverageSoldPriceZeroQuantity() {
        assertEquals(0.0, trade.getAverageSoldPrice(), 0.01);
    }

    @Test
    public void testSlippagePoints() {
        trade.updateTotalBought(10, 100.0, 0.5);
        trade.updateTotalSold(10, 105.0, 0.3);
        assertEquals(0.8, trade.getSlippagePoints(), 0.01);
    }

    @Test
    public void testSlippageAmount() {
        trade.updateTotalBought(10, 100.0, 0.5);
        trade.updateTotalSold(10, 105.0, 0.3);
        double expected = 50 * (10 * 0.5 + 10 * 0.3);
        assertEquals(expected, trade.getSlippageAmount(), 0.01);
    }

    @Test
    public void testEntryAndExitTime() {
        long entryTime = 1000L;
        long exitTime = 2000L;
        trade.setEntryTime(entryTime);
        trade.setExitTime(exitTime);
        
        assertEquals(entryTime, trade.getEntryTime());
        assertEquals(exitTime, trade.getExitTime());
    }

    @Test
    public void testTimeInMarket() {
        trade.setEntryTime(1000L);
        trade.setExitTime(5000L);
        assertEquals(4000L, trade.getTimeInMarket());
    }

    @Test
    public void testCompleteTradeScenario() {
        trade.setEntryTime(1000L);
        trade.updateTotalBought(5, 100.0, 0.1);
        trade.updateTotalBought(5, 102.0, 0.15);
        trade.updateTotalSold(10, 105.0, 0.2);
        trade.setExitTime(2000L);

        assertEquals(10, trade.getQuantityBought());
        assertEquals(10, trade.getQuantitySold());
        assertEquals(101.0, trade.getAverageBoughtPrice(), 0.01);
        assertEquals(105.0, trade.getAverageSoldPrice(), 0.01);
        assertEquals(1000L, trade.getTimeInMarket());
    }

    @Test
    public void testDifferentContractMultipliers() {
        Trade microTrade = new Trade(5);
        microTrade.updateTotalBought(10, 100.0, 0.5);
        microTrade.updateTotalSold(10, 105.0, 0.3);
        
        double microSlippage = microTrade.getSlippageAmount();
        double expectedMicroSlippage = 5 * (10 * 0.5 + 10 * 0.3);
        assertEquals(expectedMicroSlippage, microSlippage, 0.01);
    }
}
