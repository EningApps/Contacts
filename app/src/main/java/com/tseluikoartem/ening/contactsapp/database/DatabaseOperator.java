package com.tseluikoartem.ening.contactsapp.database;

import com.tseluikoartem.ening.contactsapp.ContactsApp;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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


    public void updateContact(final Contact contact,final  String oldName, final String phone){
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
        Contact respectiveContact = null;
        Callable<Contact> callable = new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return contactsDAO.getByParametrs(oldName,phone);
            }
        };
        FutureTask<Contact> futureTask = new FutureTask<Contact>(callable);
        Executors.newFixedThreadPool(2).execute(futureTask);
        try {
            respectiveContact = futureTask.get(200L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        if (respectiveContact!=null)
            contact.setId(respectiveContact.getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int found = contactsDAO.update(contact);
            }
        }).start();
    }


    public void updateFavoriteContact(final FavoriteContact favoriteContact, final String oldName, final String phone){

        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final FavoriteContactsDAO favoriteContactsDAO = database.favoriteContactsDAO();
        Contact respectiveContact = null;
        Callable<FavoriteContact> callable = new Callable<FavoriteContact>() {
            @Override
            public FavoriteContact call() throws Exception {
                return favoriteContactsDAO.getByParams(oldName,phone);
            }
        };
        FutureTask<FavoriteContact> futureTask = new FutureTask<FavoriteContact>(callable);
        Executors.newFixedThreadPool(2).execute(futureTask);
        try {
            respectiveContact = futureTask.get(200L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        if (respectiveContact!=null)
            favoriteContact.setId(respectiveContact.getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int found = favoriteContactsDAO.update(favoriteContact);
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
