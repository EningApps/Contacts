package com.tseluikoartem.ening.contactsapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/12/2017.
 */

public class Contact implements Parcelable{

    private String name;
    private String phoneNumber;
    private String device;
    private String email;
    private String profileImageURI;

    public Contact() {

    }

    public Contact(String name, String phoneNumber, String device, String email, String profileImageURI) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.device = device;
        this.email = email;
        this.profileImageURI = profileImageURI;
    }


    protected Contact(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        device = in.readString();
        email = in.readString();
        profileImageURI = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
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

    public void setProfileImageURI(String profileImageURI) {
        this.profileImageURI = profileImageURI;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", device='" + device + '\'' +
                ", email='" + email + '\'' +
                ", profileImageURI='" + profileImageURI + '\'' +
                '}';
    }
}
