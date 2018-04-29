package com.tseluikoartem.ening.contactsapp.utils;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tseluikoartem.ening.contactsapp.R;
import com.tseluikoartem.ening.contactsapp.utils.ApplicationConstants;

import java.io.File;


/**
 * Created by User on 6/13/2017.
 */

public class ChangePhotoDialog extends DialogFragment {

    private static final String TAG = "ChangePhotoDialog";

    public interface OnPhotoReceivedListener{
        public void recieveBitmapImage(Bitmap bitmap);
        public void recieveImagePath(String imagePath);
    }

    OnPhotoReceivedListener photoReceivedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);

        //initalize the textview for starting the camera
        TextView takePhoto = (TextView) view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: starting camera.");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ApplicationConstants.CAMERA_REQUEST_CODE);


            }
        });

        //Initialize the textview for choosing an image from memory
        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: accessing phones memory.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, ApplicationConstants.PICKFILE_REQUEST_CODE);
            }
        });

        // Cancel button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog.");
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoReceivedListener) {
            photoReceivedListener = (OnPhotoReceivedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhotoReceivedListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        Results when taking a new image with camera
         */
        if(requestCode == ApplicationConstants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a picture.");

            //get the new image bitmap
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Log.d(TAG, "onActivityResult: receieved bitmap: " + bitmap);

            //send the bitmap and fragment to the interface
            photoReceivedListener.recieveBitmapImage(bitmap);
            getDialog().dismiss();
        }

        /*
        Results when selecting new image from phone memory
         */
        if(requestCode == ApplicationConstants.PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            File file = new File(selectedImageUri.toString());
            Log.d(TAG, "onActivityResult: images: " + file.getPath());


            //send the bitmap and fragment to the interface
            photoReceivedListener.recieveImagePath(file.getPath());
            getDialog().dismiss();

        }
    }


}

















