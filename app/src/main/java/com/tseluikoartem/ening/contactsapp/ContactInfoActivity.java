package com.tseluikoartem.ening.contactsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.tseluikoartem.ening.contactsapp.database.ContactDatabase;
import com.tseluikoartem.ening.contactsapp.database.ContactsDAO;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

public class ContactInfoActivity extends AppCompatActivity {


    private ImageView mContactPhoto;
    private Contact mContact;
    private EditText emailET;
    private EditText phoneET;
    private EditText nameET;
    private int mPreviousKeyStroke;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//TODO:add this to MY_CONTACT_ACTIVITY      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_contact_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView backButton = findViewById(R.id.ivBackArrowContact);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
                        final ContactsDAO contactsDAO = database.contactsDAO();
                        int found = contactsDAO.update(mContact);
                    }
                }).start();
                onBackPressed();
            }
        });

        final Intent intent = getIntent();
        mContact = intent.getParcelableExtra(ContactInfoActivity.class.getCanonicalName());
        mContactPhoto = findViewById(R.id.toolbar_contact_photo);
        if(mContact.getProfileImageURI()!=null && !mContact.getProfileImageURI().equals("null"))
            UniversalImageLoader.setImage(mContact.getProfileImageURI(), mContactPhoto, null, "");

        findFields();
        setListeners();
    }

    private void setListeners(){
        final FloatingActionButton editContactFab = (FloatingActionButton) findViewById(R.id.edit_contact_fab);
        editContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneET.setEnabled(true);
                nameET.setEnabled(true);
                if(emailET!=null) {
                    emailET.setEnabled(true);
                    emailET.setSelection(phoneET.getText().length());
                }
            }
        });

        if(emailET!=null) {
            emailET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mContact.setEmail(s.toString());
                }
            });
        }

        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mContact.setName(s.toString());
            }
        });

        phoneET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                mPreviousKeyStroke = keyCode;
                return false;
            }
        });
        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                if(number.length() == 3 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains("(")){
                    number = String.format("(%s", s.toString().substring(0,3));
                    phoneET.setText(number);
                    phoneET.setSelection(number.length());
                }
                else if(number.length() == 5 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains(")")){
                    number = String.format("(%s) %s",
                            s.toString().substring(1,4),
                            s.toString().substring(4,5));
                    phoneET.setText(number);
                    phoneET.setSelection(number.length());
                }
                else if(number.length() ==10 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains("-")){
                    number = String.format("(%s) %s-%s",
                            s.toString().substring(1,4),
                            s.toString().substring(6,9),
                            s.toString().substring(9,10));
                    phoneET.setText(number);
                    phoneET.setSelection(number.length());

                    mContact.setPhoneNumber(number);

                }
            }
        });


    }

    private void findFields(){

        final LinearLayout layout = findViewById(R.id.views_container_layout);
        final LayoutInflater ltInflater = getLayoutInflater();
        nameET = findViewById(R.id.contact_nameET);
        nameET.setText(mContact.getName());

        //adding phone field to layout
        if( mContact.getPhoneNumber()!=null&&!mContact.getPhoneNumber().equals("")) {
            final View phoneView = ltInflater.inflate(R.layout.phone_cardview, layout, false);
            phoneET = phoneView.findViewById(R.id.phone_edit_text);
            final ImageView phoneCall = phoneView.findViewById(R.id.phone_call);
            final ImageView phoneMessage = phoneView.findViewById(R.id.phone_message);
            phoneET.setText(mContact.getPhoneNumber());
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
            layout.addView(phoneView);
        }

        //email if needed
        if( mContact.getEmail()!=null&&!mContact.getEmail().equals("")) {
            final View emailView = ltInflater.inflate(R.layout.email_cardview, layout, false);
            emailET = emailView.findViewById(R.id.email_edit_text);
            final ImageView sendEmailIV = emailView.findViewById(R.id.send_email_image);
            emailET.setText(mContact.getEmail());
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
