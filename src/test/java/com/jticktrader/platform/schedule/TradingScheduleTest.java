package com.jticktrader.platform.schedule;

import org.junit.Test;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Unit tests for TradingSchedule class
 */
public class TradingScheduleTest {

    @Test
    public void testTradingScheduleCreation() {
        TradingSchedule schedule = new TradingSchedule("9:30", "16:00", "America/New_York");
        assertNotNull(schedule);
    }

    @Test
    public void testTradingScheduleTimeZone() {
        TradingSchedule schedule = new TradingSchedule("9:30", "16:00", "America/New_York");
        TimeZone tz = schedule.getTimeZone();
        assertEquals("America/New_York", tz.getID());
    }

    @Test
    public void testTradingScheduleToString() {
        TradingSchedule schedule = new TradingSchedule("9:30", "16:00", "America/New_York");
        String result = schedule.toString();
        assertNotNull(result);
        assertTrue(result.contains("9:30"));
        assertTrue(result.contains("16:00"));
        assertTrue(result.contains("America/New_York"));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidTimeZone() {
        new TradingSchedule("9:30", "16:00", "InvalidTimeZone");
    }

    @Test(expected = RuntimeException.class)
    public void testEndTimeBeforeStartTime() {
        new TradingSchedule("16:00", "9:30", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testEndTimeEqualToStartTime() {
        new TradingSchedule("9:30", "9:30", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidTimeFormat() {
        new TradingSchedule("9:30:45", "16:00", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidHours() {
        new TradingSchedule("25:30", "16:00", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeHours() {
        new TradingSchedule("-1:30", "16:00", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidMinutes() {
        new TradingSchedule("9:65", "16:00", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeMinutes() {
        new TradingSchedule("9:-15", "16:00", "America/New_York");
    }

    @Test
    public void testValidTradingSchedules() {
        TradingSchedule schedule1 = new TradingSchedule("9:00", "17:00", "America/New_York");
        TradingSchedule schedule2 = new TradingSchedule("0:00", "23:59", "Europe/London");
        TradingSchedule schedule3 = new TradingSchedule("8:30", "15:00", "Asia/Tokyo");
        
        assertNotNull(schedule1);
        assertNotNull(schedule2);
        assertNotNull(schedule3);
    }

    @Test
    public void testDifferentTimeZones() {
        TradingSchedule nySchedule = new TradingSchedule("9:30", "16:00", "America/New_York");
        TradingSchedule londonSchedule = new TradingSchedule("8:00", "16:30", "Europe/London");
        TradingSchedule tokyoSchedule = new TradingSchedule("9:00", "15:00", "Asia/Tokyo");
        
        assertEquals("America/New_York", nySchedule.getTimeZone().getID());
        assertEquals("Europe/London", londonSchedule.getTimeZone().getID());
        assertEquals("Asia/Tokyo", tokyoSchedule.getTimeZone().getID());
    }

    @Test
    public void testMidnightTimes() {
        TradingSchedule schedule = new TradingSchedule("0:00", "23:59", "America/New_York");
        assertNotNull(schedule);
    }

    @Test
    public void testSingleMinuteTradingWindow() {
        TradingSchedule schedule = new TradingSchedule("12:00", "12:01", "America/New_York");
        assertNotNull(schedule);
    }

    @Test(expected = RuntimeException.class)
    public void testNonNumericHours() {
        new TradingSchedule("AB:30", "16:00", "America/New_York");
    }

    @Test(expected = RuntimeException.class)
    public void testNonNumericMinutes() {
        new TradingSchedule("9:XY", "16:00", "America/New_York");
    }
}
