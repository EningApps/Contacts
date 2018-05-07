package com.tseluikoartem.ening.contactsapp.utils;

import android.os.AsyncTask;

import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.database.ContactDatabase;
import com.tseluikoartem.ening.contactsapp.database.ContactsDAO;

import java.util.List;

/**
 * Created by ening on 21.04.18.
 */

public class DbDataAsyncLoader extends AsyncTask<Void,Void,List<Contact>> {

    private DataLoadingFragment dataLoadingFragment;

    public DbDataAsyncLoader(DataLoadingFragment dataLoadingFragment) {
        this.dataLoadingFragment = dataLoadingFragment;
    }

    @Override
    protected List<Contact> doInBackground(Void... voids) {

        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
       // contactsDAO.deleteAll();
        return contactsDAO.getAll();

    }

    @Override
    protected void onPostExecute(List<Contact> contacts) {
        dataLoadingFragment.setContactsData(contacts);
        super.onPostExecute(contacts);
    }


}
