package com.ssmptc.onwheelrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ssmptc.onwheelrent.Database.SessionManager;
import com.ssmptc.onwheelrent.User.Dashboard;
import com.ssmptc.onwheelrent.User.SignUp;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIMER = 3400;
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        new Handler().postDelayed(() -> {

            //  startActivity( new Intent(MainActivity.this, ShopSignup.class));
            //Initialize SessionManager
            manager = new SessionManager(getApplicationContext());

            if (manager.getUserLogin()){
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }else {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
            finish();

        },SPLASH_TIMER);


    }
}