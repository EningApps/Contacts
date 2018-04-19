package com.tseluikoartem.ening.contactsapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ening on 14.04.18.
 */

public class ContactsAdapter extends Adapter {

    private LinkedList<Contact> mData;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View fLinearContact = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_contact_layout, parent, false);
        return new ContactViewHolder.LinearHolder(fLinearContact);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final View contactField = ((ContactViewHolder.LinearHolder) holder).getContactField();
        contactField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Contact contact = mData.get(position);
                final Intent intent = new Intent(view.getContext(),ContactInfoActivity.class);
                intent.putExtra(ContactInfoActivity.class.getCanonicalName(),contact);
                view.getContext().startActivity(intent);
            }
        });
        final CircleImageView contactPhoto = ((ContactViewHolder.LinearHolder) holder).getContactPhoto();
        UniversalImageLoader.setImage(mData.get(position).getProfileImageURI(), contactPhoto, null, "");
        final TextView contactName = ((ContactViewHolder.LinearHolder) holder).getContactNameTV();
        contactName.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(LinkedList<Contact> mData) {
        this.mData = mData;
    }
}
