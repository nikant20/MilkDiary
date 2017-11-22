package com.wordpress.nikant20.milkdiary.Model;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wordpress.nikant20.milkdiary.R;

/**
 * Created by nikant20 on 11/21/2017.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView textViewName;
    private final TextView textViewEmailId;
    private final TextView textViewPhoneNumber;
    private final TextView textViewAddress;


    public UserViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        textViewName  = itemView.findViewById(R.id.textViewName);
        textViewEmailId = itemView.findViewById(R.id.textViewEmailId);
        textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
        textViewAddress = itemView.findViewById(R.id.textViewAddress);
    }

    public void setName(String name){
        textViewName.setText(name);
    }
    public void setEmailId(String emailid){
        textViewEmailId.setText(emailid);
    }
    public void setPhoneNumber(String phonenumber){
        textViewPhoneNumber.setText(phonenumber);
    }
    public void setAddress(String address){
        textViewAddress.setText(address);
    }
    public void setImage(String i, Context context) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(i);
        if (storageReference != null) {
            Glide.with(context).load(storageReference).into(imageView);
        }
        else {

            Glide.with(context).load(R.drawable.user).into(imageView);

            /* Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(imageView);*/

        }
    }

}
