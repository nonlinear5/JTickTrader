package com.jticktrader.platform.util;

import org.junit.Test;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Unit tests for FuturesContractCalculator
 */
public class FuturesContractCalculatorTest {

    @Test
    public void testFrontMonthInMayBeforeJuneRollover() {
        LocalDate date = LocalDate.of(2026, 5, 14);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESM26", result);
    }

    @Test
    public void testFrontMonthOnFirstFridayOfJune() {
        LocalDate date = LocalDate.of(2026, 6, 5);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESM26", result);
    }

    @Test
    public void testFrontMonthOnSecondFridayOfJune() {
        LocalDate date = LocalDate.of(2026, 6, 12);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESU26", result);
    }

    @Test
    public void testFrontMonthDecemberRollsToNextYear() {
        LocalDate date = LocalDate.of(2026, 12, 25);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESH27", result);
    }

    @Test
    public void testFrontMonthInJanuaryPointsToMarch() {
        LocalDate date = LocalDate.of(2026, 1, 15);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESH26", result);
    }

    @Test
    public void testFrontMonthInFebruaryPointsToMarch() {
        LocalDate date = LocalDate.of(2026, 2, 28);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESH26", result);
    }

    @Test
    public void testFrontMonthInMarchBeforeRollover() {
        LocalDate date = LocalDate.of(2026, 3, 5);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESH26", result);
    }

    @Test
    public void testFrontMonthInApril() {
        LocalDate date = LocalDate.of(2026, 4, 15);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESM26", result);
    }

    @Test
    public void testFrontMonthInJuly() {
        LocalDate date = LocalDate.of(2026, 7, 10);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESU26", result);
    }

    @Test
    public void testFrontMonthInSeptember() {
        LocalDate date = LocalDate.of(2026, 9, 15);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESZ26", result);
    }

    @Test
    public void testFrontMonthInOctober() {
        LocalDate date = LocalDate.of(2026, 10, 15);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESZ26", result);
    }

    @Test
    public void testFrontMonthInNovember() {
        LocalDate date = LocalDate.of(2026, 11, 15);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertEquals("ESZ26", result);
    }

    @Test
    public void testContractCodeFormatIsValid() {
        LocalDate date = LocalDate.of(2026, 5, 14);
        String result = FuturesContractCalculator.getFrontMonthContract(date);
        assertTrue(result.matches("ES[HMUZ]\\d{2}"));
    }

    @Test
    public void testDifferentYears() {
        LocalDate date2025 = LocalDate.of(2025, 1, 15);
        LocalDate date2027 = LocalDate.of(2027, 1, 15);
        String result2025 = FuturesContractCalculator.getFrontMonthContract(date2025);
        String result2027 = FuturesContractCalculator.getFrontMonthContract(date2027);
        
        assertTrue(result2025.endsWith("25"));
        assertTrue(result2027.endsWith("27"));
    }
}
