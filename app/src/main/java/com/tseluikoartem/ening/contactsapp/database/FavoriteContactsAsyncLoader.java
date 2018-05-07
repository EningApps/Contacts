package com.tseluikoartem.ening.contactsapp.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview.FavoriteContactsAdapter;

import java.util.List;

/**
 * Created by ening on 05.05.18.
 */

public class FavoriteContactsAsyncLoader extends AsyncTask<Void, Void, Void> {

    private RecyclerView favoriteContactRecyclerView;
    private List<FavoriteContact> favoriteContacts;
    private Context mContext;

    public FavoriteContactsAsyncLoader(Context context,RecyclerView recyclerView) {
        this.favoriteContactRecyclerView = recyclerView;
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final FavoriteContactsDAO favoriteContactsDAO = database.favoriteContactsDAO();
        favoriteContacts = favoriteContactsDAO.getAll();
        ContactsApp.getInstance().setFavoriteContactList(favoriteContacts);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        final FavoriteContactsAdapter adapter = new FavoriteContactsAdapter();
        adapter.setData(favoriteContacts);
        favoriteContactRecyclerView.setAdapter(adapter);
        favoriteContactRecyclerView.setLayoutManager(layoutManager);
    }
}
