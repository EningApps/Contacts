package com.tseluikoartem.ening.contactsapp.contacts_server;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.activities.MainActivity;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.database.DatabaseOperator;
import com.tseluikoartem.ening.contactsapp.utils.KeyboardOperator;

import java.text.ParseException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetContactFromServerActivity extends AppCompatActivity {


    private Button getContactButton;
    private ProgressBar progressBar;
    private EditText contactIdEditText;
    private ImageView loadingFinishedIV;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_contact_from_server);

        getContactButton = findViewById(R.id.get_contact_button);
        progressBar = findViewById(R.id.progressBar);
        contactIdEditText = findViewById(R.id.contact_server_idED);
        loadingFinishedIV = findViewById(R.id.loading_finishedIV);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        contactIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>4){
                    try{
                        Integer.parseInt(s.toString());
                    }catch (NumberFormatException e){
                        contactIdEditText.setText("");
                        return;
                    }
                    new KeyboardOperator().hideKeyboard(getContactButton);
                    getContactButton.requestFocus();
                    getContactButton.setEnabled(true);
                    getContactButton.setTextColor(getResources().getColor(R.color.black));
                    getContactButton.setElevation(10);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final Retrofit retrofit = ((ContactsApp) getApplication()).getRetrofit();
                final ContactsServerApi contactsServerApi = retrofit.create(ContactsServerApi.class);
                final int id = Integer.parseInt(contactIdEditText.getText().toString());
                contactsServerApi.getContact(Integer.valueOf(contactIdEditText.getText().toString())).enqueue(new Callback<Contact>() {
                    @Override
                    public void onResponse(Call<Contact> call, Response<Contact> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        loadingFinishedIV.setVisibility(View.VISIBLE);
                        if(response.isSuccessful()) {
                            final Contact contact = response.body();
                            if(contact.getPhoneNumber()!=null) {
                                contact.setId(0);//needed to avoid collisions in database
                                new DatabaseOperator().addContact(contact);
                                Toast.makeText(getApplicationContext(), "'"+contact.getName()+"'"+" has been added.",Toast.LENGTH_SHORT).show();
                                loadingFinishedIV.setBackground(getResources().getDrawable(R.drawable.succsessful_icon));
                            }else{
                                loadingFinishedIV.setBackground(getResources().getDrawable(R.drawable.failure_icon));
                                Toast.makeText(getApplicationContext(), "Contact not found.",Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            loadingFinishedIV.setBackground(getResources().getDrawable(R.drawable.failure_icon));
                        }
                    }

                    @Override
                    public void onFailure(Call<Contact> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        loadingFinishedIV.setBackground(getResources().getDrawable(R.drawable.failure_icon));

                    }
                });


            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}
