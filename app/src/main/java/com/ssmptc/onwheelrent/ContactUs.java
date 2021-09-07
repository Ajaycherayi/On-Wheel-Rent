package com.ssmptc.onwheelrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssmptc.onwheelrent.Database.TeamAdapter;
import com.ssmptc.onwheelrent.Database.TeamData;
import com.ssmptc.onwheelrent.Database.VehicleBookAdapter;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.Vehicle.BookVehicle;

import java.util.ArrayList;

public class ContactUs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private ArrayList<TeamData> list;
    private TeamAdapter teamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(ContactUs.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        recyclerView = findViewById(R.id.rv_team);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();


        load();

    }

    private void load() {

        FirebaseDatabase.getInstance().getReference("TeamMembers")
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    TeamData teamData = dataSnapshot.getValue(TeamData.class);
                    list.add(teamData);
                    teamAdapter = new TeamAdapter(ContactUs.this,list);
                    recyclerView.setAdapter(teamAdapter);

                }

                progressDialog.dismiss();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(ContactUs.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}