package com.techyourchance.testdrivendevelopment.example11;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.techyourchance.testdrivendevelopment.example11.FetchCartItemsUseCaseByE;
import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.example11.networking.CartItemSchema;
import com.techyourchance.testdrivendevelopment.example11.networking.GetCartItemsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCaseByETest {

    // region constants
    public static final Integer LIMIT = 6;
    public static final int PRICE = 5;
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String ID = "id";
    // endregion constants

    // region helper fields
    @Mock GetCartItemsHttpEndpoint mGetCartItemsHttpEndpoint;
    @Mock FetchCartItemsUseCaseByE.Listener mListenerMock1;
    @Mock FetchCartItemsUseCaseByE.Listener mListenerMock2;

    @Captor ArgumentCaptor<List<CartItem>> mAcCartItems;
    // endregion helper fields

    FetchCartItemsUseCaseByE SUT;

    @Before
    public void setup() {
        SUT = new FetchCartItemsUseCaseByE(mGetCartItemsHttpEndpoint);
        success();
    }

    @Test
    public void fetchCartItems_correctLimitPassedToEndpoint() {
        //Arrange
        ArgumentCaptor<Integer> acInt = ArgumentCaptor.forClass(Integer.class);
        //Act
        SUT.fetchCartItemsAndNotify(LIMIT);
        //Assert
        verify(mGetCartItemsHttpEndpoint).getCartItems(acInt.capture(), any(GetCartItemsHttpEndpoint.Callback.class));
        assertEquals(LIMIT, acInt.getValue());
    }

    @Test
    public void fetchCartItems_success_observersNotifiedWithCorrectData() {
        //Arrange
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        //Assert
        verify(mListenerMock1).onCartItemsFetched(mAcCartItems.capture());
        verify(mListenerMock2).onCartItemsFetched(mAcCartItems.capture());
        List<List<CartItem>> captures = mAcCartItems.getAllValues();
        List<CartItem> capture1 = captures.get(0);
        List<CartItem> capture2 = captures.get(1);
        assertEquals(getCartItem(), capture1);
        assertEquals(getCartItem(), capture2);
    }

    @Test
    public void fetchCartItems_success_unsubscribeObserverNotNotified() {
        //Arrange
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        //Assert
        verify(mListenerMock1).onCartItemsFetched(mAcCartItems.capture());
        verifyNoMoreInteractions(mListenerMock2);
    }

    @Test
    public void fetchCartItems_generalError_observersNotifiedAboutFailure() {
        //Arrange
        generalError();
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        //Assert
        verify(mListenerMock1).onFetchCartItemsFailed();
        verify(mListenerMock2).onFetchCartItemsFailed();
    }

    @Test
    public void fetchCartItems_networkError_observersNotifiedAboutFailure() {
        //Arrange
        networkError();
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        //Assert
        verify(mListenerMock1).onFetchCartItemsFailed();
        verify(mListenerMock2).onFetchCartItemsFailed();
    }

    // region helper methods
    private void success() {
        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GetCartItemsHttpEndpoint.Callback callback =  (GetCartItemsHttpEndpoint.Callback) args[1];
                callback.onGetCartItemsSucceeded(getCartItemSchemes());
                return null;
            }
        }).when(mGetCartItemsHttpEndpoint).getCartItems(anyInt(), any(GetCartItemsHttpEndpoint.Callback.class));
    }

    private void generalError() {
        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GetCartItemsHttpEndpoint.Callback callback =  (GetCartItemsHttpEndpoint.Callback) args[1];
                callback.onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason.GENERAL_ERROR);
                return null;
            }
        }).when(mGetCartItemsHttpEndpoint).getCartItems(anyInt(), any(GetCartItemsHttpEndpoint.Callback.class));

    }

    private void networkError() {
        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                GetCartItemsHttpEndpoint.Callback callback =  (GetCartItemsHttpEndpoint.Callback) args[1];
                callback.onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason.NETWORK_ERROR);
                return null;
            }
        }).when(mGetCartItemsHttpEndpoint).getCartItems(anyInt(), any(GetCartItemsHttpEndpoint.Callback.class));

    }

    private List<CartItemSchema> getCartItemSchemes() {
        List<CartItemSchema> schemas = new ArrayList<>();
        schemas.add(new CartItemSchema(ID, TITLE, DESCRIPTION, PRICE));
        return schemas;
    }

    private List<CartItem>  getCartItem() {
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(ID, TITLE, DESCRIPTION, PRICE));
        return items;
    }
    // endregion helper methods

    // region helper helper classes

    // endregion helper helper classes
}
