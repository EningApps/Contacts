package com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.Contact;
import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.activities.ContactIDetailsActivity;
import com.tseluikoartem.ening.contactsapp.database.DatabaseOperator;
import com.tseluikoartem.ening.contactsapp.utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ening on 14.04.18.
 */

public class ContactsAdapter extends Adapter {

    private List<Contact> mData;

    private List<Contact> dataCopy ;

    private Activity callingActivity;//needed for search

    public ContactsAdapter(Activity callingActivity) {
        this.callingActivity = callingActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View fLinearContact = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_contact_layout, parent, false);
        return new ContactViewHolder.LinearHolder(fLinearContact);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CircleImageView contactPhoto = ((ContactViewHolder.LinearHolder) holder).getContactPhoto();
        if(mData.get(position).getProfileImageURI()!=null && !mData.get(position).getProfileImageURI().equals("null"))
            UniversalImageLoader.setImage(mData.get(position).getProfileImageURI(), contactPhoto, null, "");

        final TextView contactName = ((ContactViewHolder.LinearHolder) holder).getContactNameTV();
        contactName.setText(mData.get(position).getName());

        final View contactField = ((ContactViewHolder.LinearHolder) holder).getBackgroundField();
        contactField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Contact contact = mData.get(position);
                final Intent intent = new Intent(view.getContext(),ContactIDetailsActivity.class);
                intent.putExtra(ContactIDetailsActivity.class.getCanonicalName(),contact);

                callingActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Contact> mData) {

        this.mData = mData;
        dataCopy = new ArrayList<>();
        dataCopy.addAll(mData);
    }

    public void filter(String text) {
        mData.clear();
        if(text.isEmpty()){
            mData.addAll(dataCopy);
        } else{
            text = text.toLowerCase();
            for(Contact contact: dataCopy){
                if(contact.getName().toLowerCase().contains(text) || contact.getPhoneNumber().toLowerCase().contains(text)){
                    mData.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeItem(int adapterPosition) {
        new DatabaseOperator().deleteContact(mData.get(adapterPosition));
        mData.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    public void restoreItem(Contact deletedItem, int deletedIndex) {
        mData.add(deletedIndex, deletedItem);
        new DatabaseOperator().addContact(deletedItem);
        notifyItemInserted(deletedIndex);
    }

    public List<Contact> getmData() {
        return mData;
    }
}
