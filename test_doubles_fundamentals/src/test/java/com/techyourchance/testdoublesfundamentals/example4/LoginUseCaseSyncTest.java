package com.techyourchance.testdoublesfundamentals.example4;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import com.techyourchance.testdoublesfundamentals.example4.authtoken.AuthTokenCache;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.EventBusPoster;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

public class LoginUseCaseSyncTest {

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";

    LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
    EventBusPosterTd mEventBusPosterTd;

    LoginUseCaseSync SUT;

    @Before
    public void setUp() {
        mLoginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd() ;
        mAuthTokenCacheTd = new AuthTokenCacheTd();
        mEventBusPosterTd = new EventBusPosterTd();
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd);
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME, PASSWORD);

        assertEquals(USERNAME, mLoginHttpEndpointSyncTd.mUsername);
        assertEquals(PASSWORD, mLoginHttpEndpointSyncTd.mPassword);

    }

    @Test
    public void loginSync_success_authTokenCached(){
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(AUTH_TOKEN, mAuthTokenCacheTd.mAuthToken);
    }

    @Test
    public void loginSync_generalError_AuthTokenNotCached(){
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals("", mAuthTokenCacheTd.getAuthToken()); // not touched or changed
    }

    @Test
    public void loginSync_authError_AuthTokenNotCached(){
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals("", mAuthTokenCacheTd.getAuthToken()); // not touched or changed
    }

    @Test
    public void loginSync_serverError_AuthTokenNotCached(){
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals("", mAuthTokenCacheTd.getAuthToken()); // not touched or changed
    }

    @Test
    public void loginSync_success_loggedInEventPosted() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertTrue(mEventBusPosterTd.mEvent instanceof LoggedInEvent);
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.mInteractionsCount);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.mInteractionsCount);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.mInteractionsCount);
    }

    @Test
    public void loginSync_success_successReturned(){
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.SUCCESS, result);
    }

    @Test
    public void loginSync_serverError_failureReturned(){
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_authError_failureReturned(){
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_generalError_failureReturned(){
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_networkError_networkErrorReturned(){
        mLoginHttpEndpointSyncTd.mIsNetworkError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR, result);
    }

    /*--------------------------------------------------------------------------------------------------*/
    //Helper classes
    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {

        public String mUsername;
        public String mPassword;
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            mUsername = username;
            mPassword = password;
            if(mIsGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
            } else if(mIsAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
            } else if(mIsServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            } else if(mIsNetworkError){
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
            }
        }
    }


    private static class AuthTokenCacheTd implements AuthTokenCache {

        String mAuthToken = "";

        @Override
        public void cacheAuthToken(String authToken) {
            mAuthToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return mAuthToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster {

        public Object mEvent;
        public int mInteractionsCount = 0;

        @Override
        public void postEvent(Object event) {
            mInteractionsCount++;
            mEvent = event;
        }
    }

}