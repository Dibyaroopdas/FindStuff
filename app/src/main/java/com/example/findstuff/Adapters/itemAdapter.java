package com.example.findstuff.Adapters;
import com.example.findstuff.Models.ObjectModel;
import com.example.findstuff.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class itemAdapter extends FirebaseRecyclerAdapter<ObjectModel,itemAdapter.myViewHolder>{

    //private FusedLocationProviderClient fusedLocationProviderClient;
    //Double slat,slongt;





    public itemAdapter(@NonNull FirebaseRecyclerOptions<ObjectModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public itemAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent,false);

        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final itemAdapter.myViewHolder holder, int position, @NonNull final ObjectModel model) {
        holder.name.setText(model.getName());
        holder.latitude.setText(model.getLatitude());
        holder.longitude.setText(model.getLongitude());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Intent intent = new Intent(v.getContext(), mapActivity.class);
                //intent.putExtra("latitude",model.getLatitude());
                //intent.putExtra("longitude",model.getLongitude());

                //v.getContext().startActivity(intent);

                //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(v.getContext());


                /*Dexter.withContext(v.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
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
                                Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();*/

                String uri = "http://maps.google.com/maps?saddr=&daddr=" + model.getLatitude() + "," + model.getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try
                {
                    v.getContext().startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        v.getContext().startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(v.getContext(), "Please install a maps application ", Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });
    }




    class myViewHolder extends RecyclerView.ViewHolder{

        /*CircleImageView img;*/
        TextView name,latitude,longitude;
        View v;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            /*img=(CircleImageView)itemView.findViewById(R.id.img1);*/
            //title=(TextView)itemView.findViewById(R.id.title);
            //description =(TextView)itemView.findViewById(R.id.description);
            //post = (ImageView)itemView.findViewById(R.id.post);
            //v = itemView;

            name = (TextView)itemView.findViewById(R.id.itemName);
            latitude = (TextView)itemView.findViewById(R.id.itemLatitude);
            longitude = (TextView)itemView.findViewById(R.id.itemLongitude);
            v = itemView;
        }
    }
}
