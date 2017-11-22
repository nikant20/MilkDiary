package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.app.ProgressDialog;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.wordpress.nikant20.milkdiary.Model.User;
import com.wordpress.nikant20.milkdiary.Model.UserViewHolder;
import com.wordpress.nikant20.milkdiary.R;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LoginActivity;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LogoutActivity;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    LogoutActivity logoutActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutActivity = new LogoutActivity();

        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.fabAddCustomer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);


        if (recyclerView != null) {
            //to enable optimization of recyclerview
            recyclerView.setHasFixedSize(true);
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                holder.setEmailId(model.getEmail());
                holder.setAddress(model.getAddress());
                holder.setPhoneNumber(model.getPhone());
                holder.setImage(model.getImage(),getApplicationContext());
            }

        };

        recyclerView.setAdapter(adapter);

     /*   DatabaseReference dataRefrence = FirebaseDatabase.getInstance().getReference("MilkDiary").child("MilkMan");
        DatabaseReference keyQuery = FirebaseDatabase.getInstance().getReference("MilkDiary").child("EndUsers");
        FirebaseRecyclerOptions<User> recyclerOptions = new FirebaseRecyclerOptions.Builder<User>().setIndexedQuery(keyQuery,dataRefrence,User.class).build();
       */


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
