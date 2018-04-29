package com.tseluikoartem.ening.contactsapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tseluikoartem.ening.contactsapp.Contact;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

public class ContactIDetailsActivity extends AppCompatActivity {


    private ImageView mContactPhoto;
    private Contact mContact;
    private TextView emailTV;
    private TextView phoneTV;
    private TextView phoneTypeTV;
    private TextView nameTV;
    private TextView notesET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_info);

        final ImageView backButton = findViewById(R.id.ivBackArrowContact);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Intent intent = getIntent();
        mContact = intent.getParcelableExtra(ContactIDetailsActivity.class.getCanonicalName());
        mContactPhoto = findViewById(R.id.toolbar_contact_photo);
        if(mContact.getProfileImageURI()!=null && !mContact.getProfileImageURI().equals("null"))
            UniversalImageLoader.setImage(mContact.getProfileImageURI(), mContactPhoto, null, "");

        findFields();
        setListeners();

    }

    private void setListeners(){
        final FloatingActionButton editButton = findViewById(R.id.edit_contact_fab);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent editIntent = new Intent(getApplicationContext(),EditContactActivity.class);
                editIntent.putExtra(EditContactActivity.class.getCanonicalName(),mContact);
                startActivity(editIntent);
                overridePendingTransition(R.anim.diagonaltranslate,R.anim.alpha);
            }
        });


    }

    private void findFields(){

        final LinearLayout layout = findViewById(R.id.views_container_layout);
        final LayoutInflater ltInflater = getLayoutInflater();
        nameTV = findViewById(R.id.contact_nameTV);
        nameTV.setText(mContact.getName());

        //adding phone field to layout
        if( mContact.getPhoneNumber()!=null&&!mContact.getPhoneNumber().equals("")) {
            final View phoneView = ltInflater.inflate(R.layout.phone_cardview, layout, false);
            phoneTV = phoneView.findViewById(R.id.contact_phoneTV);
            final ImageView phoneCall = phoneView.findViewById(R.id.phone_call);
            final ImageView phoneMessage = phoneView.findViewById(R.id.phone_message);
            phoneTV.setText(mContact.getPhoneNumber());
            phoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPermission(ApplicationConstants.PHONE_PERMISSIONS)) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mContact.getPhoneNumber(), null));
                        startActivity(callIntent);
                    } else {
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
            phoneTypeTV = phoneView.findViewById(R.id.phoneTypeTV);
            phoneTypeTV.setText(mContact.getDevice());
            layout.addView(phoneView);
        }

        //email if needed
        if( mContact.getEmail()!=null&&!mContact.getEmail().equals("")) {
            final View emailView = ltInflater.inflate(R.layout.email_cardview, layout, false);
            emailTV = emailView.findViewById(R.id.contact_emailTV);
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
            layout.addView(emailView);
        }

        final View notesView= ltInflater.inflate(R.layout.notes_edit_text, layout, false);
        notesET = notesView.findViewById(R.id.notesET);
        notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesET.setEnabled(true);
                notesET.setFocusable(true);
            }
        });

        layout.addView(notesView);

    }

    private void verifyPermissions(String[] permissions) { //TODO: нужно для contactAdaptera, убрать эту штуку в один класс + из EditContactActivity
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
