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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
CountryCodePicker ccp;
EditText PhNo;
Button getOTP;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
FirebaseFirestore firebaseFirestore;
ProgressDialog progressDialog;
TextView progresstext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ccp=(CountryCodePicker)findViewById(R.id.ccp);
        PhNo=findViewById(R.id.PhNo);
        getOTP=findViewById(R.id.getOTP);
        ccp.registerCarrierNumberEditText(PhNo);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            progressDialog=new ProgressDialog(MainActivity.this);

            progressDialog.show();

            progressDialog.setContentView(R.layout.progressbar);

            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progresstext=progressDialog.findViewById(R.id.progress_text);
            progresstext.setText("Signing in..");
            firebaseFirestore= FirebaseFirestore.getInstance();
            DocumentReference collectionReference=firebaseFirestore.collection("workers").document(firebaseUser.getUid());
            collectionReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(documentSnapshot.getData()!=null){
//                    Toast.makeText(Worker_Details.this,"Already present",Toast.LENGTH_SHORT).show();
                        CollectionReference coll_refer=firebaseFirestore.collection("workers").document(firebaseUser.getUid()).collection("services");
                        coll_refer.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int count =queryDocumentSnapshots.getDocuments().size();
                                if(count!=0){
//                    progressDialog.dismiss();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this,Worker_Home.class));

                                    finish();




                                }
                                else{
                                    progressDialog.dismiss();
                                    startActivity(new Intent(MainActivity.this,ServiceRegister.class));
                                    finish();
                                }
//                progressDialog.dismiss();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    else{
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this,Worker_Details.class));
                        finish();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });



        }
        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ManageOTP.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });
    }
}