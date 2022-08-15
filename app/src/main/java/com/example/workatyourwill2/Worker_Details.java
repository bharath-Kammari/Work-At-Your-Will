//package com.example.workatyourwill2;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Spinner;
//
//public class Worker_Details extends AppCompatActivity {
//AutoCompleteTextView autoCompleteTextView;
//Spinner spinner;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_worker__details);
//        autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
//        String[] professions=getApplication().getResources().getStringArray(R.array.professions);
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.profession_item,professions);
//        spinner=findViewById(R.id.search_spinner);
//
//    }
//}
/*
  Copyright 2017 Google Inc. All Rights Reserved.
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.example.workatyourwill2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.example.workatyourwill2.BuildConfig;
import com.example.workatyourwill2.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.Locale;

/**
 * Location sample.
 * <p>
 * Demonstrates use of the Location API to retrieve the last known location for a device.
 */
public class Worker_Details extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    AutoCompleteTextView autoCompleteTextView;

Spinner spinner;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private  Button addAddress;
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    Button signOut;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    GeoFirestore geoFirestore;
    double Latitude;
    double Longitude;
    int PLACE_PICKER_REQUEST=1;
    // Details
    String user_Phone;
    String user_FullName;
    String user_business;
    double user_Loc_Lat;
    double user_Loc_Long;
    String user_Profession;
    EditText editText_F_name;
    EditText editText_Bus_name;
    Button bt_Submit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker__details);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        addAddress=findViewById(R.id.addAddress);
        editText_F_name=findViewById(R.id.et_worker_name);
        editText_Bus_name=findViewById(R.id.et_worker_Org);
        autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
        bt_Submit=findViewById(R.id.bt_submit);
        user_Loc_Long=Double.MIN_VALUE;
        user_Loc_Lat=Double.MIN_VALUE;
        if (firebaseUser == null) {
            startActivity(new Intent(Worker_Details.this, MainActivity.class));
            finish();
        }
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

                                startActivity(new Intent(Worker_Details.this,Worker_Home.class));

                                finish();




                            }
                            else{
                                startActivity(new Intent(Worker_Details.this,ServiceRegister.class));
                                finish();
                            }
//                progressDialog.dismiss();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                            Toast.makeText(Worker_Details.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Worker_Details.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    getLastLocation();
                    Intent intent=new Intent(Worker_Details.this,AddAddress.class);
                    intent.putExtra("latitude",Latitude);
                    intent.putExtra("longitude",Longitude);
                    startActivityForResult(intent,100);
                }


//                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder.build(Worker_Details.this),PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
            }
        });


        user_Phone=firebaseUser.getPhoneNumber();



        String[] professions=getApplication().getResources().getStringArray(R.array.professions);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.profession_item,professions);
        autoCompleteTextView.setAdapter(arrayAdapter);
        user_Profession="";
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0)
                user_Profession=professions[i];
            }
        });




        CollectionReference documentReference = firebaseFirestore.collection("worker_locations");
        geoFirestore = new GeoFirestore(documentReference);
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(Worker_Details.this, MainActivity.class));
//                finish();
//            }
//        });
        bt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_FullName=editText_F_name.getText().toString();
                user_business=editText_Bus_name.getText().toString();
                user_Phone=firebaseUser.getPhoneNumber();



                if(!user_FullName.isEmpty()&&!user_business.isEmpty()&&!user_Phone.isEmpty()&&!user_Profession.equals("")&&user_Loc_Lat!=Double.MIN_VALUE&&user_Loc_Long!=Double.MIN_VALUE){
                    DocumentReference documentReference1=firebaseFirestore.collection("workers").document(firebaseUser.getUid());
                    Worker worker=new Worker(firebaseUser.getUid(),user_FullName,user_business,user_Phone,new GeoPoint(user_Loc_Lat,user_Loc_Long),user_Profession);
                    documentReference1.set(worker).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Worker_Details.this,"Submitted Succesfully.",Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(Worker_Details.this,ServiceRegister.class));

                        }
                    });

                }
                else{
                    Toast.makeText(Worker_Details.this,"Fields are emtpy!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            geoFirestore.setLocation(firebaseUser.getUid(), new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                            Latitude=mLastLocation.getLatitude();
                            Longitude=mLastLocation.getLongitude();
                            Toast.makeText(Worker_Details.this,mLastLocation.getLatitude()+","+mLastLocation.getLongitude(),Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){
                Place place=PlacePicker.getPlace(data,this);
                StringBuilder stringBuilder=new StringBuilder();
                String latitude=String.valueOf(place.getLatLng().latitude);
                String longitude=String.valueOf(place.getLatLng().longitude);
                stringBuilder.append(latitude+","+longitude);
                Toast.makeText(Worker_Details.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();


            }
        }
        else if(requestCode==100){
            if(resultCode==RESULT_OK){
                user_Loc_Lat=data.getDoubleExtra("latitude",0);
                user_Loc_Long=data.getDoubleExtra("longitude",0);
                Toast.makeText(Worker_Details.this,user_Loc_Lat+","+user_Loc_Long,Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Return the current state of the permissions needed.
     */

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(Worker_Details.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}