package com.example.workatyourwill2;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Jobs_done_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Jobs_done_Fragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<JobModel, NoteViewHolder> serviceAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Jobs_done_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Jobs_done_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Jobs_done_Fragment newInstance(String param1, String param2) {
        Jobs_done_Fragment fragment = new Jobs_done_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_jobs_done_, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        recyclerView=view.findViewById(R.id.recyclerView_2);
        Log.d("userId",firebaseUser.getUid());

        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        Query query=firebaseFirestore.collection("jobs").whereEqualTo("requestTo",firebaseUser.getUid());
        Log.d("success","dfojd");
        FirestoreRecyclerOptions<JobModel> allServices=new FirestoreRecyclerOptions.Builder<JobModel>().setQuery(query,JobModel.class).build();
        Log.d("success","kdjfowjfe");
        serviceAdapter=new FirestoreRecyclerAdapter<JobModel, NoteViewHolder>(allServices) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull JobModel model) {
                String docId=getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(),FinishJob.class);
                        intent.putExtra("jobid",docId);
                        startActivity(intent);
                    }
                });

                holder.ServiceTitle.setText(model.getS_name());
                holder.cost.setText(model.getS_cost());
                String custId=model.getRequestFrom();
                Date date=model.getDate();
                String orderDate= DateFormat.getDateInstance().format(date);
                holder.date.setText(orderDate);
                String estDelivery=model.getEstimateDelivery();
//                Log.d("est",estDelivery);
                String[] arr=estDelivery.split(",");

                switch(Integer.parseInt(arr[1])){
                    case 1-1:arr[1]="Jan";
                    break;
                    case 2-1:arr[1]="Feb";
                    break;
                    case 3-1:arr[1]="Mar";
                        break;
                    case 4-1:arr[1]="Apr";
                        break;
                    case 5-1:arr[1]="May";
                        break;
                    case 6-1:arr[1]="Jun";
                        break;
                    case 7-1:arr[1]="Jul";
                        break;
                    case 8-1:arr[1]="Aug";
                        break;
                    case 9-1:arr[1]="Sep";
                        break;
                    case 10-1:arr[1]="Oct";
                        break;
                    case 11-1:arr[1]="Nov";
                        break;
                    case 12-1:arr[1]="Dec";
                        break;
                }
                holder.estDelivery.setText(arr[0]+" "+arr[1]+" "+arr[2]);

                DocumentReference documentReference=firebaseFirestore.collection("users").document(custId);
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
                        geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        }
                        catch (Exception e){
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(),"failed to get Values",Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.pendingjob,parent,false);
                return new NoteViewHolder(view1);
            }
        };





//                String docId=serviceAdapter.getSnapshots().getSnapshot(position).getId();
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(serviceAdapter);



        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        serviceAdapter.startListening();
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        public TextView ServiceTitle;

        LinearLayout mNoteLayout;
        CardView cardView;
        ImageView popupButton;

        TextView cost;

        TextView customerName;
        TextView address;
        TextView date;
        TextView estDelivery;
        TextView orderStatus;
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
            estDelivery=itemView.findViewById(R.id.est_delivery);
            orderStatus=itemView.findViewById(R.id.status);

        }


    }
}