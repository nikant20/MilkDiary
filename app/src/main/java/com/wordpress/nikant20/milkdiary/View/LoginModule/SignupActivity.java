package com.wordpress.nikant20.milkdiary.View.LoginModule;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.wordpress.nikant20.milkdiary.View.UiModule.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _addressText;
    EditText _emailText;
    EditText _mobileText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    ImageButton imageButtonProfile;
    Button _signupButton;
    TextView _loginLink;
    RadioGroup radioGroup;
    RadioButton radioMilkman, radioCustomer;
    PermissionListener permissionListener;
    List<Image> images;


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,fileReference;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");




        _nameText = (EditText) findViewById(R.id.input_name);
        _addressText = (EditText) findViewById(R.id.input_address);
        _emailText = (EditText) findViewById(R.id.input_email);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        imageButtonProfile = findViewById(R.id.imageButtonProfile);
        radioGroup = findViewById(R.id.radioGroup);
        radioCustomer = findViewById(R.id.radioCustomer);
        radioMilkman = findViewById(R.id.radioMilkman);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    _signupButton.setEnabled(true);
                    return;
                }
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
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
        ImagePicker.create(SignupActivity.this)
                .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .single()
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select")
                .start(0); // image selection title
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            imageButtonProfile.setImageBitmap(BitmapFactory.decodeFile(images.get(0).getPath()));
        }
        else{
            imageButtonProfile.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(R.drawable.userxhdpi)));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void signup() {

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        int radiogroupid = radioGroup.getCheckedRadioButtonId();
        String image = String.valueOf(imageButtonProfile.getImageAlpha());
        String typeofUser;
        if (radiogroupid == R.id.radioMilkman) {
            typeofUser = "Milkman";
        } else {
            typeofUser = "Customer";
        }

        user = new User(name, email, address, mobile, typeofUser, image);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {


                    //Creating method to add extra data to user
                    registerUser(task.getResult().getUser());


                    //Starting new Intent
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Sign Up Failed.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    //Method to add Users to Firebase Db
    private void registerUser(FirebaseUser firebaseUser) {

        //EndUsers is the root name
        //user is the POJO model
        int radiogroupid = radioGroup.getCheckedRadioButtonId();
        if (radiogroupid == R.id.radioMilkman) {
            databaseReference.child("MilkDiary").child("Milkman").child(firebaseUser.getUid()).setValue(user);
        } else {
            databaseReference.child("MilkDiary").child("EndUsers").child(firebaseUser.getUid()).setValue(user);
        }


    }


    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}