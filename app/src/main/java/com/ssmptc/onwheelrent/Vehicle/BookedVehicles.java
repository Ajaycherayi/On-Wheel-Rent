package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.Database.VehicleBookAdapter;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;

import java.util.ArrayList;

public class BookedVehicles extends AppCompatActivity implements VehicleBookAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    ImageView btn_back;

    private ArrayList<VehicleData> list;
    private VehicleBookAdapter vehicleAdapter;

    SessionManager manager;

    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_booked);


        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(BookedVehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        manager = new SessionManager(getApplicationContext());
        phoneNumber = manager.getPhone();

        btn_back = findViewById(R.id.btn_back);

        recyclerView = findViewById(R.id.rv_booked);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        vehicleAdapter = new VehicleBookAdapter(BookedVehicles.this,list);
        recyclerView.setAdapter(vehicleAdapter);
        vehicleAdapter.setOnItemClickListener(BookedVehicles.this);

        btn_back.setOnClickListener(v -> onBackPressed());

        FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("bookedVehicles")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            String vehicleId = dataSnapshot.getValue(String.class);

                            FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("id").equalTo(vehicleId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot bookSnapshot) {
                                            for (DataSnapshot bSnapshot : bookSnapshot.getChildren()) {

                                                VehicleData vehicleData = bSnapshot.getValue(VehicleData.class);
                                                assert vehicleData != null;
                                                vehicleData.setId (dataSnapshot.getKey());
                                                list.add(vehicleData);

                                            }
                                            recyclerView.setAdapter(vehicleAdapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            progressDialog.dismiss();
                                            Toast.makeText(BookedVehicles.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(BookedVehicles.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onItemClick(int position) {

        VehicleData vehicleData = list.get(position);
        Toast.makeText(BookedVehicles.this, "Vehicle Details", Toast.LENGTH_SHORT).show();

        String id = vehicleData.getId();

        FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("id").equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            Intent intent = new Intent(BookedVehicles.this, SingleVehicleBooked.class);
                            intent.putExtra("VehicleId",id); // Pass Shop Id value To ShopDetailsSingleView
                            startActivity(intent);
                        }else {
                            Toast.makeText(BookedVehicles.this, "Vehicle does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BookedVehicles.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }


}