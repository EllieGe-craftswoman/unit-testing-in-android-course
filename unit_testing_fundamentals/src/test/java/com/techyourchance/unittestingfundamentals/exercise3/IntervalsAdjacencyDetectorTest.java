package com.techyourchance.unittestingfundamentals.exercise3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() {
        SUT = new IntervalsAdjacencyDetector();
    }

    //Interval1 is Adjacent to Interval2 on Interval1's End ==> true
    @Test
    public void isAdjacent_Interval1PriorAdjacentToInterval2_trueReturned() {
        Interval interval1 = new Interval(-1,3);
        Interval interval2 = new Interval(3, 4);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertTrue(result);
    }

    //Interval1 is Adjacent to Interval2 on Interval1's Start ==> true
    @Test
    public void isAdjacent_Interval1LatterAdjacentToInterval2_trueReturned() {
        Interval interval1 = new Interval(4, 6);
        Interval interval2 = new Interval(3, 4);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertTrue(result);
    }

    //Interval1 is before Interval2 ==> false
    @Test
    public void isAdjacent_Interval1PriorToInterval2_falseReturned() {
        Interval interval1 = new Interval(-1, 3);
        Interval interval2 = new Interval(5, 8);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 is after Interval2 ==> false
    @Test
    public void isAdjacent_Interval1LaterThanInterval2_falseReturned() {
        Interval interval1 = new Interval(8, 12);
        Interval interval2 = new Interval(5, 7);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 is overlaps Interval2 on Interval2 start ==> false
    @Test
    public void isAdjacent_Interval1OverlapsInterval2OnInterval2Start_falseReturned() {
        Interval interval1 = new Interval(2, 6);
        Interval interval2 = new Interval(5, 8);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 is overlaps Interval2 on Interval2 end ==> false
    @Test
    public void isAdjacent_Interval1OverlapsInterval2OnInterval2End_falseReturned() {
        Interval interval1 = new Interval(7, 12);
        Interval interval2 = new Interval(5, 8);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 is between Interval2's start and end ==> false
    @Test
    public void isAdjacent_Interval1DuringInterval2_falseReturned() {
        Interval interval1 = new Interval(6, 7);
        Interval interval2 = new Interval(5, 8);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    //Interval2 is between Interval1's start and end ==> false
    @Test
    public void isAdjacent_Interval2DuringInterval1_falseReturned() {
        Interval interval1 = new Interval(8, 12);
        Interval interval2 = new Interval(9, 10);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 and Interval2 are equal  ==> false
    @Test
    public void isAdjacent_Interval1MatchesInterval2_falseReturned() {
        Interval interval1 = new Interval(9, 10);
        Interval interval2 = new Interval(9, 10);
        boolean result = SUT.isAdjacent(interval1, interval2);
        assertFalse(result);
    }
}