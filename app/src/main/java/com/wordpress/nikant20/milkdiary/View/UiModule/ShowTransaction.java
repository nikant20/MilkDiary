package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.wordpress.nikant20.milkdiary.Model.CostModel;
import com.wordpress.nikant20.milkdiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikant20 on 12/15/2017.
 */

public class ShowTransaction extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference, diaryDatabaseReference,childDatabaseRefrence,milkManDatabaseReference;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    String diaryMilkManKey,milkManEndUserChildKey,userId;
    List<String> diaryMilkManKeyList;
    MainActivity mainActivity;
    List<String> milkMankeyList,milkManEndUserChildKeyList;
    String key;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction);


           key =  getIntent().getStringExtra("key");
           Log.i("current", "key is: "+key);
           diaryMilkManKeyList = new ArrayList<>();
           milkMankeyList = new ArrayList<>();
           milkManEndUserChildKeyList = new ArrayList<>();
           mainActivity = new MainActivity();
           firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
           userId = firebaseUser.getUid();


           //RecyclerView Initializations
           recyclerView = findViewById(R.id.recyclerViewTransactions);
           recyclerView.setHasFixedSize(true);
           layoutManager = new LinearLayoutManager(this);
           recyclerView.setLayoutManager(layoutManager);
          //FirebaseRefrences initalizations

           databaseReference = FirebaseDatabase.getInstance().getReference().child("MilkDiary");
           diaryDatabaseReference = databaseReference.child("Diary");
           milkManDatabaseReference = diaryDatabaseReference.child(userId);
           milkManDatabaseReference.child(key).addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   diaryMilkManKey = dataSnapshot.getKey();
                   Log.i("diaryMilkMankey","Key is: "+diaryMilkManKey);
                   diaryMilkManKeyList.add(diaryMilkManKey);

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

        FirebaseRecyclerAdapter<CostModel,CostModelViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<CostModel, CostModelViewHolder>(CostModel.class,R.layout.activity_add_transactions,CostModelViewHolder.class,diaryDatabaseReference) {
            @Override
            protected void populateViewHolder(CostModelViewHolder viewHolder, CostModel model, int position) {
                    viewHolder.setTextViewDate(model.getDate());
                    viewHolder.setMilkQuantity(String.valueOf(model.getMilkInLitres()));
                    viewHolder.setMilkRate(String.valueOf(model.getRate()));
                    viewHolder.setTotal(String.valueOf(model.getTotal()));
            }
        };
       recyclerView.setAdapter(recyclerAdapter);
    }

    public static class CostModelViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewMilkRate;
        TextView textViewMilkQuantity;
        TextView textViewTotal;

        View mView;


        public CostModelViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewTransactionDate);
            textViewMilkQuantity = itemView.findViewById(R.id.textViewMilkQuantity);
            textViewMilkRate = itemView.findViewById(R.id.textViewMilkRate);
            textViewTotal = itemView.findViewById(R.id.textViewMilkTotal);

            mView = itemView;

        }

        public void setTextViewDate(String date) {
            TextView setDate = mView.findViewById(R.id.textViewDate);
            setDate.setText(date);
        }

        public void setMilkQuantity(String milkQuantity) {
            TextView setQuantity = mView.findViewById(R.id.textViewMilkQuantity);
            setQuantity.setText(milkQuantity);
        }

        public void setMilkRate(String milkRate) {
            TextView setRate = mView.findViewById(R.id.textViewMilkRate);
            setRate.setText(milkRate);
        }

        public void setTotal(String total) {
            TextView setTotal = mView.findViewById(R.id.textViewMilkTotal);
            setTotal.setText(total);
        }

    }
}
