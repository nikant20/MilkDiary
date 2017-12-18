package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.wordpress.nikant20.milkdiary.Model.CostModel;
import com.wordpress.nikant20.milkdiary.Model.User;
import com.wordpress.nikant20.milkdiary.R;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LoginActivity;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LogoutActivity;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, mDatabaseReference;
    ProgressDialog progressDialog;
    LogoutActivity logoutActivity;
    User user;
    CostModel costModel;
    EditText editTextMilkInLitres;
    EditText editTextRate;
    TextView textViewDate;
    List<CostModel> costModelList;
    FirebaseUser firebaseUser;
    int currentPosition;
    String adapterKey;
    Float total;
    List<String> adapterKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutActivity = new LogoutActivity();
        user = new User();
        costModel = new CostModel();
        costModelList = new ArrayList<>();
        adapterKeyList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        editTextMilkInLitres = findViewById(R.id.editTextMilkInLitres);
        editTextRate = findViewById(R.id.editTextRate);
        textViewDate = findViewById(R.id.textViewDate);


        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.fabAddCustomer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler();
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getReference().child("MilkDiary").child("EndUsers").child(firebaseUser.getUid());
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                adapterKey = dataSnapshot.getKey();
                adapterKeyList.add(adapterKey);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        final FirebaseRecyclerAdapter<User, UserViewHolder> adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class, R.layout.customer_list, UserViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder holder, User model, int position) {
                holder.setName(model.getName());
                holder.setEmail(model.getEmail());
                holder.setPhone(model.getPhone());
                holder.setAddress(model.getAddress());
                holder.setImage(getApplicationContext(), model.getImage());
            }

            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final UserViewHolder userViewHolder = super.onCreateViewHolder(parent, viewType);
                userViewHolder.setOnClickListener(new UserViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Float milkinLitres, Float rate, String date, EditText editTextMilkInLitres, EditText editTextRate, TextView textViewDate) {
                        milkinLitres = Float.valueOf(editTextMilkInLitres.getText().toString());
                        rate = Float.valueOf(editTextRate.getText().toString());
                        date = textViewDate.getText().toString();
                        total = milkinLitres*rate;
                        currentPosition = position;
                        costModel = new CostModel(milkinLitres, rate, date,total);
                        costModelList.add(costModel);


                        String key = adapterKeyList.get(currentPosition);
                        mDatabaseReference.child("MilkDiary").child("Diary").child(firebaseUser.getUid()).child(key).push().setValue(costModel);


                    }

                    @Override
                    public void onItemClick(View view,int position) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String key = adapterKeyList.get(position);
                        Intent intent = new Intent(MainActivity.this,ShowTransaction.class);
                        intent.putExtra("key",key);
                        startActivity(intent);
                    }

                });
                return userViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        EditText editTextMilkInLitres;
        EditText editTextRate;
        TextView textViewDate;
        TextView textViewName;
        TextView textViewSubmit;
        String date;
        Float milkInLitres;
        Float rate;

        View mView;

        UserViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position, Float milkinLitres, Float rate, String date, EditText editTextMilkInLitres, EditText editTextRate, TextView textViewDate);
            public void onItemClick(View view, int position);
        }

        public void setOnClickListener(UserViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }

        public UserViewHolder(View itemView) {
            super(itemView);
            editTextMilkInLitres = itemView.findViewById(R.id.editTextMilkInLitres);
            editTextRate = itemView.findViewById(R.id.editTextRate);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewSubmit = itemView.findViewById(R.id.textViewSubmit);
            textViewName = itemView.findViewById(R.id.textViewName);

            textViewDate.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));


            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v,getAdapterPosition());
                }
            });
            textViewSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition(), milkInLitres, rate, date, editTextMilkInLitres, editTextRate, textViewDate);
                }
            });

            mView = itemView;

        }

        public void setName(String name) {
            TextView post_name = mView.findViewById(R.id.textViewName);
            post_name.setText(name);
        }

        public void setEmail(String email) {
            TextView post_email = mView.findViewById(R.id.textViewEmailId);
            post_email.setText(email);
        }

        public void setPhone(String phone) {
            TextView post_phone = mView.findViewById(R.id.textViewPhoneNumber);
            post_phone.setText(phone);
        }

        public void setAddress(String address) {
            TextView post_address = mView.findViewById(R.id.textViewAddress);
            post_address.setText(address);
        }

        public void setImage(Context context, String image) {
            CircleImageView post_image = mView.findViewById(R.id.imageView);
            Picasso.with(context).load(image).fit().centerCrop().placeholder(R.drawable.userxhdpi).error(R.drawable.userxhdpi).into(post_image);
        }

    }

    //Handling floating action bar onClick
    private void clickHandler() {
        startActivity(new Intent(MainActivity.this, AddCustomerActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Logout) {
            logoutActivity.user_logout();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
