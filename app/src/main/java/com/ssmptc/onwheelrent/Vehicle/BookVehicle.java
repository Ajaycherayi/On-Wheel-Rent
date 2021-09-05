package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssmptc.onwheelrent.Database.VehicleBookAdapter;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;

import java.util.ArrayList;

public class BookVehicle extends AppCompatActivity implements VehicleBookAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    TextInputLayout drop_menu;
    AutoCompleteTextView drop_list;

    private EditText et_search;

    private ArrayList<VehicleData> list;
    private VehicleBookAdapter vehicleAdapter;
    Query vehicleDb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_for_booking);

        et_search = findViewById(R.id.et_search);

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(BookVehicle.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        drop_menu = findViewById(R.id.menu_drop);
        drop_list = findViewById(R.id.at_category);

        String [] items = {"all","Car","bike","Others"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(BookVehicle.this,R.layout.category_list,items);

        drop_list.setAdapter(arrayAdapter);


        recyclerView = findViewById(R.id.rv_book);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        list = new ArrayList<>();
        vehicleAdapter = new VehicleBookAdapter(BookVehicle.this,list);
        recyclerView.setAdapter(vehicleAdapter);




        search();

        filter();

        loadRecycler();







    }

    private void filter() {
        drop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = String.valueOf(parent.getItemAtPosition(position));

                vehicleDb = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("booked").equalTo(false);
                vehicleDb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!category.equals("all")){

                            if (snapshot.exists()){

                                FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("category").equalTo(category)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot categorySnapshot) {

                                                if (categorySnapshot.exists()){

                                                    list.clear();

                                                    for (DataSnapshot dataSnapshot : categorySnapshot.getChildren()){

                                                        VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                                                        list.add(vehicleData);
                                                        vehicleAdapter = new VehicleBookAdapter(BookVehicle.this,list);
                                                        recyclerView.setAdapter(vehicleAdapter);
                                                        vehicleAdapter.notifyDataSetChanged();
                                                        vehicleAdapter.setOnItemClickListener(BookVehicle.this);
                                                    }

                                                    progressDialog.dismiss();


                                                }else {
                                                    list.clear();
                                                    vehicleAdapter.notifyDataSetChanged();
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                            }else {
                                Toast.makeText(BookVehicle.this, "Failed to load Data", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            loadRecycler();
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(BookVehicle.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }

    //----------------------Search Shop Details----------------
    private void search() {
        if (et_search != null){

            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    vehicleAdapter.Search(s);
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void loadRecycler() {
        vehicleDb = FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("booked").equalTo(false);
        vehicleDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                    list.add(vehicleData);
                    vehicleAdapter = new VehicleBookAdapter(BookVehicle.this,list);
                    recyclerView.setAdapter(vehicleAdapter);
                    vehicleAdapter.notifyDataSetChanged();
                    vehicleAdapter.setOnItemClickListener(BookVehicle.this);
                }

                progressDialog.dismiss();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(BookVehicle.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        VehicleData vehicleData = list.get(position);
        Toast.makeText(BookVehicle.this, "Vehicle Details", Toast.LENGTH_SHORT).show();

        String id = vehicleData.getId();

        FirebaseDatabase.getInstance().getReference("Vehicles").orderByChild("id").equalTo(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){
                            Intent intent = new Intent(BookVehicle.this, SingleVehicleForBook.class);
                            intent.putExtra("VehicleId",id); // Pass Shop Id value To ShopDetailsSingleView
                            startActivity(intent);
                        }else {
                            Toast.makeText(BookVehicle.this, "Vehicle does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }

}