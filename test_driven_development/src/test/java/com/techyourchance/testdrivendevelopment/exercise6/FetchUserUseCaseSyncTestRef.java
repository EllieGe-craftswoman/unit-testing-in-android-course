package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncTestRef {

    // region constants ----------------------------------------------------------------------------

    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final User USER = new User(USER_ID, USERNAME);

    // endregion constants -------------------------------------------------------------------------


    // region helper fields ------------------------------------------------------------------------

    FetchUserHttpEndpointSyncTestDouble mFetchUserHttpEndpointSyncTestDouble;
    @Mock UsersCache mUsersCacheMock;

    // endregion helper fields ---------------------------------------------------------------------

    private FetchUserUseCaseSync SUT;

    @Before
    public void setup(){
        mFetchUserHttpEndpointSyncTestDouble = new FetchUserHttpEndpointSyncTestDouble();

        // TODO: assign your implementation of FetchUserUseCaseSync to SUT
         SUT = new FetchUserUseCaseSyncImpl(mFetchUserHttpEndpointSyncTestDouble, mUsersCacheMock);

        userNotInCache();
        endpointSuccess();
    }

    @Test
    public void fetchUserSync_notInCache_correctUserIdPassedToEndpoint(){
        // Arrange
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(mFetchUserHttpEndpointSyncTestDouble.mUserId, is(USER_ID));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_successStatus(){
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_correctUserReturned(){
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(USER));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_userCached(){
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock).cacheUser(ac.capture());
        assertThat(ac.getValue(), is(USER));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_failureStatus(){
        // Arrange
        endpointAuthError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_nullUserReturned(){
        // Arrange
        endpointAuthError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), nullValue());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_nothingCached(){
        // Arrange
        endpointAuthError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointServerError_failureStatus(){
        // Arrange
        endpointServerError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointServerError_nullUserReturned(){
        // Arrange
        endpointServerError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(nullValue()));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointServerError_nothingCached(){
        // Arrange
        endpointServerError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_failureStatus(){
        // Arrange
        endpointNetworkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.NETWORK_ERROR));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_nullUserReturned(){
        // Arrange
        endpointNetworkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(nullValue()));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_nothingCached(){
        // Arrange
        endpointNetworkError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_correctUserIdPassedToCache(){
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock).getUser(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUserSync_inCache_successStatus(){
        // Arrange
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void fetchUserSync_inCache_cachedUserReturned(){
        // Arrange
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(USER));
    }

    @Test
    public void fetchUserSync_inCache_endpointNotPolled(){
        // Arrange
        userInCache();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(mFetchUserHttpEndpointSyncTestDouble.mRequestCount, is(0));
    }

    // region helper methods -----------------------------------------------------------------------

    private void userNotInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(null);
    }

    private void userInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(USER);
    }

    private void endpointSuccess() {
        // endpoint test double is set up for success by default; this method is for clarity of intent
    }

    private void endpointAuthError() {
        mFetchUserHttpEndpointSyncTestDouble.mAuthError = true;
    }

    private void endpointServerError() {
        mFetchUserHttpEndpointSyncTestDouble.mServerError = true;
    }

    private void endpointNetworkError() {
        mFetchUserHttpEndpointSyncTestDouble.mNetworkError = true;
    }

    // endregion helper methods --------------------------------------------------------------------


    // region helper classes -----------------------------------------------------------------------

    private class FetchUserHttpEndpointSyncTestDouble implements FetchUserHttpEndpointSync {

        private int mRequestCount;
        private String mUserId = "";
        public boolean mAuthError;
        public boolean mServerError;
        public boolean mNetworkError;

        @Override
        public EndpointResult fetchUserSync(String userId) throws NetworkErrorException {
            mRequestCount ++;
            mUserId = userId;

            if (mAuthError) {
                return new EndpointResult(EndpointStatus.AUTH_ERROR, "", "");
            } else if (mServerError) {
                return new EndpointResult(EndpointStatus.GENERAL_ERROR, "", "");
            } else if (mNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointStatus.SUCCESS, USER_ID, USERNAME);
            }
        }
    }

    // endregion helper classes --------------------------------------------------------------------

}