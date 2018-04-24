package com.tseluikoartem.ening.contactsapp;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.tseluikoartem.ening.contactsapp.database.ContactDatabase;

/**
 * Created by ening on 21.04.18.
 */

public class ContactsApp extends Application {

    public static ContactsApp instance;

    private ContactDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, ContactDatabase.class, "database").build();
    }

    public static ContactsApp getInstance() {
        return instance;
    }

    public ContactDatabase getDatabase() {
        return database;
    }
}
