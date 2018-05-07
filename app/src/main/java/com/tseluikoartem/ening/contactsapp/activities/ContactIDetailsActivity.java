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


import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.database.DatabaseOperator;
import com.tseluikoartem.ening.contactsapp.database.FavoriteContact;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import java.util.stream.Stream;

import static com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants.ADD_NEW_FAV_CONTACT;
import static com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants.EDIT_CONTACT_INTENT_CODE;

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
                editIntent.putExtra(ApplicationConstants.EDIT_CONTACT_MODE_KEY,true);
                startActivityForResult(editIntent, EDIT_CONTACT_INTENT_CODE);
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

        final View notesView = ltInflater.inflate(R.layout.notes_card_view, layout, false);
        final TextView dataTV = notesView.findViewById(R.id.birhday_dateTV);
        if(!mContact.getBirhday().equals(""))
            dataTV.setText(mContact.getBirhday());
        else{
            dataTV.setVisibility(View.INVISIBLE);
            final ImageView dataImageToHide = notesView.findViewById(R.id.data_icon_to_hide);
            dataImageToHide.setVisibility(View.INVISIBLE);
        }
        notesET = notesView.findViewById(R.id.notesET);
        notesET.setText(mContact.getNotes());
        notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesET.setEnabled(true);
                notesET.setFocusable(true);
            }
        });
        notesET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesET.setEnabled(true);
                notesET.setFocusable(true);
            }
        });

        layout.addView(notesView);



        final View contactActivitiesCardView = ltInflater.inflate(R.layout.phone_activities_cardview, layout, false);
        layout.addView(contactActivitiesCardView);

        final View sendMessView = contactActivitiesCardView.findViewById(R.id.send_messageTV);
        final View sendContactView = contactActivitiesCardView.findViewById(R.id.send_contactTV);
        final View addToFavView = contactActivitiesCardView.findViewById(R.id.add_to_favoriteTV);
        sendMessView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mContact.getPhoneNumber(), null));
                startActivity(smsIntent);
            }
        });
        sendContactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:");
                Intent sendContactIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendContactIntent.putExtra("sms_body", mContact.toString());
                startActivity(sendContactIntent);
            }
        });
        addToFavView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alreadyContains = false;
                for(FavoriteContact contact : ContactsApp.getInstance().getFavoriteContactList()){
                    if(contact.getName().equals(mContact.getName()) && contact.getPhoneNumber().equals(mContact.getPhoneNumber()))
                        alreadyContains = true;
                }
                if(!alreadyContains) {
                    final DatabaseOperator databaseOperator = new DatabaseOperator();
                    databaseOperator.addFavoriteContact(new FavoriteContact(mContact));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(ADD_NEW_FAV_CONTACT, mContact);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Этот контакт уже есть в избранном", Toast.LENGTH_SHORT).show();
                }
            }
        });


        final View bufView = ltInflater.inflate(R.layout.buf_view, layout, false);
        layout.addView(bufView);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ApplicationConstants.EDIT_CONTACT_INTENT_CODE && resultCode == RESULT_OK){
            final Contact editedContact = (Contact) data.getParcelableExtra(Contact.class.getCanonicalName());
            nameTV.setText(editedContact.getName());
            phoneTV.setText(editedContact.getPhoneNumber());
            if(emailTV!= null)
                emailTV.setText(editedContact.getEmail());
            phoneTypeTV.setText(editedContact.getDevice());

            mContact = editedContact;

        }
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
