package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.wordpress.nikant20.milkdiary.Model.User;
import com.wordpress.nikant20.milkdiary.R;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LoginActivity;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LogoutActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    LogoutActivity logoutActivity;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutActivity = new LogoutActivity();
        user = new User();

        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.fabAddCustomer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("MilkDiary").child("EndUsers");
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);



        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
/*
        //Querying database
        Query query = databaseReference.child("MilkDiary").child("EndUsers");
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query,User.class).build();


        FirebaseRecyclerAdapter<User,UserViewHolder> adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {

            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list,parent,false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UserViewHolder holder, int position, User model) {
              holder.setName(model.getName());
              holder.setEmail(model.getEmail());
              holder.setPhone(model.getPhone());
              holder.setAddress(model.getAddress());
              Glide.with(getApplicationContext()).load(databaseReference.child("MilkDiary").child("EndUsers").child("image"));

            }


        };

        recyclerView.setAdapter(adapter); */

     /*   DatabaseReference dataRefrence = FirebaseDatabase.getInstance().getReference("MilkDiary").child("MilkMan");
        DatabaseReference keyQuery = FirebaseDatabase.getInstance().getReference("MilkDiary").child("EndUsers");
        FirebaseRecyclerOptions<User> recyclerOptions = new FirebaseRecyclerOptions.Builder<User>().setIndexedQuery(keyQuery,dataRefrence,User.class).build();
       */


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<User,UserViewHolder> adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class,R.layout.customer_list,UserViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder holder, User model, int position) {
                holder.setName(model.getName());
                holder.setEmail(model.getEmail());
                holder.setPhone(model.getPhone());
                holder.setAddress(model.getAddress());
                holder.setImage(getApplicationContext(),model.getImage());
            }
        };
        recyclerView.setAdapter(adapter);


    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }
        public void setName(String name){
            TextView post_name = mView.findViewById(R.id.textViewName);
            post_name.setText(name);
        }
        public void setEmail(String email){
            TextView post_email = mView.findViewById(R.id.textViewEmailId);
            post_email.setText(email);
        }
        public void setPhone(String phone){
            TextView post_phone = mView.findViewById(R.id.textViewPhoneNumber);
            post_phone.setText(phone);
        }
        public void setAddress(String address){
            TextView post_address = mView.findViewById(R.id.textViewAddress);
            post_address.setText(address);
        }
        public void setImage(Context context,String image){
            CircleImageView post_image = mView.findViewById(R.id.imageView);
            Picasso.with(context).load(image).fit().centerCrop().placeholder(R.drawable.userxhdpi).error(R.drawable.userxhdpi).into(post_image);
        }

    }

    //Handling floating action bar onClick
    private void clickHandler() {
        startActivity(new Intent(MainActivity.this,AddCustomerActivity.class));
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
