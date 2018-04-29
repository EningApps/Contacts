package com.tseluikoartem.ening.contactsapp.database;

import com.tseluikoartem.ening.contactsapp.Contact;
import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.database.ContactDatabase;
import com.tseluikoartem.ening.contactsapp.database.ContactsDAO;

/**
 * Created by ening on 29.04.18.
 */

public class DatabaseOperator {

    public void addContact(final Contact contact){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDAO.insert(contact);
            }
        }).start();

    }

    public void updateContact(final Contact contact){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDAO.update(contact);
            }
        }).start();
    }

    public void deleteContact(final Contact contact){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDAO.delete(contact);
            }
        }).start();
    }


}
