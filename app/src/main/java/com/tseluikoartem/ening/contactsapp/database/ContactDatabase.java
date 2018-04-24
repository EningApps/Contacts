package com.tseluikoartem.ening.contactsapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tseluikoartem.ening.contactsapp.Contact;

/**
 * Created by ening on 21.04.18.
 */

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactsDAO contactsDAO();
}