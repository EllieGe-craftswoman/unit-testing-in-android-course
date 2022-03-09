package com.techyourchance.testdrivendevelopment.exercise7;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;


@RunWith(MockitoJUnitRunner.class)
public class GetReputationUseCaseSyncImplTest {

    // region constants
    public static final int REPUTATION = 5;
    // endregion constants

    // region helper fields
    @Mock GetReputationHttpEndpointSync mGetReputationHttpEndpointSync;
    // endregion helper fields

    GetReputationUseCaseSyncImpl SUT;

    @Before
    public void setup() {
        SUT = new GetReputationUseCaseSyncImpl(mGetReputationHttpEndpointSync);
        success();
    }

    @Test
    public void getReputationSync_success_successReturned() {
        //Arrange
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputation();
        //Assert
        assertEquals(GetReputationUseCaseSync.Status.SUCCESS, result.getStatus());
    }

    @Test
    public void getReputationSync_success_reputationReturned() {
        //Arrange
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputation();
        //Assert
        assertEquals(REPUTATION, result.getReputation());
    }

    @Test
    public void getReputationSync_generalError_failureReturned() {
        //Arrange
        generalError();
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputation();
        //Assert
        assertEquals(GetReputationUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void getReputationSync_nError_failureReturned() {
        //Arrange
        networkError();
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputation();
        //Assert
        assertEquals(GetReputationUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void getReputationSync_generalError_zeroReputation() {
        //Arrange
        generalError();
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputation();
        //Assert
        assertEquals(0, result.getReputation());
    }

    @Test
    public void getReputationSync_nError_zeroReputation() {
        //Arrange
        networkError();
        //Act
        GetReputationUseCaseSync.UseCaseResult result = SUT.getReputation();
        //Assert
        assertEquals(0, result.getReputation());
    }

    // region helper methods
    private void success() {
        when(mGetReputationHttpEndpointSync.getReputationSync()).thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION));
    }

    private void generalError() {
        when(mGetReputationHttpEndpointSync.getReputationSync()).thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, 0));
    }

    private void networkError() {
        when(mGetReputationHttpEndpointSync.getReputationSync()).thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, 0));
    }
    // endregion helper methods
}
