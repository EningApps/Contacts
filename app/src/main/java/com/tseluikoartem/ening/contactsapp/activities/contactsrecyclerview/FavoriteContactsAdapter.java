package com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.ContactsApp;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.activities.ContactIDetailsActivity;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.database.DatabaseOperator;
import com.tseluikoartem.ening.contactsapp.database.FavoriteContact;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ening on 14.04.18.
 */

public class FavoriteContactsAdapter extends Adapter {

    private List<FavoriteContact> mFavoriteContactsData;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View fCircleContact = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_fav_contact_layout, parent, false);
        return new ContactViewHolder.FavoriteContactHolder(fCircleContact);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CircleImageView contactPhoto = ((ContactViewHolder.FavoriteContactHolder) holder).getFavoriteContactPhoto();
        if(mFavoriteContactsData.get(position).getProfileImageURI()!=null && !mFavoriteContactsData.get(position).getProfileImageURI().equals("null"))
            UniversalImageLoader.setImage(mFavoriteContactsData.get(position).getProfileImageURI(), contactPhoto, null, "");

        contactPhoto.setOnLongClickListener(new FavContactLongClickListener(contactPhoto.getContext(),this,position));
        contactPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Contact contact = mFavoriteContactsData.get(position);
                final Intent intent = new Intent(contactPhoto.getContext(),ContactIDetailsActivity.class);
                intent.putExtra(ContactIDetailsActivity.class.getCanonicalName(),contact);
                contactPhoto.getContext().startActivity(intent);
            }
        });

        final TextView contactName = ((ContactViewHolder.FavoriteContactHolder) holder).getContactNameTV();
        contactName.setText(mFavoriteContactsData.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mFavoriteContactsData.size();
    }

    public void setData(List<FavoriteContact> mData) {

        this.mFavoriteContactsData = mData;

    }


    public void removeItem(Contact deletedItem, int deletedIndex) {
        boolean сontainsFavContact = false;
        int favContactPosition = deletedIndex;
        final List<FavoriteContact> favoriteContacts = ContactsApp.getInstance().getFavoriteContactList();
        for (int i = 0; i < favoriteContacts.size(); i++) {
            FavoriteContact contact = favoriteContacts.get(i);
            if(contact.getName().equals(deletedItem.getName()) && contact.getPhoneNumber().equals(deletedItem.getPhoneNumber())) {
                сontainsFavContact = true;
                favContactPosition = i;
                break;
            }
        }
        if(сontainsFavContact) {
            new DatabaseOperator().deleteFavoriteContact(mFavoriteContactsData.get(favContactPosition));
            mFavoriteContactsData.remove(favContactPosition);
            notifyItemRemoved(favContactPosition);
            notifyDataSetChanged();
        }
    }

    public void restoreItem(Contact deletedItem, int deletedIndex) {
        boolean сontainsFavContact = false;
        int favContactPosition = deletedIndex;
        final List<FavoriteContact> favoriteContacts = ContactsApp.getInstance().getFavoriteContactList();
        for (int i = 0; i < favoriteContacts.size(); i++) {
            FavoriteContact contact = favoriteContacts.get(i);
            if(contact.getName().equals(deletedItem.getName()) && contact.getPhoneNumber().equals(deletedItem.getPhoneNumber())) {
                сontainsFavContact = true;
                favContactPosition = i;
                break;
            }
        }
        if(сontainsFavContact) {
            mFavoriteContactsData.add(favContactPosition, new FavoriteContact(deletedItem));
            new DatabaseOperator().addFavoriteContact(new FavoriteContact(deletedItem));
            notifyItemInserted(favContactPosition);
            notifyDataSetChanged();
        }
    }


    public List<FavoriteContact> getData() {
        return mFavoriteContactsData;
    }
}
