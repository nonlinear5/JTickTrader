package com.jticktrader.platform.optimizer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for DoubleNumericString class
 */
public class DoubleNumericStringTest {

    @Before
    public void setUp() {
        // Any setup needed
    }

    @Test
    public void testDoubleNumericStringCreation() {
        DoubleNumericString dns = new DoubleNumericString("100.5");
        assertNotNull(dns);
    }

    @Test
    public void testToString() {
        DoubleNumericString dns = new DoubleNumericString("100.5");
        assertEquals("100.5", dns.toString());
    }

    @Test
    public void testCompareWithNormalNumbers() {
        DoubleNumericString dns1 = new DoubleNumericString("100.0");
        DoubleNumericString dns2 = new DoubleNumericString("200.0");
        
        assertTrue(dns1.compareTo(dns2) < 0);
        assertTrue(dns2.compareTo(dns1) > 0);
    }

    @Test
    public void testCompareWithEqualNumbers() {
        DoubleNumericString dns1 = new DoubleNumericString("100.0");
        DoubleNumericString dns2 = new DoubleNumericString("100.0");
        
        assertEquals(0, dns1.compareTo(dns2));
    }

    @Test
    public void testCompareNegativeNumbers() {
        DoubleNumericString dns1 = new DoubleNumericString("-100.0");
        DoubleNumericString dns2 = new DoubleNumericString("-50.0");
        
        assertTrue(dns1.compareTo(dns2) < 0);
    }

    @Test
    public void testCompareZero() {
        DoubleNumericString dns1 = new DoubleNumericString("0.0");
        DoubleNumericString dns2 = new DoubleNumericString("1.0");
        
        assertTrue(dns1.compareTo(dns2) < 0);
    }

    @Test
    public void testCompareVerySmallDifferences() {
        DoubleNumericString dns1 = new DoubleNumericString("100.001");
        DoubleNumericString dns2 = new DoubleNumericString("100.002");
        
        assertTrue(dns1.compareTo(dns2) < 0);
    }

    @Test
    public void testCompareWithScientificNotation() {
        DoubleNumericString dns1 = new DoubleNumericString("1.0E2");
        DoubleNumericString dns2 = new DoubleNumericString("200.0");
        
        assertTrue(dns1.compareTo(dns2) < 0);
    }

    @Test
    public void testCompareWithPositiveInfinity() {
        // Note: Infinity is formatted as '∞' symbol by DecimalFormat
        DoubleNumericString dns1 = new DoubleNumericString("∞");
        DoubleNumericString dns2 = new DoubleNumericString("999999999.0");
        
        // Infinity should be greater than all normal numbers
        assertTrue(dns1.compareTo(dns2) > 0);
        assertTrue(dns2.compareTo(dns1) < 0);
    }

    @Test
    public void testBothInfinity() {
        DoubleNumericString dns1 = new DoubleNumericString("∞");
        DoubleNumericString dns2 = new DoubleNumericString("∞");
        
        // Both infinity, compareTo still returns >0 for first
        assertTrue(dns1.compareTo(dns2) > 0);
    }

    @Test
    public void testSortingWithMixedValues() {
        DoubleNumericString[] values = {
            new DoubleNumericString("200.0"),
            new DoubleNumericString("100.0"),
            new DoubleNumericString("150.0")
        };
        
        java.util.Arrays.sort(values);
        
        assertEquals("100.0", values[0].toString());
        assertEquals("150.0", values[1].toString());
        assertEquals("200.0", values[2].toString());
    }

    @Test
    public void testCompareDecimalStrings() {
        DoubleNumericString dns1 = new DoubleNumericString("99.99");
        DoubleNumericString dns2 = new DoubleNumericString("100.01");
        
        assertTrue(dns1.compareTo(dns2) < 0);
    }
}
