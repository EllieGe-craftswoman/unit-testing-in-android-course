package com.techyourchance.testdrivendevelopment.example9;


import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;

public class AddToCartUseCaseSync {

    private final AddToCartHttpEndpointSync mAddToCartHttpEndpointSync;

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
        }
    public AddToCartUseCaseSync(AddToCartHttpEndpointSync mAddToCartHttpEndpointSync) {
        this.mAddToCartHttpEndpointSync = mAddToCartHttpEndpointSync;
    }

    public UseCaseResult addToCartSync(String offerId, int amount) {
        AddToCartHttpEndpointSync.EndpointResult result;

        try {
            result = mAddToCartHttpEndpointSync.addToCartSync(new CartItemScheme(offerId, amount));
        } catch (NetworkErrorException e) {
            return  UseCaseResult.NETWORK_ERROR;
        }

        switch (result) {
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            case AUTH_ERROR:
            case GENERAL_ERROR:
            default:
                return UseCaseResult.FAILURE;
        }
    }
}
