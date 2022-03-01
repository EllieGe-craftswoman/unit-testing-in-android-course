package com.techyourchance.unittestingfundamentals.example2;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class StringReverserTest {

    StringReverser SUT;

    @Before
    public void setUp() {
        SUT = new StringReverser();
    }

    @Test
    public void reverse_emptyString_emptyStringReturned() {
        String result = SUT.reverse("");
        assertEquals("", result);
    }

    @Test
    public void reverse_singleCharacter_sameStringReturned() {
        String result = SUT.reverse("a");
        assertEquals("a", result);
    }

    @Test
    public void reverse_longString_reversedStringReturned() {
        String result = SUT.reverse("Ellie Soudi");
        assertEquals("iduoS eillE", result);
    }
}