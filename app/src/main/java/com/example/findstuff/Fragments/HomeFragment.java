package com.example.findstuff.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.findstuff.Adapters.itemAdapter;
import com.example.findstuff.Models.ObjectModel;
import com.example.findstuff.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView recyclerViewItems;
    itemAdapter itemAdapter;
    FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static Double slat, slongt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        recyclerViewItems = view.findViewById(R.id.itemRecView);
        LinearLayoutManager homeLayoutManager = new LinearLayoutManager(view.getContext());
        homeLayoutManager.setReverseLayout(true);
        homeLayoutManager.setStackFromEnd(true);
        recyclerViewItems.setLayoutManager(homeLayoutManager);
        String userId = mAuth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<ObjectModel> options =
                new FirebaseRecyclerOptions.Builder<ObjectModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("object"), ObjectModel.class)
                        .build();


        itemAdapter = new itemAdapter(options);
        recyclerViewItems.setAdapter(itemAdapter);

        /*fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        Dexter.withContext(view.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            slat = location.getLatitude();
                            slongt = location.getLongitude();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

        Toast.makeText(view.getContext(), "latitude" + slat, Toast.LENGTH_SHORT).show();
        Toast.makeText(view.getContext(), "longitude" + slongt, Toast.LENGTH_SHORT).show();*/




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }
}