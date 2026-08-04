package com.jticktrader.platform.marketbook;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Additional unit tests for MarketSnapshot edge cases and sorting
 */
public class MarketSnapshotSortingTest {

    @Test
    public void testSortingMultipleSnapshots() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(3000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(2000L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(1000L, snapshots.get(0).getTime());
        assertEquals(2000L, snapshots.get(1).getTime());
        assertEquals(3000L, snapshots.get(2).getTime());
    }

    @Test
    public void testSortingWithNegativeTimes() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(100L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(-100L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(0L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(-100L, snapshots.get(0).getTime());
        assertEquals(0L, snapshots.get(1).getTime());
        assertEquals(100L, snapshots.get(2).getTime());
    }

    @Test
    public void testSortingWithLargeTimes() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(Long.MAX_VALUE, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(Long.MIN_VALUE, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(0L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(Long.MIN_VALUE, snapshots.get(0).getTime());
        assertEquals(0L, snapshots.get(1).getTime());
        assertEquals(Long.MAX_VALUE, snapshots.get(2).getTime());
    }

    @Test
    public void testSortingAlreadySorted() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(2000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(3000L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(1000L, snapshots.get(0).getTime());
        assertEquals(2000L, snapshots.get(1).getTime());
        assertEquals(3000L, snapshots.get(2).getTime());
    }

    @Test
    public void testSortingReverseSorted() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(3000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(2000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(1000L, snapshots.get(0).getTime());
        assertEquals(2000L, snapshots.get(1).getTime());
        assertEquals(3000L, snapshots.get(2).getTime());
    }

    @Test
    public void testSortingSingleSnapshot() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(1, snapshots.size());
        assertEquals(1000L, snapshots.get(0).getTime());
    }

    @Test
    public void testSortingEmptyList() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        Collections.sort(snapshots, new SnapshotComparator());
        assertTrue(snapshots.isEmpty());
    }

    @Test
    public void testSortingWithDuplicateTimes() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(2000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(1000L, 4502.0, 4503.0, 200));
        snapshots.add(new MarketSnapshot(3000L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        assertEquals(1000L, snapshots.get(0).getTime());
        assertEquals(1000L, snapshots.get(1).getTime());
        assertEquals(2000L, snapshots.get(2).getTime());
        assertEquals(3000L, snapshots.get(3).getTime());
    }

    @Test
    public void testSortingWithDifferentBidAsk() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(2000L, 4505.0, 4506.0, 150));
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot(3000L, 4510.0, 4511.0, 200));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        // Comparator only sorts by time, not by price
        assertEquals(1000L, snapshots.get(0).getTime());
        assertEquals(4500.0, snapshots.get(0).getBid(), 0.01);
        assertEquals(2000L, snapshots.get(1).getTime());
        assertEquals(4505.0, snapshots.get(1).getBid(), 0.01);
        assertEquals(3000L, snapshots.get(2).getTime());
        assertEquals(4510.0, snapshots.get(2).getBid(), 0.01);
    }

    @Test
    public void testEndOfStreamSnapshotSorting() {
        List<MarketSnapshot> snapshots = new ArrayList<>();
        snapshots.add(new MarketSnapshot(3000L, 4500.0, 4501.0, 100));
        snapshots.add(new MarketSnapshot());  // End of stream
        snapshots.add(new MarketSnapshot(1000L, 4500.0, 4501.0, 100));
        
        Collections.sort(snapshots, new SnapshotComparator());
        
        // End of stream has time -1, should be first
        assertEquals(-1L, snapshots.get(0).getTime());
        assertEquals(true, snapshots.get(0).isEndOfStream());
    }

    @Test
    public void testComparatorTransitivity() {
        MarketSnapshot s1 = new MarketSnapshot(1000L, 4500.0, 4501.0, 100);
        MarketSnapshot s2 = new MarketSnapshot(2000L, 4500.0, 4501.0, 100);
        MarketSnapshot s3 = new MarketSnapshot(3000L, 4500.0, 4501.0, 100);
        
        SnapshotComparator comp = new SnapshotComparator();
        
        assertTrue(comp.compare(s1, s2) < 0);
        assertTrue(comp.compare(s2, s3) < 0);
        assertTrue(comp.compare(s1, s3) < 0);
    }
}
