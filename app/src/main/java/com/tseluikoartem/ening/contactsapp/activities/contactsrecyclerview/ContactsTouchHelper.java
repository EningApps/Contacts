package com.tseluikoartem.ening.contactsapp.activities.contactsrecyclerview;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tseluikoartem.ening.contactsapp.Contact;
import com.tseluikoartem.ening.contactsapp.R;

/**
 * Created by ening on 29.04.18.
 */

public class ContactsTouchHelper implements  ContactsCallback.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;

    public ContactsTouchHelper(RecyclerView recyclerView, ContactsAdapter adapter) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ContactViewHolder.LinearHolder) {
            // get the removed item name to display it in snack bar
            String name = adapter.getmData().get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Contact deletedItem = adapter.getmData().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(recyclerView, name + " удалён из контактов", Snackbar.LENGTH_LONG);
            snackbar.setAction("Вернуть", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(recyclerView.getResources().getColor(R.color.colorAccent));
            snackbar.show();
        }
    }

}
