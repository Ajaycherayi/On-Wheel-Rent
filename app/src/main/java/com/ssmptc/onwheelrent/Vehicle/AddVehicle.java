package com.ssmptc.onwheelrent.Vehicle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.Database.VehicleData;
import com.ssmptc.onwheelrent.R;
import com.ssmptc.onwheelrent.User.Dashboard;
import java.util.Objects;

public class AddVehicle extends AppCompatActivity {


    ImageView btn_back;
    Button btn_upload,btn_cancel;

    private TextInputLayout et_vNumber,et_vName,et_amount,et_OwnerName,et_place;
    TextView tv_phone;
    private ImageView btn_chooseImg;
    private RadioGroup radioGroup;
    private RadioButton rb_selected;
    private ProgressDialog progressDialog;

    //vars
    private DatabaseReference root;
    private StorageReference storageReference;
    private Uri filePath;
    private String phone_Number;
    private String  vId,vNumber,vName,amount,ownerName,place;
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);


        btn_back = findViewById(R.id.btn_backToD);

        btn_chooseImg = findViewById(R.id.btn_chooseImage);
        btn_upload= findViewById(R.id.btn_upload);
        btn_cancel= findViewById(R.id.btn_cancel);

        // Update Details
        et_vNumber = findViewById(R.id.et_vNumber);
        et_vName = findViewById(R.id.et_vName);
        et_amount = findViewById(R.id.et_amount);
        et_OwnerName = findViewById(R.id.et_ownerName);
        tv_phone = findViewById(R.id.tv_phoneNo);
        et_place = findViewById(R.id.et_place);
        radioGroup = findViewById(R.id.radio_group);


        manager = new SessionManager(getApplicationContext());
        phone_Number = manager.getPhone();

        tv_phone.setText(phone_Number);

        root = FirebaseDatabase.getInstance().getReference("Vehicles");


        storageReference = FirebaseStorage.getInstance().getReference("VehicleImages");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finishAffinity();
        });

        btn_cancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finishAffinity();
        });

        btn_upload.setOnClickListener(v -> {

            if (validateVNumber() & validateVName() & validateOwnerName() & validateAmount() & validatePlace()) {

                if (validateCategory()){
                    if (filePath != null) {
                        uploadData(filePath);
                        Toast.makeText(AddVehicle.this, "Uploading...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddVehicle.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddVehicle.this, "Please select a category", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(AddVehicle.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
            }

        });



        btn_chooseImg.setOnClickListener(v -> chooseImage());
    }

    //-----------------------Progress Dialog-------------------
    private void loadProgressDialog() {

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(AddVehicle.this);
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

    // UploadImage method
    private void uploadData(Uri uri) {


        loadProgressDialog();

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));


        /*-----------------------------------------------------------------------------------------------------------------------------
               fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
       -------------------------------------------------------------------------------------------------------------------------------*/

        fileReference.putFile(uri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri1 -> {


            rb_selected = findViewById(radioGroup.getCheckedRadioButtonId());
            String category =  rb_selected.getText().toString();

            vNumber = et_vNumber.getEditText().getText().toString();
            vName = et_vName.getEditText().getText().toString();
            amount = et_amount.getEditText().getText().toString();
            ownerName = et_OwnerName.getEditText().getText().toString();
            place = et_place.getEditText().getText().toString();
            place = et_place.getEditText().getText().toString();
            vId = root.push().getKey();

            if (vId != null) {


                VehicleData vData = new VehicleData(vId,vNumber,vName,category,amount,ownerName,phone_Number,place,uri1.toString(), false);
                root.child(vId).setValue(vData);

            }

            Toast.makeText(AddVehicle.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            et_vNumber.getEditText().setText("");
            et_vName.getEditText().setText("");
            et_place.getEditText().setText("");
            et_amount.getEditText().setText("");
            et_OwnerName.getEditText().setText("");
            radioGroup.clearCheck();
            filePath = null;
            btn_chooseImg.setImageResource(R.drawable.ic_add_img);

            /*-------------------------------------------------------------------------------
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
             -------------------------------------------------------------------------------*/

        })).addOnProgressListener(snapshot -> {

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(AddVehicle.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
        });
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
        finish();
    }

}