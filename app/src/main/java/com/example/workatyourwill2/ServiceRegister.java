package com.example.workatyourwill2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class ServiceRegister extends AppCompatActivity {

    EditText ser_name;
    EditText ser_desc;
    EditText ser_cost;
    Button add_service;
    TextView skip_add;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    TextView progresstext;
    String occupation="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_register);
        ser_name=findViewById(R.id.et_service_name);
        ser_desc=findViewById(R.id.et_service_desc);
        ser_cost=findViewById(R.id.et_service_cost);
        add_service=findViewById(R.id.addService);
        skip_add=findViewById(R.id.skipAddService);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        firebaseFirestore=FirebaseFirestore.getInstance();
        if(firebaseUser==null){
            startActivity(new Intent(ServiceRegister.this,MainActivity.class));
            finish();
        }

        String[] profession =new String[1];
        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=ser_name.getText().toString().trim();
                String desc=ser_desc.getText().toString().trim();

                int cost=Integer.parseInt(ser_cost.getText().toString());
                if(name.isEmpty()||desc.isEmpty()||ser_cost.getText().toString().isEmpty()){
                    Toast.makeText(ServiceRegister.this,"Fields are empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                DocumentReference docRef=firebaseFirestore.collection("workers").document(firebaseUser.getUid());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                         String prof =(String)documentSnapshot.get("profession");

                         Toast.makeText(ServiceRegister.this,prof,Toast.LENGTH_SHORT).show();
                        DocumentReference collectionReference=firebaseFirestore.collection("workers").document(firebaseUser.getUid()).collection("services").document();
                        Date date_added=new Date();
                        Service service=new Service(prof,date_added,name,desc,cost,firebaseUser.getUid());
                        collectionReference.set(service).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ServiceRegister.this,"Added Service Successfully.",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ServiceRegister.this,Worker_Home.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ServiceRegister.this,"Couldn't add Service!.",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ServiceRegister.this,"Couldn't get professtion!",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        skip_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceRegister.this,Worker_Home.class));
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
//        progressDialog=new ProgressDialog(ServiceRegister.this);
//
//        progressDialog.show();
//
//        progressDialog.setContentView(R.layout.progressbar);
//
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        progresstext=progressDialog.findViewById(R.id.progress_text);
//        progresstext.setText("Loading..");

    }
}