package com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ening on 14.04.18.
 */

public class ContactViewHolder {

    public static class LinearHolder extends RecyclerView.ViewHolder {

        private CircleImageView contactPhoto;
        private TextView contactNameTV;
        private View backgroundField, foregroundField;;

        LinearHolder(final View view) {
            super(view);
            foregroundField = view.findViewById(R.id.iconLinearField);
            backgroundField = view.findViewById(R.id.view_background);
            contactPhoto = view.findViewById(R.id.contact_photo);
            contactNameTV = view.findViewById(R.id.contact_name);
        }


        public View getForegroundField() {
            return foregroundField;
        }

        public CircleImageView getContactPhoto() {
            return contactPhoto;
        }

        public TextView getContactNameTV() {
            return contactNameTV;
        }


        public View getBackgroundField() {
            return backgroundField;
        }

    }



    public static class FavoriteContactHolder extends RecyclerView.ViewHolder {

        private CircleImageView favoriteContactPhoto;
        private TextView contactNameTV;

        FavoriteContactHolder(final View view) {
            super(view);
            favoriteContactPhoto = view.findViewById(R.id.fav_contact_photo);
            contactNameTV = view.findViewById(R.id.fav_contact_name);
        }

        public CircleImageView getFavoriteContactPhoto() {
            return favoriteContactPhoto;
        }

        public TextView getContactNameTV() {
            return contactNameTV;
        }
    }

}
