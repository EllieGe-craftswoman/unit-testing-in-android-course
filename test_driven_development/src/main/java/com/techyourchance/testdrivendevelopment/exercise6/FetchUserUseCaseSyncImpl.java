package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.jetbrains.annotations.Nullable;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync{

    FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    UsersCache mUsersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        mFetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        mUsersCache = usersCache;
    }

    public UseCaseResult fetchUserSync(String userId) {
        User user = mUsersCache.getUser(userId);
        if(user == null){
            FetchUserHttpEndpointSync.EndpointResult result;
            try{
                result = mFetchUserHttpEndpointSync.fetchUserSync(userId);
            } catch (Exception exception){
                return new UseCaseResult(Status.NETWORK_ERROR, null);
            }
            switch (result.getStatus()){
                case SUCCESS: {
                    user = new User(result.getUserId(), result.getUsername());
                    mUsersCache.cacheUser(user);
                    return new UseCaseResult(Status.SUCCESS, user);
                }
                case AUTH_ERROR:
                case GENERAL_ERROR:
                default: return new UseCaseResult(Status.FAILURE, null);
            }
        } else {
            return new UseCaseResult(Status.SUCCESS, user);
        }

    }
}
