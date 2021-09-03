package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.ssmptc.onwheelrent.Database.AdapterRented;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.Database.VehicleBookAdapter;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;

import java.util.ArrayList;

public class RentedVehicles extends AppCompatActivity implements AdapterRented.OnItemClickListener {

    private  RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private ArrayList<VehicleData> list;
    private AdapterRented adapterRented;
    DatabaseReference vehicleDb ;

    private ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_vehicles);

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(RentedVehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        vehicleDb = FirebaseDatabase.getInstance().getReference("Vehicles");

        recyclerView = findViewById(R.id.rv_rented);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        adapterRented = new AdapterRented(RentedVehicles.this,list);

        recyclerView.setAdapter(adapterRented);

        adapterRented.setOnItemClickListener(RentedVehicles.this);


        mDBListener = vehicleDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                    assert vehicleData != null;
                    vehicleData.setId (dataSnapshot.getKey());
                    list.add(vehicleData);

                }
                recyclerView.setAdapter(adapterRented);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(RentedVehicles.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(RentedVehicles.this, "HAi", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(RentedVehicles.this, "HAi", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        vehicleDb.removeEventListener(mDBListener);
    }
}