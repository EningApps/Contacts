package com.tseluikoartem.ening.contactsapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ening on 21.04.18.
 */
@Dao
public interface ContactsDAO {

    @Query("SELECT * FROM contacts")
    List<Contact> getAll();

    @Query("SELECT * FROM contacts WHERE id = :id")
    Contact getById(long id);

    @Query("SELECT * FROM contacts WHERE name = :name AND phoneNumber = :phoneNumber")
    Contact getByParametrs(String name,String phoneNumber);

    @Insert
    void insert(Contact contact);

    @Insert
    void insert(List<Contact> contact);

    @Update
    int update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contacts")
    void deleteAll();

}