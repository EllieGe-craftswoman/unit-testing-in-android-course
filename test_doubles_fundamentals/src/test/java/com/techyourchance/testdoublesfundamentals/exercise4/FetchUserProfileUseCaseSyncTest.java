package com.techyourchance.testdoublesfundamentals.exercise4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.util.Log;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "UID3092";
    public static final String IMAGE_URL = "https://thumbor.forbes.com/thumbor/400x400/https://specials-images.forbesimg.com/imageserve/555df4dce4b0bacdbd753a6a/960x960.jpg?fit=scale&background=000000";
    public static final String FULL_NAME = "Ellie Ge. Soudi";

    UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSyncTd;
    UsersCacheTd usersCacheTd;

    FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() {
        mUserProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncTd, usersCacheTd);
    }

    @Test
    public void fetchUserSync_success_userIdPassesCorrectlyToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID);
        assertEquals(USER_ID, mUserProfileHttpEndpointSyncTd.mUserId);
    }

    @Test
    public void fetchUserSync_success_successReturned() {
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS, result);
    }

    @Test
    public void fetchUserSync_serverError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void fetchUserSync_authError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void fetchUserSync_generalError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void fetchUserSync_success_UserCached() {
        SUT.fetchUserProfileSync(USER_ID);
        assertNotNull(usersCacheTd.getUser(USER_ID));
        try {
            assertEquals(USER_ID, Objects.requireNonNull(usersCacheTd.getUser(USER_ID)).getUserId());
        } catch (NullPointerException ex){
            Assert.fail();
        }
    }

    @Test
    public void fetchUserSync_serverError_UserNotCached() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserSync_authError_UserNotCached() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserSync_generalError_UserNotCached() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserSync_serverError_NoInteractionWithUserCache() {
        mUserProfileHttpEndpointSyncTd.isServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(0, usersCacheTd.noOfInteractionsToAddUser);
        Assert.assertEquals(0, usersCacheTd.noOfInteractionsToGetUser);
    }

    @Test
    public void fetchUserSync_authError_NoInteractionWithUserCache() {
        mUserProfileHttpEndpointSyncTd.isAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(0, usersCacheTd.noOfInteractionsToAddUser);
        Assert.assertEquals(0, usersCacheTd.noOfInteractionsToGetUser);
    }

    @Test
    public void fetchUserSync_generalError_NoInteractionWithUserCache() {
        mUserProfileHttpEndpointSyncTd.isGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        Assert.assertEquals(0, usersCacheTd.noOfInteractionsToAddUser);
        Assert.assertEquals(0, usersCacheTd.noOfInteractionsToGetUser);
    }

    @Test
    public void fetchUserSync_networkError_successReturned() {
        mUserProfileHttpEndpointSyncTd.isNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR, result);
    }

    /*--------------------------------------------------------------------------------------------*/
    //Helper classes
    public static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        public String mUserId;
        public boolean isGeneralError;
        public boolean isServerError;
        public boolean isAuthError;
        public boolean isNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (isGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (isServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (isAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (isNetworkError){
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, mUserId, FULL_NAME, IMAGE_URL);
            }
        }
    }

    public static class UsersCacheTd implements UsersCache {

        public ArrayList<User> mUsersCache = new ArrayList<>();
        public int noOfInteractionsToAddUser = 0;
        public int noOfInteractionsToGetUser = 0;

        @Override
        public void cacheUser(User user) {
            noOfInteractionsToAddUser++;
            mUsersCache.add(user);
        }

        @Override
        public @Nullable User getUser(String userId) {
            noOfInteractionsToGetUser++;
            for(int i = 0; i < mUsersCache.size(); i++){
                if(mUsersCache.get(i).getUserId().equals(userId)) return mUsersCache.get(i);
            }
            return null;
        }
    }
}