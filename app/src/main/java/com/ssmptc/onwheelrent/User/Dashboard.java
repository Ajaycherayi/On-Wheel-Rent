package com.ssmptc.onwheelrent.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssmptc.onwheelrent.About;
import com.ssmptc.onwheelrent.ContactUs;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.Vehicle.BookVehicle;
import com.ssmptc.onwheelrent.Vehicle.BookedVehicles;
import com.ssmptc.onwheelrent.Vehicle.AddVehicle;
import com.ssmptc.onwheelrent.Vehicle.UploadedVehicles;

public class Dashboard extends AppCompatActivity {


    MaterialCardView btn_logOut,btn_exit,btn_rent,btn_rented,btn_book,btn_booked,btn_about,btn_contact;
    TextView tv_userName;

    
    SessionManager manager;
    String phoneNumber,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashborad);

        btn_rent = findViewById(R.id.btn_Rent);
        btn_rented = findViewById(R.id.btn_rented);
        btn_book = findViewById(R.id.btn_book_vehicle);
        btn_booked = findViewById(R.id.btn_booked);
        btn_logOut = findViewById(R.id.btn_logOut);
        btn_exit = findViewById(R.id.btn_exit);
        btn_about = findViewById(R.id.btn_appInfo);
        btn_contact = findViewById(R.id.btn_contact);

        tv_userName = findViewById(R.id.tv_userName);

        manager = new SessionManager(getApplicationContext());
        phoneNumber = manager.getPhone();
        userName = manager.getName();

        tv_userName.setText("Hai, "+userName);

        btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddVehicle.class));
            }
        });

        btn_rented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query checkUser = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("phone").equalTo(phoneNumber);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(Dashboard.this, "No Vehicle Details", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AddVehicle.class));
                        }else
                            startActivity(new Intent(getApplicationContext(), UploadedVehicles.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookVehicle.class));
            }
        });

        btn_booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("bookedVehicles")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()){
                                    Toast.makeText(Dashboard.this, "No vehicles booked", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), BookVehicle.class));
                                }else
                                    startActivity(new Intent(getApplicationContext(), BookedVehicles.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Thank you :)", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), About.class));
            }
        });

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactUs.class));
            }
        });

    }

    private void logout() {

        manager = new SessionManager(getApplicationContext());

        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        //Set Title
        builder.setTitle("Log out");

        //set Message
        builder.setMessage("Are you sure to Log out ?");

        //positive YES button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                manager.setUserLogin(false);
                manager.setDetails("","","");

                
                //activity.finishAffinity();
                dialog.dismiss();

                //Finish Activity
                startActivity(new Intent(getApplicationContext(), SignUp.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                Toast.makeText(Dashboard.this, "Thank You :)", Toast.LENGTH_SHORT).show();
                finish();
                
            }
        });

        //Negative NO button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss Dialog
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Thank you :)", Toast.LENGTH_SHORT).show();
        finishAffinity();
    }
    
}