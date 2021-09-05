package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
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
import com.ssmptc.onwheelrent.Database.UploadedAdapter;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;

import java.util.Objects;

public class UpdateVehicleDetails extends AppCompatActivity {

    String vehicleId;

    ImageView btn_back;
    private  Button btn_update,btn_cancel;

    private TextInputLayout et_vNumber,et_vName,et_amount,et_OwnerName,et_place;
    private ImageView btn_chooseImg;
    private RadioGroup radioGroup;
    private RadioButton rb_bike,rb_car,rb_other,rb_selected;
    private ProgressDialog progressDialog;

    //vars
    private DatabaseReference root,rootImage,reference ;
    private StorageReference storageReference;
    private FirebaseStorage ImgStorage;
    private Uri filePath;
    private String phone_Number;
    private String  vId,vNumber,vName,amount,ownerName,place,imgUrl;
    private Boolean ImgPick = false;
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_vehicle_details);

        //---------------Get Data From Previous Activity----------------
        vehicleId = getIntent().getStringExtra("VehicleId");

        btn_back = findViewById(R.id.btn_back);

        btn_chooseImg = findViewById(R.id.btn_chooseImage);
        btn_update= findViewById(R.id.btn_update);
        btn_cancel= findViewById(R.id.btn_cancel);

        // Update Details
        et_vNumber = findViewById(R.id.et_vNumber);
        et_vName = findViewById(R.id.et_vName);
        et_amount = findViewById(R.id.et_amount);
        et_OwnerName = findViewById(R.id.et_ownerName);
        et_place = findViewById(R.id.et_place);
        radioGroup = findViewById(R.id.radio_group);
        rb_bike = findViewById(R.id.radio_bike);
        rb_car = findViewById(R.id.radio_car);
        rb_other = findViewById(R.id.radio_other);

        manager = new SessionManager(getApplicationContext());
        phone_Number = manager.getPhone();

        ImgStorage= FirebaseStorage.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("Vehicles").child(vehicleId);

        storageReference = FirebaseStorage.getInstance().getReference("VehicleImages");

        loadProgressDialog();

        loadData();

        btn_back.setOnClickListener(v -> {
            onBackPressed();
        });

        btn_cancel.setOnClickListener(v -> {
            onBackPressed();
        });

        btn_update.setOnClickListener(v -> updateData());



        btn_chooseImg.setOnClickListener(v -> chooseImage());



    }

    private void updateData() {

        if (!validateVNumber() & !validateVName() & !validateOwnerName() & !validateAmount() & !validatePlace()) {

                   return;
        }


        loadProgressDialog();

        FirebaseDatabase.getInstance().getReference("Vehicles").child(vehicleId)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String _vName = snapshot.child("vehicleName").getValue(String.class);
                String _vNumber = snapshot.child("vehicleNumber").getValue(String.class);
                String _amount = snapshot.child("amount").getValue(String.class);
                String _ownerName = snapshot.child("ownerName").getValue(String.class);
                String _place = snapshot.child("place").getValue(String.class);
                String _category = snapshot.child("category").getValue(String.class);


                rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());

                if (_vName.equals(et_vName.getEditText().getText().toString()) &&
                        _vNumber.equals(et_vNumber.getEditText().getText().toString()) &&
                        _amount.equals(et_amount.getEditText().getText().toString()) &&
                        _category.equals(rb_selected.getText().toString()) &&
                        _place.equals(et_place.getEditText().getText().toString()) &&
                        _ownerName.equals(et_OwnerName.getEditText().getText().toString()) && filePath == null){

                    Toast.makeText(UpdateVehicleDetails.this, "Same data no changes", Toast.LENGTH_SHORT).show();

                }else {
                    loadProgressDialog();
                    Toast.makeText(UpdateVehicleDetails.this, "Data Updating", Toast.LENGTH_SHORT).show();

                    reference.child("vehicleName").setValue(et_vName.getEditText().getText().toString());
                    reference.child("vehicleNumber").setValue(et_vNumber.getEditText().getText().toString());
                    reference.child("amount").setValue(et_amount.getEditText().getText().toString());
                    reference.child("category").setValue(rb_selected.getText().toString());
                    reference.child("place").setValue(et_place.getEditText().getText().toString());
                    reference.child("ownerName").setValue(et_OwnerName.getEditText().getText().toString());

                    if (filePath == null) {
                        Toast.makeText(UpdateVehicleDetails.this, "Updated", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }

                if (filePath != null) {
                    updateImg(filePath);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressDialog.dismiss();

    }

    private void updateImg(Uri uri) {

        loadProgressDialog();

        if (imgUrl != null){
            StorageReference imageRef = ImgStorage.getReferenceFromUrl(imgUrl);

            imageRef.delete().addOnSuccessListener(aVoid -> {
                reference.child("imgUrl").removeValue();
            });

        }

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));


        fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri1 -> {

            reference.child("imgUrl").setValue(uri1.toString());

            ImgPick = true;

            progressDialog.dismiss();

            filePath = null;
            Toast.makeText(UpdateVehicleDetails.this, "Image Updated", Toast.LENGTH_SHORT).show();

        })).addOnProgressListener(snapshot -> {

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(UpdateVehicleDetails.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
        });


    }

    private void loadData() {
        FirebaseDatabase.getInstance().getReference("Vehicles").child(vehicleId)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //---------------Get Data From Shop DataBase----------------

                et_vName.getEditText().setText(snapshot.child("vehicleName").getValue(String.class));
                et_vNumber.getEditText().setText(snapshot.child("vehicleNumber").getValue(String.class));
                et_amount.getEditText().setText(snapshot.child("amount").getValue(String.class));
                et_OwnerName.getEditText().setText(snapshot.child("ownerName").getValue(String.class));
                et_place.getEditText().setText(snapshot.child("place").getValue(String.class));

                imgUrl = snapshot.child("imgUrl").getValue(String.class);

                Picasso.with(UpdateVehicleDetails.this)
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .into(btn_chooseImg);

                String category = snapshot.child("category").getValue(String.class);

                assert category != null;
                if (category.equals("Car")){
                    rb_car.setChecked(true);
                }
                if (category.equals("bike")){
                    rb_bike.setChecked(true);
                }
                if (category.equals("Other")){
                    rb_other.setChecked(true);
                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //-----------------------Progress Dialog-------------------
    private void loadProgressDialog() {

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(UpdateVehicleDetails.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void chooseImage() {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//-- checking request code and result code if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK then set image in the image view--

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            btn_chooseImg.setImageURI(filePath);

        }

    }


    //--------------------Get image file extension------------------
    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }


    //----------------------------- Validate input Text Required ------------------------------

    private boolean validateVName(){
        String val1 = Objects.requireNonNull(et_vName.getEditText()).getText().toString().trim();

        if (val1.isEmpty()){
            et_vName.setError("Field can not be empty");
            return false;
        }
        else{
            et_vName.setError(null);
            et_vName.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateVNumber(){
        String val1 = Objects.requireNonNull(et_vNumber.getEditText()).getText().toString().trim();

        if (val1.isEmpty()){
            et_vNumber.setError("Field can not be empty");
            return false;
        }
        else{
            et_vNumber.setError(null);
            et_vNumber.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateAmount(){
        String val1 = Objects.requireNonNull(et_amount.getEditText()).getText().toString().trim();

        if (val1.isEmpty()){
            et_amount.setError("Field can not be empty");
            return false;
        }
        else{
            et_amount.setError(null);
            et_amount.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateOwnerName(){
        String val1 = Objects.requireNonNull(et_OwnerName.getEditText()).getText().toString().trim();

        if (val1.isEmpty()){
            et_OwnerName.setError("Field can not be empty");
            return false;
        }
        else{
            et_OwnerName.setError(null);
            et_OwnerName.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validatePlace(){
        String val1 = Objects.requireNonNull(et_place.getEditText()).getText().toString().trim();

        if (val1.isEmpty()){
            et_place.setError("Field can not be empty");
            return false;
        }
        else{
            et_place.setError(null);
            et_place.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateCategory(){

        return radioGroup.getCheckedRadioButtonId() != -1;
    }



}

