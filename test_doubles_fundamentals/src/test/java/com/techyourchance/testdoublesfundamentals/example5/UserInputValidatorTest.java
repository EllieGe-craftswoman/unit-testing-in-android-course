package com.techyourchance.testdoublesfundamentals.example5;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserInputValidatorTest {

    public static final String FULL_NAME = "Ellie Soudi";
    public static final String USERNAME = "Ellie_S";
    UserInputValidator SUT;

    @Before
    public void setUp() {
        SUT = new UserInputValidator();
    }

    @Test
    public void isValidFullName_validFullName_trueReturned() {
        boolean result = SUT.isValidFullName(FULL_NAME);
        assertTrue(result);
    }

    @Test
    public void isValidFullName_invalidFullName_falseReturned() {
        boolean result = SUT.isValidFullName("");
        assertFalse(result);
    }
    /* Not Testable due to use of static function which is hidden and can't be seen here
       And we should avoid mocking statics
    @Test
    public void isValidUsername_validUsername_trueReturned() {
        boolean result = SUT.isValidUsername(USERNAME);
        assertTrue(result);
    }

    @Test
    public void isValidUsername_invalidUsername_falseReturned() {
        boolean result = SUT.isValidUsername("");
        assertFalse(result);
    }
    */
}