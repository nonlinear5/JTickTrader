package com.jticktrader.platform.marketbook;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for MarketSnapshot class
 */
public class MarketSnapshotTest {

    @Test
    public void testMarketSnapshotWithAllParameters() {
        MarketSnapshot snapshot = new MarketSnapshot("ES", 1000L, 4500.0, 4501.0, 100);
        assertNotNull(snapshot);
        assertEquals("ES", snapshot.getContract());
        assertEquals(1000L, snapshot.getTime());
        assertEquals(4500.0, snapshot.getBid(), 0.01);
        assertEquals(4501.0, snapshot.getAsk(), 0.01);
        assertEquals(100, snapshot.getVolume());
    }

    @Test
    public void testMarketSnapshotWithoutContract() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, 4500.0, 4501.0, 100);
        assertNotNull(snapshot);
        assertNull(snapshot.getContract());
        assertEquals(1000L, snapshot.getTime());
    }

    @Test
    public void testEndOfStreamSnapshot() {
        MarketSnapshot snapshot = new MarketSnapshot();
        assertTrue(snapshot.isEndOfStream());
        assertEquals(-1L, snapshot.getTime());
    }

    @Test
    public void testIsEndOfStreamReturnsFalseForNormalSnapshot() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, 4500.0, 4501.0, 100);
        assertFalse(snapshot.isEndOfStream());
    }

    @Test
    public void testGetPrice() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, 4500.0, 4502.0, 100);
        double expectedPrice = (4500.0 + 4502.0) / 2.0;
        assertEquals(expectedPrice, snapshot.getPrice(), 0.01);
    }

    @Test
    public void testGetPriceWithIdenticalBidAsk() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, 4500.0, 4500.0, 100);
        assertEquals(4500.0, snapshot.getPrice(), 0.01);
    }

    @Test
    public void testMarketSnapshotWithZeroVolume() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, 4500.0, 4501.0, 0);
        assertEquals(0, snapshot.getVolume());
    }

    @Test
    public void testMarketSnapshotWithLargeVolume() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, 4500.0, 4501.0, 1000000);
        assertEquals(1000000, snapshot.getVolume());
    }

    @Test
    public void testMarketSnapshotWithNegativeTime() {
        MarketSnapshot snapshot = new MarketSnapshot(-500L, 4500.0, 4501.0, 100);
        assertEquals(-500L, snapshot.getTime());
    }

    @Test
    public void testMarketSnapshotWithNegativePrices() {
        MarketSnapshot snapshot = new MarketSnapshot(1000L, -100.0, -99.0, 100);
        assertEquals(-100.0, snapshot.getBid(), 0.01);
        assertEquals(-99.0, snapshot.getAsk(), 0.01);
    }

    @Test
    public void testMarketSnapshotComparator() {
        MarketSnapshot snapshot1 = new MarketSnapshot(1000L, 4500.0, 4501.0, 100);
        MarketSnapshot snapshot2 = new MarketSnapshot(2000L, 4500.0, 4501.0, 100);
        
        SnapshotComparator comparator = new SnapshotComparator();
        assertTrue(comparator.compare(snapshot1, snapshot2) < 0);
        assertTrue(comparator.compare(snapshot2, snapshot1) > 0);
    }

    @Test
    public void testMarketSnapshotComparatorEqual() {
        MarketSnapshot snapshot1 = new MarketSnapshot(1000L, 4500.0, 4501.0, 100);
        MarketSnapshot snapshot2 = new MarketSnapshot(1000L, 4502.0, 4503.0, 200);
        
        SnapshotComparator comparator = new SnapshotComparator();
        assertEquals(0, comparator.compare(snapshot1, snapshot2));
    }
}
