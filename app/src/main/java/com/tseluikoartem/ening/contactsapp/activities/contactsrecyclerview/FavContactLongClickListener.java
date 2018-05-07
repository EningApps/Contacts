package com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.database.DatabaseOperator;
import com.tseluikoartem.ening.contactsapp.database.FavoriteContact;

import java.util.List;



public class FavContactLongClickListener implements View.OnLongClickListener{
    private Context context;
    private FavoriteContact mContact;
    private FavoriteContactsAdapter mAdapter;
    private int position;

    public FavContactLongClickListener(Context context, FavoriteContactsAdapter mAdapter, int position) {
        this.context = context;
        this.mAdapter = mAdapter;
        this.mContact = mAdapter.getData().get(position);
        this.position = position;
    }

    @Override
    public boolean onLongClick(final View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        view.startAnimation(shake);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.contact_popmenu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.pop_menu_del:
                        final DatabaseOperator databaseOperator = new DatabaseOperator();
                        databaseOperator.deleteFavoriteContact(mContact);
                        mAdapter.getData().remove(position);
                        mAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        popupMenu.show();
        return true;
    }
}
