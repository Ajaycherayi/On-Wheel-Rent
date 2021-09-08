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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssmptc.onwheelrent.Database.UploadedAdapter;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;
import java.util.ArrayList;

public class UploadedVehicles extends AppCompatActivity implements UploadedAdapter.OnItemClickListener {

    private  RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    ImageView btn_back;

    private ArrayList<VehicleData> list;
    private UploadedAdapter uploadedAdapter;
    DatabaseReference vehicleDb ;

    SessionManager manager;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_uploaded);

        btn_back = findViewById(R.id.btn_back);

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(UploadedVehicles.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        manager= new SessionManager(getApplicationContext());


        phoneNumber = manager.getPhone();
        vehicleDb = FirebaseDatabase.getInstance().getReference("Vehicles");
        Query checkUser = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("phone").equalTo(phoneNumber);

        recyclerView = findViewById(R.id.rv_rented);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        list = new ArrayList<>();

        uploadedAdapter = new UploadedAdapter(UploadedVehicles.this,list);

        recyclerView.setAdapter(uploadedAdapter);

        uploadedAdapter.setOnItemClickListener(UploadedVehicles.this);


        checkUser.addValueEventListener(new ValueEventListener() {
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

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        });

    }

    @Override
    public void onItemClick(int position) {

        VehicleData vehicleData = list.get(position);
        Toast.makeText(UploadedVehicles.this, "Vehicle Details", Toast.LENGTH_SHORT).show();

        String id = vehicleData.getId();

        FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("id").equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            Intent intent = new Intent(UploadedVehicles.this, SingleUploadedVehicles.class);
                            intent.putExtra("VehicleId",id); // Pass Shop Id value To ShopDetailsSingleView
                            startActivity(intent);
                        }else {
                            Toast.makeText(UploadedVehicles.this, "Vehicle does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
        finish();
    }

}