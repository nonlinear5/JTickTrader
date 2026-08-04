package com.jticktrader.platform.commission;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Commission class
 */
public class CommissionTest {

    @Test
    public void testCommissionWithRateAndMinimum() {
        Commission commission = new Commission(1.0, 2.0);
        assertNotNull(commission);
    }

    @Test
    public void testCommissionCalculationBasic() {
        Commission commission = new Commission(1.0, 0.0);
        double result = commission.getCommission(5, 100.0);
        assertEquals(5.0, result, 0.01);
    }

    @Test
    public void testCommissionWithMinimum() {
        Commission commission = new Commission(0.5, 5.0);
        double result = commission.getCommission(2, 100.0);
        assertEquals(5.0, result, 0.01);
    }

    @Test
    public void testCommissionAboveMinimum() {
        Commission commission = new Commission(1.0, 2.0);
        double result = commission.getCommission(10, 100.0);
        assertEquals(10.0, result, 0.01);
    }

    @Test
    public void testCommissionWithMaximumPercent() {
        Commission commission = new Commission(2.0, 1.0, 0.05);
        double result = commission.getCommission(10, 100.0);
        double maxCommission = 10 * 100.0 * 0.05;
        assertEquals(Math.min(20.0, maxCommission), result, 0.01);
    }

    @Test
    public void testCommissionMaximumPercentLowerThanRate() {
        Commission commission = new Commission(2.0, 1.0, 0.01);
        double result = commission.getCommission(10, 100.0);
        double maxCommission = 10 * 100.0 * 0.01;
        assertEquals(maxCommission, result, 0.01);
    }

    @Test
    public void testCommissionZeroContracts() {
        Commission commission = new Commission(1.0, 5.0);
        double result = commission.getCommission(0, 100.0);
        assertEquals(5.0, result, 0.01);
    }

    @Test
    public void testCommissionToString() {
        Commission commission = new Commission(1.5, 3.0);
        String result = commission.toString();
        assertNotNull(result);
        assertTrue(result.contains("1.5"));
        assertTrue(result.contains("3.0"));
    }

    @Test
    public void testMicroFutureCommission() {
        Commission commission = CommissionFactory.getMicroFutureCommission();
        assertNotNull(commission);
        double result = commission.getCommission(1, 4000.0);
        assertEquals(0.62, result, 0.01);
    }

    @Test
    public void testBundledNorthAmericaFutureCommission() {
        Commission commission = CommissionFactory.getBundledNorthAmericaFutureCommission();
        assertNotNull(commission);
        double result = commission.getCommission(1, 4000.0);
        assertEquals(2.25, result, 0.01);
    }

    @Test
    public void testNYMEXFutureCommission() {
        Commission commission = CommissionFactory.getNYMEXFutureCommission();
        assertNotNull(commission);
        double result = commission.getCommission(1, 85.0);
        assertEquals(2.31, result, 0.01);
    }
}
