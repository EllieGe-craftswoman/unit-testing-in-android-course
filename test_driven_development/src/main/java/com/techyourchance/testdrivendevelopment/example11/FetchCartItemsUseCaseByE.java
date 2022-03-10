package com.techyourchance.testdrivendevelopment.example11;

import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.example11.networking.CartItemSchema;
import com.techyourchance.testdrivendevelopment.example11.networking.GetCartItemsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchCartItemsUseCaseByE {

    public interface Listener {
        void onCartItemsFetched(List<CartItem> capture);

        void onFetchCartItemsFailed();
    }

    private final List<Listener> mListeners = new ArrayList<>();
    private final GetCartItemsHttpEndpoint mGetCartItemsHttpEndpoint;

    public FetchCartItemsUseCaseByE(GetCartItemsHttpEndpoint mGetCartItemsHttpEndpoint) {
        this.mGetCartItemsHttpEndpoint = mGetCartItemsHttpEndpoint;
    }

    public void fetchCartItemsAndNotify(int limit) {
        mGetCartItemsHttpEndpoint.getCartItems(limit, new GetCartItemsHttpEndpoint.Callback() {

            @Override
            public void onGetCartItemsSucceeded(List<CartItemSchema> cartItems) {
                for (Listener listener : mListeners) {
                    listener.onCartItemsFetched(cartItemsFromSchemas(cartItems));
                }
            }

            @Override
            public void onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason failReason) {
                if (failReason == GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR
                        || failReason == GetCartItemsHttpEndpoint.FailReason.NETWORK_ERROR) {
                    for (Listener listener : mListeners) {
                        listener.onFetchCartItemsFailed();
                    }
                }

            }
        });
    }

    private List<CartItem> cartItemsFromSchemas(List<CartItemSchema> cartItems) {
        List<CartItem> items = new ArrayList<>();
        for (CartItemSchema schema : cartItems) {
            items.add(new CartItem(schema.getId(),
                    schema.getTitle(),
                    schema.getDescription(),
                    schema.getPrice()));
        }
        return items;
    }

    public void registerListener(Listener mListener) {
        mListeners.add(mListener);
    }

    public void unregisterListener(Listener mListener) {
        mListeners.remove(mListener);
    }
}
