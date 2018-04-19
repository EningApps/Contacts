package com.tseluikoartem.ening.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;
    private LinkedList<Contact> mAdapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initImageLoader();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivityForResult(new Intent(getApplicationContext(),AddContactActivity.class), ApplicationConstants.ADD_CONTACT_REQUEST_CODE);
            }
        });


        mAdapterData = new LinkedList<>();

        final Contact temp = new Contact();
        temp.setPhoneNumber("4234234");
        temp.setName("Artem");
        temp.setEmail("ensdc@cfd");
        mAdapterData.add(temp);

        mAdapter = new ContactsAdapter();
        mAdapter.setData(mAdapterData);
        mRecyclerView = findViewById(R.id.contacts_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ApplicationConstants.ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK){
            final Contact newContact = (Contact) data.getParcelableExtra(Contact.class.getCanonicalName());
            mAdapterData.add(newContact);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
}
