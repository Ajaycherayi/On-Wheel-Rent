package com.ssmptc.onwheelrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class About extends AppCompatActivity {

    ImageView source_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        source_code = findViewById(R.id.source_code);

        source_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://github.com/Ajaycherayi/On-Wheel-Rent";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                Toast.makeText(About.this, "On Wheel Rent Source Source code", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }

}