package com.example.workatyourwill2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobsDone extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<JobDoneModel, NoteViewHolder> serviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_done);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        recyclerView=findViewById(R.id.recyclerView);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        Query query=firebaseFirestore.collection("workers").document(firebaseUser.getUid()).collection("jobsDone").orderBy("delivered_on", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<JobDoneModel> allServices=new FirestoreRecyclerOptions.Builder<JobDoneModel>().setQuery(query,JobDoneModel.class).build();
        serviceAdapter=new FirestoreRecyclerAdapter<JobDoneModel, NoteViewHolder>(allServices) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull JobDoneModel model) {


//                String docId=serviceAdapter.getSnapshots().getSnapshot(position).getId();

                String serv_title=model.getS_name();
                Toast.makeText(JobsDone.this,serv_title,Toast.LENGTH_SHORT).show();
//                String serv_desc=model.getS_desc();
//                String data="jflsjfse";
//                String businessName=model.getBusiness_name();
                String cost=model.getS_cost();
                Date date=model.getDelivered_on();
                holder.date.setText(date.toString());
                String customerId=model.getRequestFrom();
                DocumentReference documentReference=firebaseFirestore.collection("users").document(customerId);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String customerName=documentSnapshot.get("user_name")+"";
                        holder.customerName.setText(customerName);
                        GeoPoint lat_lang=(GeoPoint)documentSnapshot.get("lat_lng");
                        double lat=lat_lang.getLatitude();
                        double lng=lat_lang.getLongitude();
                        Geocoder geocoder;
                        List<Address> addresses=null;
                        geocoder = new Geocoder(JobsDone.this, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        }
                        catch (Exception e){
                            Toast.makeText(JobsDone.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        String address = addresses.get(0).getAddressLine(0)+""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality()+"";
                        String state = addresses.get(0).getAdminArea()+"";
//                        String country = addresses.get(0).getCountryName()+"";
                        String postalCode = addresses.get(0).getPostalCode()+"";
//                        String knownName = addresses.get(0).getFeatureName();
                        String customerAddr=address;
                        holder.address.setText(customerAddr);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JobsDone.this,"failed to get Values",Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(JobsDone.this,"Success",Toast.LENGTH_SHORT).show();
//                Date date_added=model.getDate_added();
//                String worker_id=model.getWorker_id();
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(position);
//                        String docId=documentSnapshot.getId();
//                        Toast.makeText(getContext(),docId,Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });


//                String rating=model.getS_rating()+"";
//                String bookings_month=model.getBookings_month()+"";
//                data= DateFormat.getDateInstance().format(date_added);
                holder.ServiceTitle.setText(serv_title);
//                holder.ServiceDesc.setText(serv_desc);

                holder.cost.setText(cost+"");
//                holder.rating.setText(rating);
//                holder.bookings.setText(bookings_month);
//                holder.businessName.setText(businessName);



//                String docId=serviceAdapter.getSnapshots().getSnapshot(position).getId();

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.job_done_item,parent,false);
                return new NoteViewHolder(view1);
            }
        };





//                String docId=serviceAdapter.getSnapshots().getSnapshot(position).getId();
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(serviceAdapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        serviceAdapter.startListening();
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        public TextView ServiceTitle;
        public TextView ServiceDesc;
        LinearLayout mNoteLayout;
        CardView cardView;
        ImageView popupButton;
        TextView businessName;
        TextView cost;
        TextView customerName;
        TextView address;
        TextView date;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ServiceTitle=itemView.findViewById(R.id.noteTitle);
            mNoteLayout=itemView.findViewById(R.id.noteLayout);
            cardView=itemView.findViewById(R.id.notecard);
            cost=itemView.findViewById(R.id.note_cost);
            popupButton=itemView.findViewById(R.id.menupopupButton);
            customerName=itemView.findViewById(R.id.customerName);
            address=itemView.findViewById(R.id.address);
            date=itemView.findViewById(R.id.date);


        }


    }
}