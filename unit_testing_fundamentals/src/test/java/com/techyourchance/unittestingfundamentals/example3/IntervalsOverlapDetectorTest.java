package com.techyourchance.unittestingfundamentals.example3;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IntervalsOverlapDetectorTest {

    IntervalsOverlapDetector SUT;

    @Before
    public void setUp(){
        SUT = new IntervalsOverlapDetector();
    }

    //Interval1 is before interval2
    @Test
    public void isIntervalValid_interval1NotValid_nullValueStaysUnchanged() {
        Interval interval1;
        try{
            interval1 = new Interval(5, 4);
        } catch (IllegalArgumentException ex){
            interval1 = null;
        }
        assertNull(interval1);
    }

    //Interval1 is before interval2
    @Test
    public void isOverlap_interval1BeforeInterval2_falseReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 overlaps Interval2 on start
    @Test
    public void isOverlap_interval1OverlapsInterval2OnStart_trueReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    //Interval1 is contained contains Interval2
    @Test
    public void isOverlap_interval1IsContainedInInterval2_trueReturned() {
        Interval interval1 = new Interval(5, 10);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    //Interval1 contains Interval2
    @Test
    public void isOverlap_interval1ContainsInterval2_trueReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(2, 4);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    //Interval1 overlaps Interval2 on end
    @Test
    public void isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() {
        Interval interval1 = new Interval(10, 15);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertTrue(result);
    }

    //Interval1 is after Interval2
    @Test
    public void isOverlap_interval1isAfterInterval2_falseReturned() {
        Interval interval1 = new Interval(16, 20);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }

    //Interval1 is after adjacent Interval2
    @Test
    public void isOverlap_interval1isAfterAdjacentInterval2_falseReturned() {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-3, -1);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertFalse(result);
    }
}