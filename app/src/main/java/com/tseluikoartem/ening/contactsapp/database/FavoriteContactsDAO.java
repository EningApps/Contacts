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
@Dao()
public interface FavoriteContactsDAO {

    @Query("SELECT * FROM favorite_contacts")
    List<FavoriteContact> getAll();

    @Query("SELECT * FROM contacts WHERE id = :id")
    FavoriteContact getById(long id);

    @Insert
    void insert(FavoriteContact favContact);

    @Insert
    void insert(List<FavoriteContact> favContact);

    @Update
    int update(FavoriteContact favContact);

    @Delete
    void delete(FavoriteContact favContact);

    @Query("DELETE FROM favorite_contacts")
    void deleteAll();

}