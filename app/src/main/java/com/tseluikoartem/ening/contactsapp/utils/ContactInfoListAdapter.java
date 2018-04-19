//package com.tseluikoartem.ening.contactsapp.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tseluikoartem.ening.contactsapp.AddContactActivity;
//import com.tseluikoartem.ening.contactsapp.ContactInfoActivity;
//import com.tseluikoartem.ening.contactsapp.R;
//
//import java.util.List;
//
//
//
//
//public class ContactInfoListAdapter extends ArrayAdapter<String> {
//
//    private static final String TAG = "ContactPropertyListAdap";
//
//    private LayoutInflater mInflater;
//    private List<String> mProperties;
//    private int layoutResource;
//    private Context mContext;
//
//    public ContactInfoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> properties) {
//        super(context, resource, properties);
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutResource = resource;
//        this.mContext = context;
//        this.mProperties = properties;
//    }
//
//    private static class ViewHolder{
//        TextView property;
//        ImageView rightIcon;
//        ImageView leftIcon;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        final ViewHolder holder;
//
//        if(convertView == null){
//            convertView = mInflater.inflate(layoutResource, parent, false);
//            holder = new ViewHolder();
//
//
//        }
//        else{
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        final String property = mProperties.get(position);
//        holder.property.setText(property);
//
//        //check if it's an email or a phone number
//         if(property.contains("@")){
//            //email
//            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_email", null, mContext.getPackageName()));
//            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick: opening email.");
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                    emailIntent.setType("plain/text");
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {property});
//                    mContext.startActivity(emailIntent);
//
//                    /* optional settings for sending emails
//                    String email = property;
//                    String subject = "subject";
//                    String body = "body...";
//
//                    String uriText = "mailto: + Uri.encode(email) + "?subject=" + Uri.encode(subject) +
//                    "&body=" + Uri.encode(body);
//                    Uri uri = Uri.parse(uriText);
//
//                    emailIntent.setData(uri);
//                    mContext.startActivitY(emailIntent);
//
//                     */
//                }
//            });
//
//        }
//        else if((property.length() != 0)){
//            //Phone call
//            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_phone", null, mContext.getPackageName()));
//            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(((ContactInfoActivity)mContext).checkPermission(ApplicationConstants.PHONE_PERMISSIONS)){
//                        Log.d(TAG, "onClick: initiating phone call...");
//                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", property, null));
//                        mContext.startActivity(callIntent);
//                    }else{
//                        ((ContactInfoActivity)mContext).verifyPermissions(ApplicationConstants.PHONE_PERMISSIONS);
//                    }
//
//
//                }
//            });
//
//            //setup the icon for sending text messages
//            holder.rightIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_message", null, mContext.getPackageName()));
//            holder.rightIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick: initiating text message....");
//
//                    //The number that we want to send SMS
//                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", property, null));
//                    mContext.startActivity(smsIntent);
//                }
//            });
//
//        }
//
//
//        //--------------------------------------------------------------------------------------
//
//        return convertView;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
