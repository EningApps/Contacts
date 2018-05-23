package com.tseluikoartem.ening.contactsapp.contacts_server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendContactServerCodeActivity extends AppCompatActivity {


    private TextView contactIdTV;
    private TextView contactNameTV;
    private CircleImageView contactPhotoIV;
    private Contact mContact;
    private ProgressBar progressBar;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_contact_activity);

        mContact = getIntent().getParcelableExtra(Contact.class.getCanonicalName());

        contactNameTV = findViewById(R.id.send_contact_nameTV);
        contactIdTV = findViewById(R.id.contact_id_keyTV);
        contactPhotoIV = findViewById(R.id.send_contact_photoIV);
        progressBar = findViewById(R.id.progressBar2);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        contactNameTV.setText(mContact.getName());
        if(mContact.getProfileImageURI()!=null && !mContact.getProfileImageURI().equals(""))
            UniversalImageLoader.setImage(mContact.getProfileImageURI(),contactPhotoIV,null,"");


        if(mContact!=null) {
            final Retrofit retrofit = ((ContactsApp) getApplication()).getRetrofit();
            final ContactsServerApi contactsServerApi = retrofit.create(ContactsServerApi.class);
            contactsServerApi.sendContact(mContact).enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Error occured.",Toast.LENGTH_SHORT).show();
                    }else{
                        contactIdTV.setText(String.valueOf(response.body().getId()));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error occured: " + t.getMessage(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
