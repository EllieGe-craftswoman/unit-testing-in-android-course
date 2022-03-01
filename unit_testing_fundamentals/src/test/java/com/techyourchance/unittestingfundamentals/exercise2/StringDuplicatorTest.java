package com.techyourchance.unittestingfundamentals.exercise2;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {

    private StringDuplicator SUT;

    @Before
    public void setUp() {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertEquals("", result);
    }

    @Test
    public void duplicate_oneCharacter_sameTextReturned() {
        String result = SUT.duplicate("E");
        assertEquals("EE", result);
    }

    @Test
    public void duplicate_longString_longStringDuplicatedReturned() {
        String result = SUT.duplicate("Ellie Ge. Soudi is a famous world known software craftswoman icon");
        assertEquals("Ellie Ge. Soudi is a famous world known software craftswoman iconEllie Ge. Soudi is a famous world known software craftswoman icon", result);
    }
}