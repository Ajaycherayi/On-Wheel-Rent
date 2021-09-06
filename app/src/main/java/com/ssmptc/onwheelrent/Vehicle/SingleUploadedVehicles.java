package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;

public class SingleUploadedVehicles extends AppCompatActivity {

    Button btn_edit,btn_delete,btn_call;
    ImageView img_vehicle,btn_back;

    LinearLayout ly_booked;
    TextView title_booked,hint_name,hint_phone,tv_bookedName,tv_bookedPhone,tv_bookStatus;

    ProgressDialog progressDialog;

    private TextView tv_vName,tv_category,tv_vNumber,tv_place,tv_amount,tv_OwnerName,tv_phone;

    private String vehicleId,vehicleName,imgUrl,phoneNumber,name,bookedPhoneNumber=null;
    private boolean bookStatus;

    private FirebaseStorage ImgStorage;
    private DatabaseReference vehicleDb,bookDb,userDb;

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_uploaded_vehicles);

        ly_booked = findViewById(R.id.ly_booked);
        title_booked = findViewById(R.id.title_booked);
        hint_name = findViewById(R.id.hintName_booked);
        hint_phone = findViewById(R.id.hintPhone_booked);
        tv_bookedName = findViewById(R.id.tv_bookedName);
        tv_bookedPhone = findViewById(R.id.tv_bookedPhone);
        tv_bookStatus = findViewById(R.id.tv_bookStatus);
        btn_call = findViewById(R.id.btn_call);

        ly_booked.setVisibility(View.GONE);
        title_booked.setVisibility(View.GONE);
        hint_name.setVisibility(View.GONE);
        hint_phone.setVisibility(View.GONE);
        tv_bookedName.setVisibility(View.GONE);
        tv_bookedPhone.setVisibility(View.GONE);
        btn_call.setVisibility(View.GONE);




        btn_back = findViewById(R.id.btn_back);

        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);


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
        progressDialog = new ProgressDialog(SingleUploadedVehicles.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImgStorage= FirebaseStorage.getInstance();

        manager = new SessionManager(getApplicationContext());
        phoneNumber = manager.getPhone();
        name = manager.getName();

        bookDb = FirebaseDatabase.getInstance().getReference("Vehicles").child(vehicleId).child("bookedBy");
        userDb = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("bookedVehicles");

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

                Boolean bookStatus = snapshot.child("booked").getValue(Boolean.class);

                assert bookStatus != null;
                if (bookStatus.equals(false)){
                    tv_bookStatus.setVisibility(View.VISIBLE);
                }else {
                    bookedTrue();
                }

                progressDialog.dismiss();



                Picasso.with(SingleUploadedVehicles.this)
                        .load(imgUrl)
                        .placeholder(R.drawable.bg_loading)
                        .error(R.drawable.bg_loading)
                        .into(img_vehicle);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + bookedPhoneNumber));
                startActivity(callIntent);

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleUploadedVehicles.this, UpdateVehicleDetails.class);
                intent.putExtra("VehicleId",vehicleId); // Pass Shop Id value To ShopDetailsSingleView
                startActivity(intent);
            }
        });

    }

    private void onDelete() {

        Toast.makeText(this, "Delete Click", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setTitle("Booking");
        builder.setMessage("Are you sure to Book ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                StorageReference imageRef = ImgStorage.getReferenceFromUrl(imgUrl);

                imageRef.delete().addOnSuccessListener(aVoid -> {

                    DatabaseReference deleteDb = FirebaseDatabase.getInstance().getReference("Vehicles");
                    deleteDb.child(vehicleId).removeValue();

                    if (bookedPhoneNumber != null){

                        DatabaseReference removeBooked = FirebaseDatabase.getInstance().getReference("Users").child(bookedPhoneNumber).child("bookedVehicles");

                        removeBooked.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    removeBooked.child(vehicleId).removeValue();
                                    startActivity(new Intent(getApplicationContext(),UploadedVehicles.class));
                                } else {
                                    Toast.makeText(SingleUploadedVehicles.this, "Book doesn't exist  ", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else {
                        startActivity(new Intent(getApplicationContext(),UploadedVehicles.class));
                    }



                    Toast.makeText(SingleUploadedVehicles.this, "Vehicle Data Deleted..", Toast.LENGTH_SHORT).show();

                    Query checkUser = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("phone").equalTo(phoneNumber);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                startActivity(new Intent(SingleUploadedVehicles.this, Dashboard.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                });

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

    private void bookedTrue() {

        tv_bookStatus.setVisibility(View.VISIBLE);
        tv_bookStatus.setText("Booked");
        tv_bookStatus.setBackgroundResource(R.drawable.bg_booking_active);
        tv_bookStatus.setTextColor(getResources().getColor(R.color.black));

        ly_booked.setVisibility(View.VISIBLE);
        title_booked.setVisibility(View.VISIBLE);
        hint_name.setVisibility(View.VISIBLE);
        hint_phone.setVisibility(View.VISIBLE);
        tv_bookedName.setVisibility(View.VISIBLE);
        tv_bookedPhone.setVisibility(View.VISIBLE);
        btn_call.setVisibility(View.VISIBLE);

        bookDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_bookedName.setText(snapshot.child("name").getValue(String.class));
                bookedPhoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                tv_bookedPhone.setText(bookedPhoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),UploadedVehicles.class));
        finish();
    }

}