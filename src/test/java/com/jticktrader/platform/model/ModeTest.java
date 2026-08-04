package com.jticktrader.platform.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Mode enum
 */
public class ModeTest {

    @Test
    public void testTradeMode() {
        Mode mode = Mode.Trade;
        assertNotNull(mode);
        assertEquals("Trading", mode.getName());
    }

    @Test
    public void testBackTestMode() {
        Mode mode = Mode.BackTest;
        assertEquals("Back Testing", mode.getName());
    }

    @Test
    public void testBackTestAllMode() {
        Mode mode = Mode.BackTestAll;
        assertEquals("Back Testing All", mode.getName());
    }

    @Test
    public void testForwardTestMode() {
        Mode mode = Mode.ForwardTest;
        assertEquals("Forward Testing", mode.getName());
    }

    @Test
    public void testForceCloseMode() {
        Mode mode = Mode.ForceClose;
        assertEquals("Force Close", mode.getName());
    }

    @Test
    public void testOptimizationMode() {
        Mode mode = Mode.Optimization;
        assertEquals("Optimizing", mode.getName());
    }

    @Test
    public void testAllModesHaveNames() {
        for (Mode mode : Mode.values()) {
            assertNotNull("Mode " + mode + " should have a name", mode.getName());
            assertFalse("Mode name should not be empty", mode.getName().isEmpty());
        }
    }

    @Test
    public void testModeEnumSize() {
        assertEquals(6, Mode.values().length);
    }

    @Test
    public void testModeValueOf() {
        Mode mode = Mode.valueOf("Trade");
        assertEquals(Mode.Trade, mode);
    }

    @Test
    public void testModeComparison() {
        Mode trade = Mode.Trade;
        Mode backtest = Mode.BackTest;
        assertNotEquals(trade, backtest);
    }

    @Test
    public void testModeName() {
        String name = Mode.Optimization.getName();
        assertEquals("Optimizing", name);
        assertNotNull(name);
    }
}
