package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResult;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

public class FetchUserProfileUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UserProfileHttpEndpointSync mUserProfileHttpEndpointSync;
    private final UsersCache mUsersCache;

    public FetchUserProfileUseCaseSync(UserProfileHttpEndpointSync userProfileHttpEndpointSync,
                                       UsersCache usersCache) {
        mUserProfileHttpEndpointSync = userProfileHttpEndpointSync;
        mUsersCache = usersCache;
    }

    public UseCaseResult fetchUserProfileSync(String userId) {
        EndpointResult endpointResult;
        try {
            // the FIXED bug here is that userId is not passed to endpoint
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile(userId);
            // the FIXED bug here is that I don't check for successful result and it's also a duplication
            // of the call later in this method
            if (isSuccessfulEndpointResult(endpointResult)) {
                mUsersCache.cacheUser(
                        new User(userId, endpointResult.getFullName(), endpointResult.getImageUrl()));
            }
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        // the FIXED bug here is that I return wrong result in case of an unsuccessful server response
        return getUseCaseResult(endpointResult.getStatus());
    }

    private boolean isSuccessfulEndpointResult(EndpointResult endpointResult) {
        return endpointResult.getStatus() == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
    private UseCaseResult getUseCaseResult(UserProfileHttpEndpointSync.EndpointResultStatus endpointResultStatus) {
        if (endpointResultStatus == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS) return UseCaseResult.SUCCESS;
        else return UseCaseResult.FAILURE;
    }
}
