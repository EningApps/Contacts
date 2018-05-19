package com.tseluikoartem.ening.contactsapp.contacts_server;


import com.tseluikoartem.ening.contactsapp.database.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContactsServerApi {

    @GET("api//contacts")
    Call<List<Contact>> getAllContacts();

    @GET("api//contacts/{id}")
    Call<Contact> getContact(@Path("id") int id);

    @POST("api//contacts")
    Call<Contact> sendContact(@Body Contact contact);

}
