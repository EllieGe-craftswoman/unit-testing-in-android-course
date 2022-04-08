package com.techyourchance.unittestinginandroid.example13;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;


@RunWith(MockitoJUnitRunner.class)
public class AndroidUnitTestingProblemsByETest {

    // region constants

    // endregion constants

    // region helper fields

    // endregion helper fields

    AndroidUnitTestingProblemsByE SUT;

    @Before
    public void setup() {
        SUT = new AndroidUnitTestingProblemsByE();

    }

    @Test
    public void testStaticApiCall() {
        //Arrange
        //Act
        SUT.callStaticAndroidApi("");
        //Assert
        assertTrue(true);
    }
    // region helper methods

    // endregion helper methods

    // region helper helper classes

    // endregion helper helper classes
}
