package com.example.findstuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.findstuff.Fragments.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_LOCATION_PERMISSION = 44 ;
    private BottomNavigationView bottomNavigationView;
    private ImageView scanQR;
    public static Double slat,slongt;

    private Fragment selectorFragment;

    ActionBar actionBar;

    private FusedLocationProviderClient fusedLocationProviderClient;
    Double lat,longt;
    public static String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background));


        /*bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);*/

        selectorFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();



        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.orange));
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && text != null) {
                            lat = location.getLatitude();
                            longt = location.getLongitude();

                            findById(text,lat,longt);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.generateQR:
                startActivity(new Intent(getApplicationContext(),QRcodeGenerator.class));
                break;
            case R.id.logOut:
                Toast.makeText(this, "This is the log out button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.scanQR:
                startActivity(new Intent(getApplicationContext(),scannerView.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void findById(final String text, final Double lat, final Double longt) {
        Toast.makeText(MainActivity.this, "Hello1", Toast.LENGTH_SHORT).show();
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mObjectRef = FirebaseDatabase.getInstance().getReference();
        //Query query = mFirebaseDatabaseReference.child("Users");
        String id = mObjectRef.push().getKey();
        String uID = text.substring(0,28);

        HashMap<String, Object> map = new HashMap<>();
        map.put("latitude", lat);
        map.put("longitude", longt);

        Toast.makeText(MainActivity.this, "The latitude is : " + lat, Toast.LENGTH_SHORT).show();


        mObjectRef.child("Users").child(uID).child("object").child(text).child("latitude").setValue(lat.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(MainActivity.this, "Scan Successful", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mObjectRef.child("Users").child(uID).child("object").child(text).child("longitude").setValue(longt.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(MainActivity.this, "Scan Successful", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
