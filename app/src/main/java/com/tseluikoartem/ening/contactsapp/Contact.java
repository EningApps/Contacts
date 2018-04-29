package com.tseluikoartem.ening.contactsapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by User on 6/12/2017.
 */
@Entity(tableName = "contacts")
public class Contact implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    public long id;//needed fo database

    private String name;
    private String lastName;
    private String company;

    @NonNull
    private String phoneNumber;

    private String device;
    private String email;
    private String profileImageURI;

    public Contact() {

    }

    public Contact(long id, String name, String lastName, String company, @NonNull String phoneNumber, String device, String email, String profileImageURI) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.company = company;
        this.phoneNumber = phoneNumber;
        this.device = device;
        this.email = email;
        this.profileImageURI = profileImageURI;
    }

    protected Contact(Parcel in) {
        id = in.readLong();
        name = in.readString();
        lastName = in.readString();
        company = in.readString();
        phoneNumber = in.readString();
        device = in.readString();
        email = in.readString();
        profileImageURI = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(lastName);
        dest.writeString(company);
        dest.writeString(phoneNumber);
        dest.writeString(device);
        dest.writeString(email);
        dest.writeString(profileImageURI);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageURI() {
        return profileImageURI;
    }

    public long getId() {
        return id;
    }


    public void setProfileImageURI(String profileImageURI) {
        this.profileImageURI = profileImageURI;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", device='" + device + '\'' +
                ", email='" + email + '\'' +
                ", profileImageURI='" + profileImageURI + '\'' +
                '}';
    }
}