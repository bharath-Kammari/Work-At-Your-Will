package com.example.workatyourwill2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirestoreRecyclerAdapter<RequestModel, NoteViewHolder> serviceAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
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

        View view= inflater.inflate(R.layout.fragment_home_, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        recyclerView=view.findViewById(R.id.recyclerView);
        Log.d("userId",firebaseUser.getUid());

        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        Query query=firebaseFirestore.collection("requests").whereEqualTo("requestTo",firebaseUser.getUid());

        FirestoreRecyclerOptions<RequestModel> allServices=new FirestoreRecyclerOptions.Builder<RequestModel>().setQuery(query,RequestModel.class).build();

        serviceAdapter=new FirestoreRecyclerAdapter<RequestModel, NoteViewHolder>(allServices) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull RequestModel model) {


                String docId=serviceAdapter.getSnapshots().getSnapshot(position).getId();

                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        JobModel job=new JobModel(model.isAccepted(),model.getBookings_month(),model.getBusiness_name(),model.getDate(),"22 22 2222","active",model.getRequestFrom(),model.getRequestTo(),model.getS_cost(),model.getS_desc(),model.getS_name(),model.getS_rating(),model.getServiceId());

                        DialogFragment newFragment = new DatePickerFragment(docId,job);
                        newFragment.show(getFragmentManager(), "datePicker");

                    }
                });
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                String serv_title=model.getS_name();
                Toast.makeText(getContext(),serv_title,Toast.LENGTH_SHORT).show();
//                String serv_desc=model.getS_desc();
//                String data="jflsjfse";
//                String businessName=model.getBusiness_name();
                String cost=model.getS_cost();
                Date date=model.getDate();
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

                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
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
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.request,parent,false);
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
        public TextView ServiceDesc;
        LinearLayout mNoteLayout;
        CardView cardView;
        ImageView popupButton;
        TextView businessName;
        TextView cost;
        TextView rating;
        TextView bookings;
        TextView customerName;
        TextView address;
        TextView date;
        Button accept;
        Button reject;
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
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);

        }


    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        String docId;
        JobModel job;
        DatePickerFragment(String docId,JobModel job){
            super();
            this.docId=docId;
            this.job=job;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String estDate=day+","+month+","+year;
            job.estimateDelivery=estDate;
            job.orderStatus="active";
            DocumentReference documentReference=FirebaseFirestore.getInstance().collection("requests").document(docId);
            CollectionReference documentReference1=FirebaseFirestore.getInstance().collection("jobs");
            documentReference1.add(job).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("job added","djosjfoew");
                }
            });

            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    Toast.makeText(getActivity(),"Deleted Successfully",Toast.LENGTH_SHORT).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getActivity(),"Internet problem",Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}