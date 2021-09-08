package com.ssmptc.onwheelrent.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ssmptc.onwheelrent.R;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    //Variable
    private TextInputLayout et_userName,et_phoneNumber,et_password;
    Button btn_getOtp,btn_login;

    ProgressDialog progressDialog;

    //FireBase Variables
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        et_userName = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);

        btn_getOtp = findViewById(R.id.btn_getOtp);
        btn_login = findViewById(R.id.btn_backToLogin);

        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));

        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //EditText Validations
                if (!validatePhoneNumber()  | !validateUserName() | !validatePassword()) {

                    return;
                }

                //Initialize ProgressDialog
                progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                progressDialog.setCancelable(false);


                String phone = et_phoneNumber.getEditText().getText().toString().trim();
                String phoneNumber = "+91" + phone;

                if (!phone.isEmpty()) {
                    if (phone.length() == 10) {

                        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(phoneNumber);

                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, "This User already Exist  Please Login", Toast.LENGTH_LONG).show();

                                } else {


                                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                                            .setPhoneNumber(phoneNumber)
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(SignUp.this)
                                            .setCallbacks(mCallBacks)
                                            .build();
                                    PhoneAuthProvider.verifyPhoneNumber(options);

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Please Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //sometime the code is not detected automatically
                //so user has to manually enter the code

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent otpIntent = new Intent(SignUp.this, OtpVerification.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        otpIntent.putExtra("auth", s);

                        String phoneNumber = "+91" + et_phoneNumber.getEditText().getText().toString();
                        String name = et_userName.getEditText().getText().toString();
                        String password = et_password.getEditText().getText().toString();

                        otpIntent.putExtra("phoneNumber", phoneNumber);
                        otpIntent.putExtra("name", name);
                        otpIntent.putExtra("password", password);
                        startActivity(otpIntent);
                        finish();
                    }

                }, 1);

            }
        };

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    private boolean validatePhoneNumber() {

        String val = et_phoneNumber.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            et_phoneNumber.setError("Field can not be empty");
            return false;
        }else if(val.length()>10 | val.length()<10){
            et_phoneNumber.setError("Please Enter 10 Digit Phone Number");
            return false;
        }else if (!val.matches("\\w*")){
            et_phoneNumber.setError("White spaces not allowed");
            return false;
        }else {
            et_phoneNumber.setError(null);
            return true;
        }
    }

    private boolean validateUserName() {
        String val = et_userName.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            et_userName.setError("Field can not be empty");
            return false;
        }else if(val.length()>25){
            et_userName.setError("Name is Too Large");
            return false;
        }else {
            et_userName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = et_password.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            et_password.setError("Field can not be empty");
            return false;
        }else if(val.length()<8) {
            et_password.setError("Password minimum 8 Characters");
            return false;
        }else if (!val.matches("\\w*")){
            et_password.setError("White spaces not allowed");
            return false;
        }else {
            et_password.setError(null);
            return true;
        }

    }


}