package com.example.findstuff;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class mapActivity extends AppCompatActivity {

    TextView mapLatitude,mapLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        mapLatitude = findViewById(R.id.mapLat);
        mapLongitude = findViewById(R.id.mapLongt);

        mapLongitude.setText(getIntent().getStringExtra("longitude"));
        mapLatitude.setText(getIntent().getStringExtra("latitude"));

    }


}