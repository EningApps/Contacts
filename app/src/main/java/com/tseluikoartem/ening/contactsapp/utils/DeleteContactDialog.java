package com.tseluikoartem.ening.contactsapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.database.Contact;


/**
 * Created by User on 6/13/2017.
 */

public class DeleteContactDialog extends DialogFragment {

    private static final String TAG = "DeleteContactDialog";
    private static final String DELETED_INDEX_KEY = "DELETED_INDEX";
    private static final String DELETED_CONTACT_KEY = "DELETED_CONTACT";

    private int deletedIndex;
    private Contact deletedContact;

    public interface OnContactDeletedListener {
        public void deleteConfirmed(Contact contact, int deletedIndex);
        public void deleteCanceled();
    }

    public static DeleteContactDialog newInstance(Contact deletedContact,int deletedIndex) {
        DeleteContactDialog fragment = new DeleteContactDialog();
        Bundle args = new Bundle();
        args.putInt(DELETED_INDEX_KEY, deletedIndex);
        args.putParcelable(DELETED_CONTACT_KEY, deletedContact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deletedIndex = getArguments().getInt(DELETED_INDEX_KEY);
            deletedContact = getArguments().getParcelable(DELETED_CONTACT_KEY);
        }
    }

    OnContactDeletedListener contactDeletedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_contact, container, false);

        //initalize the textview for starting the camera
        TextView confirmButton = (TextView) view.findViewById(R.id.dialog_delete_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDeletedListener.deleteConfirmed(deletedContact,deletedIndex);
                getDialog().dismiss();
            }
        });


        // Cancel button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialog_delete_cancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDeletedListener.deleteCanceled();
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactDeletedListener) {
            contactDeletedListener = (OnContactDeletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactDeletedListener");
        }
    }


}

















