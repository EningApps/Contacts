package com.tseluikoartem.ening.contactsapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.database.FavoriteContact;
import com.tseluikoartem.ening.contactsapp.utils.ChangePhotoDialog;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.database.DatabaseOperator;
import com.tseluikoartem.ening.contactsapp.utils.ImageHelper;
import com.tseluikoartem.ening.contactsapp.utils.KeyboardOperator;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditContactActivity extends AppCompatActivity implements ChangePhotoDialog.OnPhotoReceivedListener {

    private Contact mContact;

    private EditText nameET;
    private EditText lastNameET;
    private EditText companyET;
    private EditText phoneET;
    private EditText emailET;
    private EditText birhdayET;
    private EditText mapET;
    private EditText vkUrlET;
    private EditText facebookUrlET;
    private EditText twitterUrlET;
    private EditText githubUrlET;
    private EditText meetingPlaceET;

    private Spinner phoneType;
    private CircleImageView contactPhoto;
    private TextView saveEditsButton;
    private DatePicker mBirhdayDatePicker;
    private ImageView pickBirthdayIV;


    private String contactPhotoUri;
    private String oldName;
    private String oldPhone;
    private boolean editMode;
    private int mPreviousKeyStroke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        final Intent intent = getIntent();
        if(intent!=null) {
            mContact = intent.getParcelableExtra(EditContactActivity.class.getCanonicalName());
            editMode = intent.getBooleanExtra(ApplicationConstants.EDIT_CONTACT_MODE_KEY,false);
            if(mContact!=null) {
                oldName = mContact.getName();
                oldPhone = mContact.getPhoneNumber();
            }
        }
        findViews();
        setViewsTexts();
        setListeners();

    }


    void findViews(){
        contactPhoto = findViewById(R.id.edit_contact_photoIV);
        if(mContact!=null )
            UniversalImageLoader.setImage(mContact.getProfileImageURI(),contactPhoto,null,"");
        saveEditsButton = findViewById(R.id.save_edits_button);
        if(editMode){
            saveEditsButton.setVisibility(View.VISIBLE);
        }
        nameET = findViewById(R.id.edit_nameET);
        lastNameET = findViewById(R.id.edit_last_nameET);
        companyET = findViewById(R.id.edit_companyET);
        phoneET = findViewById(R.id.edit_phoneET);
        emailET = findViewById(R.id.edit_emailET);
        phoneType = findViewById(R.id.select_device_spinner);
        saveEditsButton = (TextView) findViewById(R.id.save_edits_button);
        pickBirthdayIV = findViewById(R.id.pick_bithdayIV);
        birhdayET = findViewById(R.id.birhdayET);
        mapET = findViewById(R.id.meeting_placeET);
        vkUrlET = findViewById(R.id.vk_linkET);
        facebookUrlET = findViewById(R.id.facebook_linkET);
        twitterUrlET = findViewById(R.id.twitter_linkET);
        githubUrlET = findViewById(R.id.github_linkET);
        meetingPlaceET = findViewById(R.id.meeting_placeET);

    }


    void setViewsTexts(){
        if(mContact !=null){
            if(mContact.getName()!=null)
                nameET.setText(mContact.getName());
            if(mContact.getLastName()!=null)
                lastNameET.setText(mContact.getLastName());
            if(mContact.getCompany()!=null)
                companyET.setText(mContact.getCompany());
            if(mContact.getEmail()!=null)
                emailET.setText(mContact.getEmail());
            if(mContact.getPhoneNumber()!=null)
                phoneET.setText(mContact.getPhoneNumber());
            if(mContact.getBirhday()!=null)
                birhdayET.setText(mContact.getBirhday());
            if(mContact.getVkUrl()!=null)
                vkUrlET.setText(mContact.getVkUrl());
            if(mContact.getFacebookUrl()!=null)
                facebookUrlET.setText(mContact.getFacebookUrl());
            if(mContact.getTwitterUrl()!=null)
                twitterUrlET.setText(mContact.getTwitterUrl());
            if(mContact.getGithubUrl()!=null)
                githubUrlET.setText(mContact.getGithubUrl());
        }
    }

    void setListeners(){

        contactPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < ApplicationConstants.PERMISSIONS.length; i++){
                    String[] permission = {ApplicationConstants.PERMISSIONS[i]};
                    if(checkPermission(permission)){
                        if(i == ApplicationConstants.PERMISSIONS.length - 1){
                            Log.d("TAG", "onClick: opening the 'image selection dialog box'.");
                            ChangePhotoDialog dialog = new ChangePhotoDialog();
                            dialog.show(getSupportFragmentManager(),"dcd");
                        }
                    }else{
                        verifyPermissions(ApplicationConstants.PERMISSIONS);
                    }
                }
            }
        });

        saveEditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdits();
            }
        });


        birhdayET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBirthdayIV.performClick();// i shouldn't do like this, yes?
            }
        });
        pickBirthdayIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBirthdayIV.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.expand_icon_rotated, null));
                if(mBirhdayDatePicker==null) {
                    final FrameLayout frameLayout = findViewById(R.id.birtday_picker_container);
                    final LayoutInflater inflater = getLayoutInflater();
                    final View inflatedView = inflater.inflate(R.layout.birthday_picker, frameLayout, false);
                    mBirhdayDatePicker = inflatedView.findViewById(R.id.birhday_date_picker);

                    Calendar today = Calendar.getInstance();
                    mBirhdayDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH),
                                            new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            birhdayET.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            birhdayET.setText(String.valueOf(dayOfMonth)+"/"+(String.valueOf(monthOfYear)+"/"+(String.valueOf(year))));
                        }
                    });

                    frameLayout.addView(inflatedView);
                    inflatedView.requestFocus();
                    new KeyboardOperator().hideKeyboard(pickBirthdayIV);
                }else{
                    mBirhdayDatePicker = null;
                    final FrameLayout frameLayout = findViewById(R.id.birtday_picker_container);
                    frameLayout.removeAllViews();
                    pickBirthdayIV.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.expand_date_icon, null));

                }
            }
        });





        findViewById(R.id.edit_contact_linear_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KeyboardOperator().hideKeyboard(v);
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//overwritten method
            }
        });


        setPhoneETListeners();
    }


    private void verifyPermissions(String[] permissions) {
        Log.d("TAG", "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(
                this,
                permissions,
                1
        );
    }

    private boolean checkPermission(String[] permission){

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


    private void setPhoneETListeners(){

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

                saveEditsButton.setVisibility(number.length()>0?View.VISIBLE:View.INVISIBLE);

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

                }
            }
        });
    }

    private void saveEdits(){
        final Contact newContact = getThisContact();
        Intent intent;
        if(editMode){
            intent = new Intent(getApplicationContext(), ContactIDetailsActivity.class);
        }else {
            intent = new Intent(getApplicationContext(), MainActivity.class);

        }
        intent.putExtra(Contact.class.getCanonicalName(), newContact);
        if(editMode == true){
            new DatabaseOperator().updateContact(newContact,oldName,oldPhone);
            new DatabaseOperator().updateFavoriteContact(new FavoriteContact(newContact),oldName,oldPhone);
        }
        else{

            new DatabaseOperator().addContact(newContact);
            final CheckBox checkBox = findViewById(R.id.check_box_add_calendar_event);
            if(birhdayET.getText().toString()!="" && checkBox.isChecked()){
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(mBirhdayDatePicker.getYear(), mBirhdayDatePicker.getMonth(), mBirhdayDatePicker.getDayOfMonth(), 10, 30);
                Calendar endTime = Calendar.getInstance();
                endTime.set(mBirhdayDatePicker.getYear(), mBirhdayDatePicker.getMonth(), mBirhdayDatePicker.getDayOfMonth(), 10, 30);
                Intent calendarIntent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, "Yoga")
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
                startActivity(calendarIntent);
            }
        }

        startActivity(intent);
    }

    @Override
    public void recieveBitmapImage(Bitmap bitmap) {
        if(bitmap != null) {
            //compress the image (if you like)
            ImageHelper.compressBitmap(bitmap, 70);
            Uri imageUri = saveImageToGallery(bitmap);
            contactPhotoUri = imageUri.toString();
            UniversalImageLoader.setImage(contactPhotoUri, contactPhoto, null, "");
        }
    }

    @Override
    public void recieveImagePath(String imagePath) {
        if( !imagePath.equals("")){
            imagePath = imagePath.replace(":/", "://");
            contactPhotoUri  = imagePath;
            UniversalImageLoader.setImage(imagePath, contactPhoto, null, "");
        }
    }


    private Uri saveImageToGallery(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/contacts_app");
        myDir.mkdirs();
        Random generator = new Random();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "ContactImage-"+ timeStamp +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            // sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //     Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        Uri uri = getImageContentUri(file.getAbsoluteFile());
        return uri;

    }


    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    private Contact getThisContact(){
        if(mContact == null) {
            mContact = new Contact();
        }
        mContact.setName(nameET.getText().toString().equals("")? "Unknown" :nameET.getText().toString());
        mContact.setLastName(lastNameET.getText().toString());
        mContact.setCompany(companyET.getText().toString());
        mContact.setPhoneNumber(phoneET.getText().toString());
        mContact.setEmail(emailET.getText().toString());
        mContact.setDevice( ((Spinner)findViewById(R.id.select_device_spinner)).getSelectedItem().toString());
        if(contactPhotoUri!=null)
          mContact.setProfileImageURI(contactPhotoUri);
        mContact.setLastName(lastNameET.getText().toString());
        mContact.setCompany(companyET.getText().toString());
        mContact.setBirhday(birhdayET.getText().toString());
        mContact.setMeetingPlace(mapET.getText().toString());
        mContact.setVkUrl(vkUrlET.getText().toString());
        mContact.setFacebookUrl(facebookUrlET.getText().toString());
        mContact.setTwitterUrl(twitterUrlET.getText().toString());
        mContact.setGithubUrl(githubUrlET.getText().toString());
        mContact.setMeetingPlace(mapET.getText().toString());
        return mContact;
    }

}