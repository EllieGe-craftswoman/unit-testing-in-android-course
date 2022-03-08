package com.techyourchance.testdrivendevelopment.example9;

import static com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR;
import static com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR;
import static com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.SUCCESS;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;

import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;


@RunWith(MockitoJUnitRunner.class)
public class AddToCartUseCaseSyncTestByE {

    // region constants
    public static final String OFFER_ID = "ofId64722";
    public static final int AMOUNT = 34;
    // endregion constants

    // region helper fields
    @Mock
    AddToCartHttpEndpointSync mAddToCartHttpEndpointSync;
    // endregion helper fields

    AddToCartUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new AddToCartUseCaseSync(mAddToCartHttpEndpointSync);
        success();
    }

    @Test
    public void addToCartSync_correctParametersPassedToEndPoint() throws Exception {
        //Arrange
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);
        //Act
        SUT.addToCartSync(OFFER_ID, AMOUNT);
        //Assert
        verify(mAddToCartHttpEndpointSync).addToCartSync(ac.capture());
        assertEquals(OFFER_ID, ac.getValue().getOfferId());
        assertEquals(AMOUNT, ac.getValue().getAmount());
    }

    @Test
    public void addToCartSync_success_successReturned() {
        //Arrange
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        //Assert
        assertEquals(AddToCartUseCaseSync.UseCaseResult.SUCCESS, result);
    }

    @Test
    public void addToCartSync_authError_failureReturned() throws NetworkErrorException {
        //Arrange
        authError();
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        //Assert
        assertEquals(AddToCartUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void addToCartSync_generalError_failureReturned() throws NetworkErrorException {
        //Arrange
        generalError();
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        //Assert
        assertEquals(AddToCartUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void addToCartSync_networkError_failureReturned() throws NetworkErrorException {
        //Arrange
        networkError();
        //Act
        AddToCartUseCaseSync.UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        //Assert
        assertEquals(AddToCartUseCaseSync.UseCaseResult.NETWORK_ERROR, result);
    }

    // region helper methods
    private void authError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSync.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AUTH_ERROR);
    }

    private void generalError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSync.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(GENERAL_ERROR);
    }

    private void networkError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSync.addToCartSync(any(CartItemScheme.class)))
                .thenThrow(new NetworkErrorException());
    }

    private void success() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSync.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(SUCCESS);
    }
    // endregion helper methods

}
