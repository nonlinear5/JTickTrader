package com.jticktrader.platform.position;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Position class
 */
public class PositionTest {

    @Test
    public void testPositionInitialization() {
        Position position = new Position(1000L, 5, 100.5);
        assertNotNull(position);
    }

    @Test
    public void testGetPosition() {
        Position position = new Position(1000L, 10, 100.5);
        assertEquals(10, position.getPosition());
    }

    @Test
    public void testGetTime() {
        long expectedTime = 1000L;
        Position position = new Position(expectedTime, 5, 100.5);
        assertEquals(expectedTime, position.getTime());
    }

    @Test
    public void testGetAvgFillPrice() {
        Position position = new Position(1000L, 5, 100.5);
        assertEquals(100.5, position.getAvgFillPrice(), 0.01);
    }

    @Test
    public void testPositionWithNegativePosition() {
        Position position = new Position(1000L, -10, 100.5);
        assertEquals(-10, position.getPosition());
    }

    @Test
    public void testPositionWithZeroPosition() {
        Position position = new Position(1000L, 0, 100.5);
        assertEquals(0, position.getPosition());
    }

    @Test
    public void testPositionWithZeroPrice() {
        Position position = new Position(1000L, 5, 0.0);
        assertEquals(0.0, position.getAvgFillPrice(), 0.01);
    }

    @Test
    public void testPositionWithNegativePrice() {
        Position position = new Position(1000L, 5, -50.0);
        assertEquals(-50.0, position.getAvgFillPrice(), 0.01);
    }

    @Test
    public void testPositionWithLargeValues() {
        Position position = new Position(9999999999L, 1000000, 9999.99);
        assertEquals(1000000, position.getPosition());
        assertEquals(9999.99, position.getAvgFillPrice(), 0.01);
        assertEquals(9999999999L, position.getTime());
    }

    @Test
    public void testMultiplePositionsCanBeCreated() {
        Position pos1 = new Position(1000L, 5, 100.0);
        Position pos2 = new Position(2000L, 10, 105.0);
        Position pos3 = new Position(3000L, -5, 98.0);

        assertEquals(5, pos1.getPosition());
        assertEquals(10, pos2.getPosition());
        assertEquals(-5, pos3.getPosition());
    }
}
