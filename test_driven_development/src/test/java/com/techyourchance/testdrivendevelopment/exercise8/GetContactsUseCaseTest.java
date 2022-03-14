package com.techyourchance.testdrivendevelopment.exercise8;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.*;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class GetContactsUseCaseTest {

    // region constants
    public static final String FILTER_TERM = "Soudi";
    public static final double AGE = 25.4;
    public static final String IMAGE_URL = "";
    public static final String PHONE_NUMBER = "+49 151 27966385";
    public static final String FULL_NAME = "Ellie Soudi";
    public static final String ID = "UID2992929";
    // endregion constants

    // region helper fields
    @Mock
    GetContactsHttpEndpoint mGetContactsHttpEndpoint;
    @Mock
    GetContactsUseCase.Listener mockListener1;
    @Mock
    GetContactsUseCase.Listener mockListener2;
    @Captor
    ArgumentCaptor<List<Contact>> acContacts;
    // endregion helper fields

    GetContactsUseCase SUT;

    @Before
    public void setup() throws Exception {
        SUT = new GetContactsUseCase(mGetContactsHttpEndpoint);
        success();
    }

    @Test
    public void getContactsSync_filterItemPassedCorrectly() {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        //Act
        SUT.getContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mGetContactsHttpEndpoint).getContacts(ac.capture(), any(GetContactsHttpEndpoint.Callback.class));
    }

    @Test
    public void getContactsSync_success_allObserversNotified() {
        //Arrange
        SUT.registerListener(mockListener1);
        SUT.registerListener(mockListener2);
        //Act
        SUT.getContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mockListener1).onContactsFetched(acContacts.capture());
        verify(mockListener2).onContactsFetched(acContacts.capture());
        List<List<Contact>> capturedLists = acContacts.getAllValues();
        List<Contact> list1 = capturedLists.get(0);
        List<Contact> list2 = capturedLists.get(1);
        assertEquals(getListOfContacts(), list1);
        assertEquals(getListOfContacts(), list2);
    }

    @Test
    public void getContactsSync_success_unregisteredListenersNotNotified() {
        //Arrange
        SUT.registerListener(mockListener1);
        SUT.registerListener(mockListener2);
        SUT.unregisterListener(mockListener2);
        //Act
        SUT.getContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mockListener1).onContactsFetched(acContacts.capture());
        verifyNoMoreInteractions(mockListener2);
    }

    @Test
    public void getContactsSync_generalError_registeredListenersNotifiedWithFailure() {
        //Arrange
        generalError();
        SUT.registerListener(mockListener1);
        SUT.registerListener(mockListener2);
        //Act
        SUT.getContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mockListener1).onContactsFetchedFailed();
        verify(mockListener2).onContactsFetchedFailed();
    }

    @Test
    public void getContactsSync_networkError_registeredListenersNotifiedWithFailure() {
        //Arrange
        networkError();
        SUT.registerListener(mockListener1);
        SUT.registerListener(mockListener2);
        //Act
        SUT.getContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mockListener1).onContactsFetchedFailed();
        verify(mockListener2).onContactsFetchedFailed();
    }

    // region helper methods
    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
                callback.onGetContactsSucceeded(getListOfContactsSchema());
                return null;
            }
        }).when(mGetContactsHttpEndpoint).getContacts(any(String.class), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void generalError() {doAnswer(new Answer() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
            callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
            return null;
        }
    }).when(mGetContactsHttpEndpoint).getContacts(any(String.class), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void networkError() {doAnswer(new Answer() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            GetContactsHttpEndpoint.Callback callback = (GetContactsHttpEndpoint.Callback) args[1];
            callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);
            return null;
        }
    }).when(mGetContactsHttpEndpoint).getContacts(any(String.class), any(GetContactsHttpEndpoint.Callback.class));
    }

    private List<ContactSchema> getListOfContactsSchema() {
        List<ContactSchema> contactSchemas = new ArrayList<>();
        contactSchemas.add(new ContactSchema(ID, FULL_NAME, PHONE_NUMBER, IMAGE_URL, AGE));
        return contactSchemas;
    }

    private List<Contact> getListOfContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contacts;
    }
    // endregion helper methods

}
