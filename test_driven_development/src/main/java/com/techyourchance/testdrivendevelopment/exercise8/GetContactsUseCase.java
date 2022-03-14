package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class GetContactsUseCase {

    List<Listener> mListenerList = new ArrayList<>();
    GetContactsHttpEndpoint mGetContactsHttpEndpoint;
    
    public GetContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
        mGetContactsHttpEndpoint = getContactsHttpEndpoint;
    }

    public void getContactsAndNotify(String filterTerm) {
        mGetContactsHttpEndpoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contacts) {
                for (Listener listener : mListenerList) {
                    listener.onContactsFetched(convertContactSchemaToContact(contacts));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                for (Listener listener : mListenerList) {
                    listener.onContactsFetchedFailed();
                }
            }
        });
    }

    private List<Contact> convertContactSchemaToContact(List<ContactSchema> contactsSchema) {
        List<Contact> contactsList = new ArrayList<>();
        for (ContactSchema contact: contactsSchema) {
            contactsList.add(new Contact(contact.getId(), contact.getFullName(), contact.getImageUrl()));
        }
        return  contactsList;
    }

    public void registerListener(Listener listener) {
        mListenerList.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListenerList.remove(listener);
    }

    public interface Listener {
        void onContactsFetched(List<Contact> contacts);

        void onContactsFetchedFailed();
    }
}
