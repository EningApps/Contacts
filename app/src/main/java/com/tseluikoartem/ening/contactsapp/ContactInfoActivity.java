package com.tseluikoartem.ening.contactsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

public class ContactInfoActivity extends AppCompatActivity {


    private ImageView mContactPhoto;
    private ListView mContactInfoListView;
    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton editContactFab = (FloatingActionButton) findViewById(R.id.edit_contact_fab);
        editContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Intent intent = getIntent();
        mContact = intent.getParcelableExtra(ContactInfoActivity.class.getCanonicalName());
        mContactPhoto = findViewById(R.id.toolbar_contact_photo);
        UniversalImageLoader.setImage(mContact.getProfileImageURI(), mContactPhoto, null, "");
        addFieldsToLayout();


    }

    private void addFieldsToLayout(){

        final LinearLayout layout = findViewById(R.id.views_container_layout);
        final LayoutInflater ltInflater = getLayoutInflater();
        final View phoneView = ltInflater.inflate(R.layout.phone_cardview, layout, false);
        final TextView phoneTV = phoneView.findViewById(R.id.phone_text_view);
        final ImageView phoneCall = phoneView.findViewById(R.id.phone_call);
        final ImageView phoneMessage = phoneView.findViewById(R.id.phone_message);
        phoneTV.setText(mContact.getPhoneNumber());
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission(ApplicationConstants.PHONE_PERMISSIONS)){
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mContact.getPhoneNumber(), null));
                    startActivity(callIntent);
                }else{
                    verifyPermissions(ApplicationConstants.PHONE_PERMISSIONS);
                }
            }
        });

        phoneMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mContact.getPhoneNumber(), null));
                startActivity(smsIntent);
            }
        });


        final View emailView= ltInflater.inflate(R.layout.email_cardview, layout, false);
        final TextView emailTV = emailView.findViewById(R.id.email_text_view);
        final ImageView sendEmailIV = emailView.findViewById(R.id.send_email_image);
        emailTV.setText(mContact.getEmail());
        sendEmailIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + mContact.getEmail()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "new email");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "notes");
                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final View notesView= ltInflater.inflate(R.layout.notes_edit_text, layout, false);

        layout.addView(phoneView);
        layout.addView(emailView);
        layout.addView(notesView);

    }

    private void verifyPermissions(String[] permissions) { //TODO: нужно для contactAdaptera, убрать эту штуку в один класс + из AddContactActivity
        Log.d("TAG", "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(
                this,
                permissions,
                1
        );
    }

    private boolean checkPermission(String[] permission){
        Log.d("TAG", "checkPermission: checking permissions for:" + permission[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(
                this,
                permission[0]);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d("TAG", "checkPermission: \n Permissions was not granted for: " + permission[0]);
            return false;
        }else{
            return true;
        }
    }
}
