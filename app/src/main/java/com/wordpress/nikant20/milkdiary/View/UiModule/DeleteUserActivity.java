package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.wordpress.nikant20.milkdiary.Model.User;
import com.wordpress.nikant20.milkdiary.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeleteUserActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabaseReference,databaseReference, endUserDatabaseReference,childDatabaseRefrence,milkManDatabaseReference;
    ProgressDialog progressDialog;
    User user;
    FirebaseUser firebaseUser;
    int currentPosition;
    String adapterKey;
    List<String> adapterKeyList;
    AlertDialog.Builder builder;
    String userId;
    int currntPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        user = new User();
        adapterKeyList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        recyclerView = findViewById(R.id.recyclerViewDelete);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("MilkDiary");
        endUserDatabaseReference = databaseReference.child("EndUsers");
        milkManDatabaseReference = endUserDatabaseReference.child(userId);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
        childDatabaseRefrence = firebaseDatabase.getReference().child("MilkDiary").child("EndUsers").child(firebaseUser.getUid());
        progressDialog = new ProgressDialog(com.wordpress.nikant20.milkdiary.View.UiModule.DeleteUserActivity.this, R.style.AppTheme_Dark_Dialog);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        milkManDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                adapterKey = dataSnapshot.getKey();
                Log.i("key",adapterKey);
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


        final FirebaseRecyclerAdapter<User, DeleteUserViewHolder> adapter = new FirebaseRecyclerAdapter<User, DeleteUserViewHolder>(User.class, R.layout.delete_customer_list, DeleteUserViewHolder.class, childDatabaseRefrence) {
            @Override
            protected void populateViewHolder(DeleteUserViewHolder holder, User model, int position) {
                holder.setName(model.getName());
                holder.setEmail(model.getEmail());
                holder.setPhone(model.getPhone());
                holder.setAddress(model.getAddress());
                holder.setImage(getApplicationContext(), model.getImage());
            }

            @Override
            public DeleteUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                DeleteUserViewHolder deleteuserViewHolder = super.onCreateViewHolder(parent, viewType);
                deleteuserViewHolder.setOnClickListener(new DeleteUserViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        currntPosition = position;
                        Log.i("Position", String.valueOf(currntPosition));
                        builder = new AlertDialog.Builder(DeleteUserActivity.this);
                        builder.setTitle("Delete transaction History?").setMessage("Do you want to delete Transaction History?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference deleteQuery = milkManDatabaseReference.child(adapterKeyList.get(currntPosition));
                                        deleteQuery.setValue(null);
                                        notifyDataSetChanged();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                      dialog.cancel();
                            }
                        }).setIcon(R.drawable.trash).show();
                    }
                });
                return super.onCreateViewHolder(parent, viewType);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    public static class DeleteUserViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDelete;
        View mView;

        DeleteUserViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);
        }

        public void setOnClickListener(DeleteUserViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }

        public DeleteUserViewHolder(View itemView) {
            super(itemView);
            textViewDelete = itemView.findViewById(R.id.textViewDelete);

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v,getAdapterPosition());
                }
            });

            mView = itemView;

        }

        public void setName(String name) {
            TextView post_name = mView.findViewById(R.id.textViewCustomerName);
            post_name.setText(name);
        }

        public void setEmail(String email) {
            TextView post_email = mView.findViewById(R.id.textViewCustomerEmailId);
            post_email.setText(email);
        }

        public void setPhone(String phone) {
            TextView post_phone = mView.findViewById(R.id.textViewCustomerPhoneNumber);
            post_phone.setText(phone);
        }

        public void setAddress(String address) {
            TextView post_address = mView.findViewById(R.id.textViewCustomerAddress);
            post_address.setText(address);
        }

        public void setImage(Context context, String image) {
            CircleImageView post_image = mView.findViewById(R.id.imageViewCustomerProfile);
            Picasso.with(context).load(image).fit().centerCrop().placeholder(R.drawable.userxhdpi).error(R.drawable.userxhdpi).into(post_image);
        }

    }
}