package com.techyourchance.unittestingfundamentals.exercise1;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {

    private NegativeNumberValidator SUT;
    @Before
    public void setUp(){
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void testIsNegativeWithNegative(){
        boolean result = SUT.isNegative(-4);
        Assert.assertTrue(result);
    }
    @Test
    public void testIsNegativeWithPositive(){
        boolean result = SUT.isNegative(4);
        Assert.assertFalse(result);
    }
    @Test
    public void testIsNegativeWithZero(){
        boolean result = SUT.isNegative(0);
        Assert.assertFalse(result);
    }
    

}