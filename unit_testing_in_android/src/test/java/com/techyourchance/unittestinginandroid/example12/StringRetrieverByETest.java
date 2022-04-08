package com.techyourchance.unittestinginandroid.example12;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;

import android.content.Context;


@RunWith(MockitoJUnitRunner.class)
public class StringRetrieverByETest {
    public static final int ID = 10;
    public static final String STRING = "Ellie Soudi";

    // region constants

    // endregion constants

    // region helper fields
    @Mock
    Context mContextMock;
    // endregion helper fields

    StringRetrieverByE SUT;

    @Before
    public void setup() {
        SUT = new StringRetrieverByE(mContextMock);

    }

    @Test
    public void getString_correctParameterPassedToContext() {
        //Arrange
        //Act
        SUT.getString(ID);
        //Assert
        verify(mContextMock).getString(ID);
    }

    @Test
    public void getString_correctResultReturned() {
        //Arrange
        when(mContextMock.getString(ID)).thenReturn(STRING);
        //Act
        String result = SUT.getString(ID);
        //Assert
        assertEquals(STRING, result);
    }

    // region helper methods

    // endregion helper methods

    // region helper helper classes

    // endregion helper helper classes
}
