package com.wordpress.nikant20.milkdiary.View.UiModule;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.wordpress.nikant20.milkdiary.Model.CostModel;
import com.wordpress.nikant20.milkdiary.Model.User;
import com.wordpress.nikant20.milkdiary.R;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LoginActivity;
import com.wordpress.nikant20.milkdiary.View.LoginModule.LogoutActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    CostModel costModel;
    EditText editTextMilkInLitres;
    EditText editTextRate;
    EditText editTextDate;
//    Date date;
    Float milkInLitres;
    Float rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutActivity = new LogoutActivity();
        user = new User();
        costModel = new CostModel();

        editTextMilkInLitres = findViewById(R.id.editTextMilkInLitres);
        editTextRate = findViewById(R.id.editTextRate);
        editTextDate = findViewById(R.id.editTextDate);


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

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<User, UserViewHolder> adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class, R.layout.customer_list, UserViewHolder.class, databaseReference) {
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
                    public void onItemClick(View view, int position, Float milkInLitres, Float rate, Date date, EditText editTextMilkInLitres, EditText editTextRate, final EditText editTextDate) {

                        milkInLitres = Float.valueOf(editTextMilkInLitres.getText().toString());
                        rate = Float.valueOf(editTextRate.getText().toString());
                        //Converting edittext input into date format
                        //
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//                        try {
//                            date = sdf.parse(String.valueOf(editTextDate.getText()));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }

                        final Calendar myCalendar = Calendar.getInstance();
                        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateLabel();
                            }

                            private void updateLabel() {
                                String myFormat = "MM/dd/yy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                               editTextDate.setText(sdf.format(myCalendar.getTime()));
                               date  = editTextDate.getText().toString();

                            }
                        };
                        editTextDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(getApplicationContext(), date1, myCalendar
                                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });



                       costModel = new CostModel(milkInLitres, rate, date);

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
        EditText editTextDate;
        TextView textViewSubmit;
        CostModel costModel;
        Date date;
        Float milkInLitres;
        Float rate;

        View mView;

        UserViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position, Float milkinLitres, Float rate, Date date, EditText editTextMilkInLitres, EditText editTextRate, EditText editTextDate);
        }

        public void setOnClickListener(UserViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }

        public UserViewHolder(View itemView) {
            super(itemView);
            editTextMilkInLitres = itemView.findViewById(R.id.editTextMilkInLitres);
            editTextRate = itemView.findViewById(R.id.editTextRate);
            editTextDate = itemView.findViewById(R.id.editTextDate);
            textViewSubmit = itemView.findViewById(R.id.textViewSubmit);
//            milkInLitres = Float.valueOf(editTextMilkInLitres.getText().toString());
//            rate = Float.valueOf(editTextRate.getText().toString());

            //Converting edittext input into date format
            //
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//            try {
//                date = sdf.parse(String.valueOf(editTextDate.getText()));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            textViewSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition(), milkInLitres, rate, date, editTextMilkInLitres, editTextRate, editTextDate);
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
