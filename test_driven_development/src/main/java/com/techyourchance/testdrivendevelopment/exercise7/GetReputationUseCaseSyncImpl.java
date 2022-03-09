package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

public class GetReputationUseCaseSyncImpl implements GetReputationUseCaseSync {

    GetReputationHttpEndpointSync mGetReputationHttpEndpointSync;

    public GetReputationUseCaseSyncImpl(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        mGetReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    @Override
    public UseCaseResult getReputation() {
        GetReputationHttpEndpointSync.EndpointResult result = mGetReputationHttpEndpointSync.getReputationSync();
        switch (result.getStatus()) {
            case SUCCESS:
                return new UseCaseResult(Status.SUCCESS, result.getReputation());
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return new UseCaseResult(Status.FAILURE, 0);
            default:
                throw new RuntimeException("Invalid Error");
        }
    }
}
