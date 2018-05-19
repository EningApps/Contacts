package com.tseluikoartem.ening.contactsapp;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.tseluikoartem.ening.contactsapp.database.ContactDatabase;
import com.tseluikoartem.ening.contactsapp.database.FavoriteContact;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ening on 21.04.18.
 */

public class ContactsApp extends Application {

    public static ContactsApp instance;

    private List<FavoriteContact> favoriteContactList;

    private Retrofit retrofit;

    private ContactDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, ContactDatabase.class, "database").build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationConstants.ServerConstants.SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public static ContactsApp getInstance() {
        return instance;
    }

    public ContactDatabase getDatabase() {
        return database;
    }

    public List<FavoriteContact> getFavoriteContactList() {
        return favoriteContactList;
    }

    public void setFavoriteContactList(List<FavoriteContact> favoriteContactList) {
        this.favoriteContactList = favoriteContactList;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
