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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssmptc.onwheelrent.Database.UploadedAdapter;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;

import java.util.ArrayList;

public class UploadedVehicles extends AppCompatActivity implements UploadedAdapter.OnItemClickListener {

    private  RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private ArrayList<VehicleData> list;
    private UploadedAdapter uploadedAdapter;
    DatabaseReference vehicleDb ;
    private FirebaseStorage ImgStorage;

    SessionManager manager;

    private ValueEventListener mDBListener;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaded_vehicles);

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(UploadedVehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        manager= new SessionManager(getApplicationContext());

        phoneNumber = manager.getPhone();
        ImgStorage= FirebaseStorage.getInstance();
        vehicleDb = FirebaseDatabase.getInstance().getReference("Vehicles");
        Query checkUser = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("phone").equalTo(phoneNumber);

        recyclerView = findViewById(R.id.rv_rented);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        list = new ArrayList<>();

        uploadedAdapter = new UploadedAdapter(UploadedVehicles.this,list);

        recyclerView.setAdapter(uploadedAdapter);

        uploadedAdapter.setOnItemClickListener(UploadedVehicles.this);


        mDBListener = checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                    assert vehicleData != null;
                    vehicleData.setId (dataSnapshot.getKey());
                    list.add(vehicleData);

                }
                recyclerView.setAdapter(uploadedAdapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(UploadedVehicles.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(int position) {

        Toast.makeText(UploadedVehicles.this, "Press 1 sec", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Delete Click", Toast.LENGTH_SHORT).show();

        VehicleData selectedVehicle = list.get(position);

        String selectedKey = selectedVehicle.getId();

        StorageReference imageRef = ImgStorage.getReferenceFromUrl(selectedVehicle.getImgUrl());

        imageRef.delete().addOnSuccessListener(aVoid -> {

            vehicleDb.child(selectedKey).removeValue();
            Toast.makeText(UploadedVehicles.this, "Vehicle Data Deleted..", Toast.LENGTH_SHORT).show();

            Query checkUser = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("phone").equalTo(phoneNumber);
                   checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                startActivity(new Intent(UploadedVehicles.this, Dashboard.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        });

    }
}