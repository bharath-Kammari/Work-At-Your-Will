package com.example.workatyourwill2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.io.IOException;
import java.util.List;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;
//import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;

public class AddAddress extends FragmentActivity implements OnMapReadyCallback{
    private static final String TAG = MainActivity.class.getSimpleName();
    protected Location mLastLocation;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
GoogleMap map;
Button confirm_location;
SupportMapFragment mapFragment;
SearchView searchView;
ListView listView;
String[] list;
StringBuilder sb;
Marker nm;
boolean map_Ready_flag=false;
double latitude;
double longitude;
    Intent intent;
    MarkerOptions marker;
//    Geocoder geocoder=new Geocoder(AddAddress.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        searchView=findViewById(R.id.search_location);
        listView=findViewById(R.id.listView);
        confirm_location=findViewById(R.id.confirm_location);

        searchView.setFocusable(true);
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        intent=getIntent();
        latitude=intent.getDoubleExtra("latitude",0);
        longitude=intent.getDoubleExtra("longitude",0);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


//        listView.setVisibility(View.INVISIBLE);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location=searchView.getQuery().toString();
                List<Address> addresses=null;
                map.clear();

                if(location!=null&&!location.equals("")){
                    Geocoder geocoder=new Geocoder(AddAddress.this);
                    try {
                        addresses=geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addresses.size()>=1) {
                        Address address = addresses.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        if(nm!=null){
                            nm.remove();
                        }
                        marker =new MarkerOptions().position(latLng).title(location).draggable(true);

                        nm=map.addMarker(marker);

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                        listView.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Toast.makeText(AddAddress.this,"HI",Toast.LENGTH_SHORT).show();
                    }
//                        addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

//                        String addressline = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName();


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String location=s;
                List<Address> addresses=null;
                Geocoder geocoder=new Geocoder(AddAddress.this);
                listView.setVisibility(View.VISIBLE);
                if(location!=null&&!location.equals("")){

                    try {
                        addresses=geocoder.getFromLocationName(location,5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int n=0;
                    if(addresses.size()<5){
                        n=addresses.size();
                    }
                    else{
                        n=5;
                    }

                    list=new String[n];
                    sb=new StringBuilder();
//                        addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    for(int i=0;i<n;i++) {
                        sb.append(addresses.get(i).getAddressLine(0)); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        sb.append(","+addresses.get(i).getAddressLine(0));


                        sb.append(","+addresses.get(i).getLocality());
                        sb.append(","+addresses.get(i).getAdminArea());
                        sb.append(","+addresses.get(i).getCountryName());
                        sb.append(","+addresses.get(i).getPostalCode());
                        sb.append(","+addresses.get(i).getFeatureName());
                        list[i]=sb.toString();
                        sb.setLength(0);


                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AddAddress.this, R.layout.simple_list_item,list);
//                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                    List<Address> finalAddresses = addresses;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                        addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            if(finalAddresses!=null&&finalAddresses.size()>i) {
                                Address address = finalAddresses.get(i);
                                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                if(nm!=null){
                                    nm.remove();
                                }
                                marker =new MarkerOptions().position(latLng).title(location).draggable(true).title(address.getLocality());
                                nm=map.addMarker(marker);
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                                listView.setVisibility(View.GONE);

                            }
                        }
                    });


                }
                return false;
            }
        });
        mapFragment.getMapAsync(this);
        confirm_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng=nm.getPosition();
                intent.putExtra("latitude",latLng.latitude);
                intent.putExtra("longitude",latLng.longitude);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if(requestCode==100&&resultCode==RESULT_OK){
////            Place place=Autocomplete.getPlaceFromIntent(data);
////            searchView.setText(place.getAddress());
////            LatLng latLng=place.getLatLng();
////            map.addMarker(new MarkerOptions().position(latLng).title(searchView.getText().toString()));
////            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
////
////        }
////        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
////            Status status=Autocomplete.getStatusFromIntent(data);
////            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
////        }
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//
//    }
//    private boolean checkPermissions() {
//        int permissionState = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION);
//        return permissionState == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void startLocationPermissionRequest() {
//        ActivityCompat.requestPermissions(AddAddress.this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                REQUEST_PERMISSIONS_REQUEST_CODE);
//    }
//
//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//
//            showSnackbar(R.string.permission_rationale, android.R.string.ok,
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            startLocationPermissionRequest();
//                        }
//                    });
//
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            startLocationPermissionRequest();
//        }
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        Log.i(TAG, "onRequestPermissionResult");
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.length <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.");
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted.
//                getLastLocation();
//            } else {
//                // Permission denied.
//
//                // Notify the user via a SnackBar that they have rejected a core permission for the
//                // app, which makes the Activity useless. In a real app, core permissions would
//                // typically be best requested during a welcome-screen flow.
//
//                // Additionally, it is important to remember that a permission might have been
//                // rejected without asking the user for permission (device policy or "Never ask
//                // again" prompts). Therefore, a user interface affordance is typically implemented
//                // when permissions are denied. Otherwise, your app could appear unresponsive to
//                // touches or interactions which have required permissions.
//                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Build intent that displays the App settings screen.
//                                Intent intent = new Intent();
//                                intent.setAction(
//                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package",
//                                        BuildConfig.APPLICATION_ID, null);
//                                intent.setData(uri);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        });
//            }
//        }
//    }
//    /**
//     * Shows a {@link Snackbar}.
//     *
//     * @param mainTextStringId The id for the string resource for the Snackbar text.
//     * @param actionStringId   The text of the action item.
//     * @param listener         The listener associated with the Snackbar action.
//     */
//    private void showSnackbar(final int mainTextStringId, final int actionStringId,
//                              View.OnClickListener listener) {
//        Snackbar.make(findViewById(android.R.id.content),
//                getString(mainTextStringId),
//                Snackbar.LENGTH_INDEFINITE)
//                .setAction(getString(actionStringId), listener).show();
//    }
//    @SuppressWarnings("MissingPermission")
//    private void getLastLocation() {
//        mFusedLocationClient.getLastLocation()
//                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            mLastLocation = task.getResult();
//                            latitude=mLastLocation.getLatitude();
//                            longitude=mLastLocation.getLongitude();
////                            geoFirestore.setLocation(firebaseUser.getUid(), new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
//                            Toast.makeText(AddAddress.this,mLastLocation.getLatitude()+","+mLastLocation.getLongitude(),Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            Log.w(TAG, "getLastLocation:exception",task.getException());
//                            showSnackbar(getString(R.string.no_location_detected));
//                            setResult(RESULT_CANCELED,intent);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * Shows a {@link Snackbar} using {@code text}.
//     *
//     * @param text The Snackbar text.
//     */
//    private void showSnackbar(final String text) {
//        View container = findViewById(R.id.main_activity_container);
//        if (container != null) {
//            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
//        }
//    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map=googleMap;
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                nm=marker;
            }
        });
        map_Ready_flag=true;
//        getLastLocation();
        LatLng latLng = new LatLng(latitude, longitude);
        if(nm!=null){
            nm.remove();
        }

        marker =new MarkerOptions().position(latLng).title("Your current location").draggable(true);

        nm = map.addMarker(marker);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

    }


}