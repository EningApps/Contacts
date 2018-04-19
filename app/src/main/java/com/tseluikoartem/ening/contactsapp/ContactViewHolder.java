package com.tseluikoartem.ening.contactsapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ening on 14.04.18.
 */

public class ContactViewHolder {

    public static class LinearHolder extends RecyclerView.ViewHolder {

        private CircleImageView contactPhoto;
        private TextView contactNameTV;
        private View contactField;

        LinearHolder(final View view) {
            super(view);
            contactField = view.findViewById(R.id.iconLinearField);
            contactPhoto = view.findViewById(R.id.contact_photo);
            contactNameTV = view.findViewById(R.id.contact_name);
        }


        public CircleImageView getContactPhoto() {
            return contactPhoto;
        }

        public TextView getContactNameTV() {
            return contactNameTV;
        }


        public View getContactField() {
            return contactField;
        }

    }
}
