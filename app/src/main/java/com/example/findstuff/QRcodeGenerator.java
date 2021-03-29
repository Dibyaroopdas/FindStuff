package com.example.findstuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;

public class QRcodeGenerator extends AppCompatActivity {

    EditText tagText;
    ImageView genQrCode;
    Button genQRbtn;
    EditText nameText;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode_generator);

        tagText = findViewById(R.id.itemTagText);
        genQrCode = findViewById(R.id.qrCode);
        genQRbtn = findViewById(R.id.genQRbtn);
        nameText = findViewById(R.id.itemName);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();





        genQRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    HashMap<String,Object> map = new HashMap<>();
                    String tag = tagText.getText().toString().trim();
                    String name = nameText.getText().toString().trim();
                    String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String id = mRootRef.push().getKey();

                    map.put("name",name);

                    BitMatrix matrix = writer.encode(uID + tag + id, BarcodeFormat.QR_CODE,350,350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    genQrCode.setImageBitmap(bitmap);
                    InputMethodManager manager = (InputMethodManager) getSystemService(

                            Context.INPUT_METHOD_SERVICE
                    );
                    manager.hideSoftInputFromWindow(tagText.getApplicationWindowToken(),0);


                    mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("object").child(uID+tag+id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(QRcodeGenerator.this, "Successfully Added to Database" , Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QRcodeGenerator.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}