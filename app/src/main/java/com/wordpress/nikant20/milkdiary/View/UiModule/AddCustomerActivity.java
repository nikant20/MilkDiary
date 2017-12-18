package com.wordpress.nikant20.milkdiary.View.UiModule;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.wordpress.nikant20.milkdiary.Model.User;
import com.wordpress.nikant20.milkdiary.R;
import com.wordpress.nikant20.milkdiary.View.LoginModule.Constants;
import com.wordpress.nikant20.milkdiary.View.LoginModule.SignupActivity;

import java.io.IOException;
import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class AddCustomerActivity extends Activity {
    CircleImageView imageCircularButtonProfile;
    EditText inputName,inputAddress,inputEmail,inputMobile;
    Button btnCreateAccount;
    PermissionListener permissionListener;

    public Uri downloadUrl;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,mDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    User user;
    FirebaseUser firebaseUser;

    private static final int GALLERY_INTENT = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        imageCircularButtonProfile = findViewById(R.id.imageCircularButtonProfile);
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputMobile = findViewById(R.id.inputMobile);
        inputAddress = findViewById(R.id.inputAddress);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    btnCreateAccount.setEnabled(true);
                    return;
                }
                signup();
            }
        });
        imageCircularButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        pickImage();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();

                    }
                };
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(permissionListener)
                        .setRationaleTitle("Allow Permissions")
                        .setRationaleMessage("Allow This app to access Gallery")
                        .setDeniedTitle("Permission denied")
                        .setDeniedMessage(
                                "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setGotoSettingButtonText("Allow")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });


    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageCircularButtonProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            StorageReference filepath = storageReference.child("Photos").child(uri.getLastPathSegment());


            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    btnCreateAccount.setEnabled(true);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Image uploading failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    public void signup() {

        btnCreateAccount.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddCustomerActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        final String name = inputName.getText().toString();
        String address = inputAddress.getText().toString();
        String email = inputEmail.getText().toString();
        final String mobile = inputMobile.getText().toString();
        String image = String.valueOf((downloadUrl));

        user = new User(name, email, address, mobile,image);
        databaseReference.child("MilkDiary").child("EndUsers").child(String.valueOf(firebaseUser.getUid())).push().setValue(user);
        progressDialog.dismiss();
        Intent intent = new Intent(AddCustomerActivity.this,MainActivity.class);
        startActivity(intent);
        finish();


    }

    public boolean validate() {
        boolean valid = true;

        String name = inputName.getText().toString();
        String address = inputAddress.getText().toString();
        String email = inputEmail.getText().toString();
        String mobile = inputMobile.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            inputName.setError("at least 3 characters");
            valid = false;
        } else {
            inputName.setError(null);
        }

        if (address.isEmpty()) {
            inputAddress.setError("Enter Valid Address");
            valid = false;
        } else {
            inputAddress.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            inputMobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            inputMobile.setError(null);
        }


        return valid;
    }


}