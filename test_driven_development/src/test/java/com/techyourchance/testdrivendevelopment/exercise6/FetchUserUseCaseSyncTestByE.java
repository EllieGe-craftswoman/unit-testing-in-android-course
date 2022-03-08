package com.techyourchance.testdrivendevelopment.exercise6;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import java.util.Collection;
import java.util.Objects;


@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncTestByE {

    // region constants
    public static final String USER_ID = "UID82922";
    public static final String USERNAME = "sameUsername";
    public static final User USER = new User(USER_ID, USERNAME);
    // endregion constants

    // region helper fields
    @Mock UsersCache mUsersCache;
    @Mock FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    // endregion helper fields

    FetchUserUseCaseSync SUT;

    @Before
    public void setup() throws NetworkErrorException {
        SUT = new FetchUserUseCaseSyncImpl(mFetchUserHttpEndpointSync, mUsersCache);
        success();
    }

    @Test
    public void fetchUser_correctUserIdPassedToCache() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCache).getUser(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUser_inCache_fetchedFromCache() {
        //Arrange
        addToCache();
        //Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        if(mUsersCache.getUser(USER_ID) != null && result.getUser() != null){
            assertEquals(Objects.requireNonNull(mUsersCache.getUser(USER_ID)).getUserId(), result.getUser().getUserId());
        } else {
            fail("Returned User is null");
        }
    }

    @Test
    public void fetchUser_inCache_endpointNotHit() {
        // Arrange
        addToCache();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verifyNoMoreInteractions(mFetchUserHttpEndpointSync);
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_userIdPassedToEndpoint() throws NetworkErrorException {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        //Act
        SUT.fetchUserSync(USER_ID);
        //Assert
        verify(mFetchUserHttpEndpointSync).fetchUserSync(ac.capture());
        assertEquals(USER_ID, ac.getValue());
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_success_successReturned() {
        //Arrange
        //Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertEquals(FetchUserUseCaseSync.Status.SUCCESS, result.getStatus());
        assertEquals(USER, result.getUser());
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_generalError_failureReturned() throws NetworkErrorException {
        //Arrange
        generalError();
        //Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertEquals(FetchUserUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_authError_failureReturned() throws NetworkErrorException {
        //Arrange
        authError();
        //Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertEquals(FetchUserUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_generalError_noInteractionWithCache() throws NetworkErrorException {
        //Arrange
        generalError();
        //Act
        SUT.fetchUserSync(USER_ID);
        //Assert
        Mockito.verify(mUsersCache, Mockito.times(1)).getUser(Mockito.anyString());
        Mockito.verify(mUsersCache, Mockito.times(0)).cacheUser(any(User.class));
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_authError_noInteractionWithCache() throws NetworkErrorException {
        //Arrange
        authError();
        //Act
        SUT.fetchUserSync(USER_ID);
        //Assert
        Mockito.verify(mUsersCache, Mockito.times(1)).getUser(Mockito.anyString());
        Mockito.verify(mUsersCache, Mockito.times(0)).cacheUser(any(User.class));
    }

    @Test
    public void fetchUser_notInCache_fetchFromServer_networkError_networkErrorReturned() throws NetworkErrorException {
        //Arrange
        networkError();
        //Act
        FetchUserUseCaseSync.UseCaseResult result = SUT.fetchUserSync(USER_ID);
        //Assert
        assertEquals(FetchUserUseCaseSync.Status.NETWORK_ERROR, result.getStatus());
    }

    @Test
    public void fetchUser_fetchedFromServer_storedInCache() {
        //Arrange
        //Act
        SUT.fetchUserSync(USER_ID);
        addToCache();
        //Assert
        if(mUsersCache.getUser(USER_ID) != null){
            assertEquals(USER.getUserId(), Objects.requireNonNull(mUsersCache.getUser(USER_ID)).getUserId());
            assertEquals(USER.getUsername(), Objects.requireNonNull(mUsersCache.getUser(USER_ID)).getUsername());
            assertEquals(USER.hashCode(), Objects.requireNonNull(mUsersCache.getUser(USER_ID)).hashCode());
        } else {
            fail();
        }
    }

    // region helper methods
    private void addToCache() {
        when(mUsersCache.getUser(anyString())).thenReturn(USER);
    }

    private void success() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSync.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void authError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSync.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR, "", ""));
    }

    private void generalError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSync.fetchUserSync(any(String.class)))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR, "", ""));
    }

    private void networkError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSync.fetchUserSync(any(String.class)))
                .thenThrow(new NetworkErrorException());
    }
    // endregion helper methods
}
