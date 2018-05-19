package com.tseluikoartem.ening.contactsapp.activities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview.FavoriteContactsAdapter;
import com.tseluikoartem.ening.contactsapp.contacts_server.GetContactFromServerActivity;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview.ContactsAdapter;
import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.database.ContactDatabase;
import com.tseluikoartem.ening.contactsapp.database.ContactsDAO;
import com.tseluikoartem.ening.contactsapp.database.FavoriteContact;
import com.tseluikoartem.ening.contactsapp.database.FavoriteContactsAsyncLoader;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;
import com.tseluikoartem.ening.contactsapp.utils.DataLoadingFragment;
import com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview.ContactsCallback;
import com.tseluikoartem.ening.contactsapp.utils.DeleteContactDialog;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements DataLoadingFragment.OnDataLoadedListener,
                   DeleteContactDialog.OnContactDeletedListener,
                   ContactsCallback.RecyclerItemTouchHelperListener{


    private RecyclerView mContactsRecyclerView , mFavoriteContactsRecyclerView;
    private ContactsAdapter mAdapter;
    private List<Contact> mAdapterData;
    private List<FavoriteContact> favoriteContacts;
    private View getContactFromServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        getContactFromServer = findViewById(R.id.getting_contact_from_server);

        getContactFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GetContactFromServerActivity.class));
            }
        });

        favoriteContacts = new ArrayList<>();

        mFavoriteContactsRecyclerView = findViewById(R.id.favorite_contacts_recycler_view);
        final FavoriteContactsAsyncLoader favoriteContactsAsyncLoader = new FavoriteContactsAsyncLoader(this, mFavoriteContactsRecyclerView);
        favoriteContactsAsyncLoader.execute();



        initImageLoader();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(),EditContactActivity.class);
                startActivityForResult(intent, ApplicationConstants.ADD_CONTACT_REQUEST_CODE);
            }
        });

       // loadSystemContacts();


        mAdapterData = new ArrayList<>();
        mAdapter = new ContactsAdapter(this);
        mAdapter.setData(mAdapterData);
        mContactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContactsRecyclerView.setAdapter(mAdapter);

        mContactsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mContactsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ContactsCallback(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mContactsRecyclerView);

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
    protected void onResume() {
        updateDada();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return true;
            }
        });

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

    private void updateDada() {
        final DataLoadingFragment dataLoadingFragment = new DataLoadingFragment();
        dataLoadingFragment.show(getSupportFragmentManager(),"dataloading");
    }

    private void loadSystemContacts() {//TODO: check this method

        final List<Contact> systemContacts = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));


                String phoneNo=null;
                if (cursor.getInt(cursor.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNo = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();
                }

                String email=null;
                Cursor emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (emailCursor.moveToNext()) {
                    //to get the contact names
                    email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }


                final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
                String contactPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY).toString();


                final Contact newContact = new Contact();
                newContact.setName(name);
                newContact.setPhoneNumber(phoneNo);
                newContact.setEmail(email);
                newContact.setEmail(contactPhotoUri);
                systemContacts.add(newContact);
            }

        }
        if(cursor!=null){
            cursor.close();
        }

        final ContactDatabase database = ContactsApp.getInstance().getDatabase();
        final ContactsDAO contactsDAO = database.contactsDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactsDAO.insert(systemContacts);
            }
        }).start();

    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    @Override
    public void recieveContactsData(List<Contact> data) {
        mAdapterData = data;
        Collections.sort(mAdapterData, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return (o1.getName()+o1.getLastName()).compareTo((o2.getName()+o2.getLastName()));
            }
        });
        mAdapter.setData(mAdapterData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteConfirmed(final Contact deletedContact, final int deletedIndex) {

        ((FavoriteContactsAdapter) mFavoriteContactsRecyclerView.getAdapter()).removeItem(deletedContact, deletedIndex);
        mAdapter.removeItem(deletedIndex);
        Snackbar snackbar = Snackbar
                .make(mContactsRecyclerView, deletedContact.getName() + " удалён из контактов", Snackbar.LENGTH_LONG);
        snackbar.setAction("Вернуть", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // undo is selected, restore the deleted item
                mAdapter.restoreItem(deletedContact, deletedIndex);
                ((FavoriteContactsAdapter) mFavoriteContactsRecyclerView.getAdapter()).restoreItem(deletedContact,deletedIndex);
            }
        });
        snackbar.setActionTextColor(mContactsRecyclerView.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    @Override
    public void deleteCanceled() {
        updateDada(); // needed to hide swipe artifact

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        DeleteContactDialog dialog = DeleteContactDialog.newInstance(mAdapter.getmData().get(position),position);
        dialog.show(getSupportFragmentManager(),"delete_dialog");
    }


}
