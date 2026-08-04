package com.jticktrader.platform.util.format;

import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.*;

/**
 * Unit tests for NumberFormatterFactory
 */
public class NumberFormatterFactoryTest {

    @Test
    public void testGetNumberFormatterWithZeroDecimalPlaces() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(0);
        assertNotNull(formatter);
        String result = formatter.format(123.456);
        assertEquals("123", result);
    }

    @Test
    public void testGetNumberFormatterWithTwoDecimalPlaces() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(2);
        assertNotNull(formatter);
        String result = formatter.format(123.456);
        assertNotNull(result);
        assertTrue(result.contains("123"));
    }

    @Test
    public void testGetNumberFormatterWithSixDecimalPlaces() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(6);
        assertNotNull(formatter);
        String result = formatter.format(123.456789);
        assertNotNull(result);
    }

    @Test
    public void testGetNumberFormatterWithNegativeNumber() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(2);
        String result = formatter.format(-123.456);
        assertNotNull(result);
        assertTrue(result.contains("-"));
        assertTrue(result.contains("123"));
    }

    @Test
    public void testGetNumberFormatterWithZero() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(2);
        String result = formatter.format(0);
        assertNotNull(result);
        assertTrue(result.contains("0"));
    }

    @Test
    public void testGetNumberFormatterConsistency() {
        DecimalFormat formatter1 = NumberFormatterFactory.getNumberFormatter(3);
        DecimalFormat formatter2 = NumberFormatterFactory.getNumberFormatter(3);
        
        String result1 = formatter1.format(99.9999);
        String result2 = formatter2.format(99.9999);
        
        assertEquals(result1, result2);
    }

    @Test
    public void testGetNumberFormatterVeryLargeNumber() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(2);
        String result = formatter.format(999999999.999);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetNumberFormatterVerySmallNumber() {
        DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(6);
        String result = formatter.format(0.000001);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testMultipleDecimalPlaceVariations() {
        for (int places = 0; places <= 10; places++) {
            DecimalFormat formatter = NumberFormatterFactory.getNumberFormatter(places);
            assertNotNull("Formatter for " + places + " decimal places should not be null", formatter);
            String result = formatter.format(123.456789);
            assertNotNull("Format result should not be null", result);
            assertFalse("Format result should not be empty", result.isEmpty());
        }
    }
}
