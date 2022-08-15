package com.example.workatyourwill2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FinishJob extends AppCompatActivity {
    ImageView call;
    TextView sName;
    TextView sCost;
    TextView sDesc;
    TextView custName;
    TextView address;
    TextView phone;
    Button finishJob;
    private static final int REQUEST_PHONE_CALL = 1;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_job);
        call=findViewById(R.id.call);
        sName=findViewById(R.id.noteTitle);
        sCost=findViewById(R.id.note_cost);
        sDesc=findViewById(R.id.et_service_desc);
        custName=findViewById(R.id.customerName);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.cust_phone);
        finishJob=findViewById(R.id.finishjob);
        Intent intent=getIntent();
        String docId=intent.getStringExtra("jobid");
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        JobDoneModel obj=new JobDoneModel();
        DocumentReference documentReference=firebaseFirestore.collection("jobs").document(docId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String servName=(String)documentSnapshot.get("s_name");

                sName.setText(servName);
                String s_cost=(String)documentSnapshot.get("s_cost");
                sCost.setText(s_cost);
                obj.s_name=servName;
                obj.s_cost=s_cost;
                obj.accepted=(boolean)documentSnapshot.get("accepted");
                obj.bookings_month=(String)documentSnapshot.get("bookings_month");
                obj.business_name=(String)documentSnapshot.get("business_name");

                Timestamp ts= (Timestamp) documentSnapshot.get("date");
                obj.date= ts.toDate();
                obj.estimateDelivery=(String)documentSnapshot.get("estimateDelivery");
                obj.orderStatus=(String)documentSnapshot.get("orderStatus");
                obj.requestFrom=(String)documentSnapshot.get("requestFrom");
                obj.requestTo=(String)documentSnapshot.get("requestTo");
                obj.s_rating=(String)documentSnapshot.get("s_rating");
                obj.s_desc=(String)documentSnapshot.get("s_desc");
                obj.serviceId=(String)documentSnapshot.get("serviceId");
                Log.d("xyz","Krishna");

                String s_desc=(String)documentSnapshot.get("s_desc");
                sDesc.setText(s_desc);
                String custId=(String)documentSnapshot.get("requestFrom");
                DocumentReference documentReference=firebaseFirestore.collection("users").document(custId);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String customerName=documentSnapshot.get("user_name")+"";
                        custName.setText(customerName);
//                        holder.customerName.setText(customerName);
                        String phNo=(String)documentSnapshot.get("ph_no");
                        phone.setText(phNo);
                        GeoPoint lat_lang=(GeoPoint)documentSnapshot.get("lat_lng");
                        double lat=lat_lang.getLatitude();
                        double lng=lat_lang.getLongitude();
                        Geocoder geocoder;
                        List<Address> addresses=null;
                        geocoder = new Geocoder(FinishJob.this, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        }
                        catch (Exception e){
                            Toast.makeText(FinishJob.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        String addresss = addresses.get(0).getAddressLine(0)+""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality()+"";
                        String state = addresses.get(0).getAdminArea()+"";
//                        String country = addresses.get(0).getCountryName()+"";
                        String postalCode = addresses.get(0).getPostalCode()+"";
//                        String knownName = addresses.get(0).getFeatureName();
                        String customerAddr=addresss;
                        address.setText(customerAddr);

//
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FinishJob.this,"failed to get Values",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        finishJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj.delivered_on=new Date();
                DocumentReference documentReference=firebaseFirestore.collection("jobs").document(docId);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(FinishJob.this,"Deleted",Toast.LENGTH_SHORT).show();
                        CollectionReference jobsDone=firebaseFirestore.collection("workers").document(obj.requestTo).collection("jobsDone");
                        jobsDone.add(obj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(FinishJob.this,"Added",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FinishJob.this,"Not Added",Toast.LENGTH_SHORT).show();
                            }
                        });

                        CollectionReference myOrders=firebaseFirestore.collection("users").document(obj.requestFrom).collection("myOrders");
                        myOrders.add(obj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(FinishJob.this,"Added2",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FinishJob.this,"Not Added2",Toast.LENGTH_SHORT).show();
                            }
                        });
                        DocumentReference updateBookings=firebaseFirestore.collection("workers").document(obj.requestTo).collection("services").document(obj.serviceId);
//                        final DocumentReference sfDocRef = db.collection("cities").document("SF");

                        firebaseFirestore.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(updateBookings);

                                // Note: this could be done without a transaction
                                //       by updating the population using FieldValue.increment()
                                long newPopulation = snapshot.getLong("s_bookings_per_month")+1 ;
                                transaction.update(updateBookings, "s_bookings_per_month", newPopulation);

                                // Success
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("transaction", "Transaction success!");
                                finish();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("transacton", "Transaction failure.", e);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FinishJob.this,"Not Deleted",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phone.getText().toString()));//change the number

                startActivity(callIntent);
            }
        });

    }


}