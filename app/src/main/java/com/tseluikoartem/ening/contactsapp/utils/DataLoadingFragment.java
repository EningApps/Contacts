package com.tseluikoartem.ening.contactsapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.tseluikoartem.ening.contactsapp.database.Contact;
import com.tseluikoartem.ening.contactsapp.R;

import java.util.List;


/**
 * Created by User on 6/13/2017.
 */

public class DataLoadingFragment extends DialogFragment {

    public interface OnDataLoadedListener {
        void recieveContactsData(List<Contact> data);
    }

    OnDataLoadedListener dataLoadedListener;
    private ProgressBar loadingProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_loading, container, false);

        loadingProgressBar = (ProgressBar) view.findViewById(R.id.loading_progress_bar);

        final DbDataAsyncLoader asyncLoader = new DbDataAsyncLoader(this);
        asyncLoader.execute();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnDataLoadedListener) {
            dataLoadedListener = (OnDataLoadedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDataLoadedListener");
        }
    }

    public void setContactsData(List<Contact> contactsData) {
        dataLoadedListener.recieveContactsData(contactsData);
        dismiss();
    }
}

















