package com.tseluikoartem.ening.contactsapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

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
    private String notes;
    private String birhday;
    private String meetingPlace;
    private String vkUrl;
    private String facebookUrl;
    private String twitterUrl;
    private String githubUrl;


    @NonNull
    private String phoneNumber;
    private String device;
    private String email;
    private String profileImageURI;

    public Contact() {

    }

    public Contact(long id, String name, String lastName, String company, String notes, String birhday, String meetingPlace,
                   String vkUrl, String facebookUrl, String twitterUrl, String githubUrl, @NonNull String phoneNumber,
                   String device, String email, String profileImageURI) {

        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.company = company;
        this.notes = notes;
        this.birhday = birhday;
        this.meetingPlace = meetingPlace;
        this.vkUrl = vkUrl;
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.githubUrl = githubUrl;
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
        birhday = in.readString();
        meetingPlace = in.readString();
        vkUrl =  in.readString();
        facebookUrl = in.readString();
        twitterUrl = in.readString();
        githubUrl = in.readString();
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
        dest.writeString(birhday);
        dest.writeString(meetingPlace);
        dest.writeString(vkUrl);
        dest.writeString(facebookUrl);
        dest.writeString(twitterUrl);
        dest.writeString(githubUrl);
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBirhday() {
        return birhday;
    }

    public void setBirhday(String birhday) {
        this.birhday = birhday;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
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

    public String getVkUrl() {
        return vkUrl;
    }

    public void setVkUrl(String vkUrl) {
        this.vkUrl = vkUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", notes='" + notes + '\'' +
                ", birhday='" + birhday + '\'' +
                ", meetingPlace='" + meetingPlace + '\'' +
                ", vkUrl='" + vkUrl + '\'' +
                ", facebookUrl='" + facebookUrl + '\'' +
                ", twitterUrl='" + twitterUrl + '\'' +
                ", githubUrl='" + githubUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", device='" + device + '\'' +
                ", email='" + email + '\'' +
                ", profileImageURI='" + profileImageURI + '\'' +
                '}';
    }
}