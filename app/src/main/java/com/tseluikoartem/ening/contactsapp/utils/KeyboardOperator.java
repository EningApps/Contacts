package com.tseluikoartem.ening.contactsapp.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ening on 07.05.18.
 */

public class KeyboardOperator {

    public void hideKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
