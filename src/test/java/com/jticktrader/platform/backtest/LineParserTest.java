package com.jticktrader.platform.backtest;

import com.jticktrader.platform.marketbook.MarketSnapshot;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for LineParser class
 */
public class LineParserTest {
    private LineParser parser;

    @Before
    public void setUp() {
        parser = new LineParser(null);
    }

    @Test
    public void testLineParserCreation() {
        assertNotNull(parser);
    }

    @Test
    public void testCommentLineIsNotMarketData() {
        MarketSnapshot result = parser.process("# This is a comment");
        assertNull(result);
    }

    @Test
    public void testPropertyLineIsNotMarketData() {
        parser.process("timeZone=America/New_York");
        assertNull(parser.process("someProperty=someValue"));
    }

    @Test
    public void testBlankLineIsNotMarketData() {
        assertNull(parser.process(""));
        assertNull(parser.process("   "));
        assertNull(parser.process("\t"));
    }

    @Test
    public void testTimeZonePropertyProcessing() {
        MarketSnapshot result = parser.process("timeZone=America/New_York");
        assertNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidColumnCountThrowsException() {
        parser.process("timeZone=America/New_York");
        parser.process("020226093015,4500,4501");
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeVolumeThrowsException() {
        parser.process("timeZone=America/New_York");
        parser.process("020226093015,093015,4500.0,4501.0,-100");
    }

    @Test(expected = RuntimeException.class)
    public void testMissingTimeZoneThrowsException() {
        parser.process("020226093015,093015,4500.0,4501.0,100");
    }

    @Test
    public void testMultipleCommentLines() {
        assertNull(parser.process("# Comment 1"));
        assertNull(parser.process("# Comment 2"));
        assertNull(parser.process("# Comment 3"));
    }

    @Test
    public void testMixedCommentAndBlankLines() {
        assertNull(parser.process("# Comment"));
        assertNull(parser.process(""));
        assertNull(parser.process("# Another comment"));
        assertNull(parser.process("   "));
    }

    @Test
    public void testLineParserWithWhitespace() {
        // Comments must start with # without leading whitespace
        // Lines with leading whitespace are treated as data
        assertNull(parser.process("   "));
    }
}
