package com.tseluikoartem.ening.contactsapp.database;

import android.arch.persistence.room.Entity;

/**
 * Created by ening on 05.05.18.
 */
@Entity(tableName = "favorite_contacts") //this class needed to save favorite contacts to another db table
public class FavoriteContact extends Contact {

    public FavoriteContact() {
    }

    public FavoriteContact(Contact baseClass) {
        this.setName(baseClass.getName());
        this.setLastName(baseClass.getLastName());
        this.setProfileImageURI(baseClass.getProfileImageURI());
        this.setBirhday(baseClass.getBirhday());
        this.setPhoneNumber(baseClass.getPhoneNumber());
        this.setNotes(baseClass.getNotes());
        this.setCompany(baseClass.getCompany());
        this.setEmail(baseClass.getEmail());
        this.setDevice(baseClass.getDevice());
    }
}
