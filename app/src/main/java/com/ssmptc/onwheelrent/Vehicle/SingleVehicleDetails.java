package com.ssmptc.onwheelrent.Vehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.ssmptc.onwheelrent.R;

public class SingleVehicleDetails extends AppCompatActivity {

    Button btn_call,btn_book;
    ImageView img_vehicle,btn_back;

    ProgressDialog progressDialog;

    private TextView tv_vName,tv_category,tv_vNumber,tv_place,tv_amount,tv_OwnerName,tv_phone;

    private String shopId,pushKey,shopName,category,location,email,phoneNumber;

    private DatabaseReference shopDb;

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
    }
}