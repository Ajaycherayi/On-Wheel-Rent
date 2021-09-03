package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.loader.ResourcesProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.ssmptc.onwheelrent.R;

public class SingleVehicleDetails extends AppCompatActivity {

    Button btn_call,btn_book;
    ImageView img_vehicle,btn_back;

    ProgressDialog progressDialog;

    private TextView tv_vName,tv_category,tv_vNumber,tv_place,tv_amount,tv_OwnerName,tv_phone;

    private String vehicleId,vehicleName,imgUrl;
    private boolean bookStatus;
    private DatabaseReference vehicleDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_vehicle_details);

        btn_back = findViewById(R.id.btn_back);

        btn_book = findViewById(R.id.btn_book);
        btn_call = findViewById(R.id.btn_call);

        tv_vName = findViewById(R.id.tv_vName);
        tv_vNumber = findViewById(R.id.tv_vNumber);
        tv_category = findViewById(R.id.tv_category);
        tv_amount = findViewById(R.id.tv_amount);
        tv_OwnerName = findViewById(R.id.tv_ownerName);
        tv_phone = findViewById(R.id.tv_phoneNo);
        tv_place = findViewById(R.id.tv_place);

        img_vehicle = findViewById(R.id.img_vehicle);

        //---------------Get Data From Previous Activity----------------
        vehicleId = getIntent().getStringExtra("VehicleId");

        //--------------- Initialize ProgressDialog -----------
        progressDialog = new ProgressDialog(SingleVehicleDetails.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        vehicleDb = FirebaseDatabase.getInstance().getReference("Vehicles").child(vehicleId);
        vehicleDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //---------------Get Data From Shop DataBase----------------
                vehicleName = snapshot.child("vehicleName").getValue(String.class);
                tv_vName.setText(vehicleName);

                tv_vNumber.setText(snapshot.child("vehicleNumber").getValue(String.class));
                tv_category.setText(snapshot.child("category").getValue(String.class));
                tv_amount.setText(snapshot.child("amount").getValue(String.class));
                tv_OwnerName.setText(snapshot.child("ownerName").getValue(String.class));
                tv_phone.setText(snapshot.child("phone").getValue(String.class));
                tv_place.setText(snapshot.child("place").getValue(String.class));
                imgUrl = snapshot.child("imgUrl").getValue(String.class);

                progressDialog.dismiss();



                Glide.with(SingleVehicleDetails.this)
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .into(img_vehicle);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            private boolean state = false;
            public void onClick(View v) {
                if ( state ) {
                    state = false;
                    unBookVehicle();
                } else {
                    state = true;
                    bookVehicle();
                }
            }

        });

    }

    private void unBookVehicle() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking");
        builder.setMessage("Are you sure to Book ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                btn_book.setText("Book Vehicle");
                btn_book.setBackgroundColor(getResources().getColor(R.color.primaryDark));

                vehicleDb.child("booked").setValue(false);


            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void bookVehicle() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking");
        builder.setMessage("Are you sure to Book ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                btn_book.setText("Vehicle Booked");
                btn_book.setBackgroundColor(getResources().getColor(R.color.light_green));

                vehicleDb.child("booked").setValue(true);


            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}