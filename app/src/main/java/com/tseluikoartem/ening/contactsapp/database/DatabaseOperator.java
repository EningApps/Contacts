package com.tseluikoartem.ening.contactsapp.database;

import com.tseluikoartem.ening.contactsapp.ContactsApp;

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

    public void addFavoriteContact(final FavoriteContact favContact){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final FavoriteContactsDAO contactsDAO = database.favoriteContactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDAO.insert(favContact);
            }
        }).start();

    }

    public void updateContact(final Contact contact){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int found = contactsDAO.update(contact);
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

    public void deleteFavoriteContact(final FavoriteContact contact){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final FavoriteContactsDAO contactsDAO = database.favoriteContactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDAO.delete(contact);
            }
        }).start();
    }


}
