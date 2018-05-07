package com.tseluikoartem.ening.contactsapp.utils;

import android.Manifest;
import android.app.ActivityOptions;
import android.util.Pair;

/**
 * Created by ening on 14.04.18.
 */

public interface ApplicationConstants {

    public static final int ADD_CONTACT_REQUEST_CODE = 635;
    public static final int ADD_CONTACT_IMAGE_CODE = 231;
    public static final int EDIT_CONTACT_INTENT_CODE = 232;


    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static final String[] PHONE_PERMISSIONS = {Manifest.permission.CALL_PHONE};

    public static final int CAMERA_REQUEST_CODE = 5;
    public static final int PICKFILE_REQUEST_CODE = 8352;


    public static final String ADD_NEW_FAV_CONTACT = "add_new_fav_contact";
    public static final String EDIT_CONTACT_MODE_KEY = "edit_contact_mode";



}
