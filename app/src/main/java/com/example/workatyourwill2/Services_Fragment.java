package com.example.workatyourwill2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Services_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Services_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> serviceAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    public Services_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Services_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Services_Fragment newInstance(String param1, String param2) {
        Services_Fragment fragment = new Services_Fragment();
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
        View view=inflater.inflate(R.layout.fragment_services_, container, false);
        fab=view.findViewById(R.id.fab);
        recyclerView=view.findViewById(R.id.recyclerView);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        Query query=firebaseFirestore.collection("workers").document(firebaseUser.getUid()).collection("services");

        FirestoreRecyclerOptions<FirebaseModel> allServices=new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();
        serviceAdapter=new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allServices) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull FirebaseModel model) {
                String serv_title=model.getS_name();
                String serv_desc=model.getS_desc();
                String data="jflsjfse";
                double cost=model.getS_cost();
                Date date_added=model.getDate_added();
                String rating=model.getS_rating()+"";
                String bookings_month=model.getS_bookings_per_month()+"";
                data= DateFormat.getDateInstance().format(date_added);
                holder.ServiceTitle.setText(serv_title);
                holder.ServiceDesc.setText(serv_desc);
                holder.dateAdded.setText(data);
                holder.cost.setText(cost+"");
                holder.rating.setText(rating);
                holder.bookings.setText(bookings_month);



                String docId=serviceAdapter.getSnapshots().getSnapshot(position).getId();

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
                return new NoteViewHolder(view1);
            }
        };
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(serviceAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(container.getContext(),ServiceRegister.class));

            }
        });
        return view;


    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        public TextView ServiceTitle;
        public TextView ServiceDesc;
        LinearLayout mNoteLayout;
        CardView cardView;
        ImageView popupButton;
        TextView dateAdded;
        TextView cost;
        TextView rating;
        TextView bookings;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            ServiceTitle=itemView.findViewById(R.id.noteTitle);
            ServiceDesc=itemView.findViewById(R.id.noteContent);
            mNoteLayout=itemView.findViewById(R.id.noteLayout);
            cardView=itemView.findViewById(R.id.notecard);
            dateAdded=itemView.findViewById(R.id.date);
            cost=itemView.findViewById(R.id.note_cost);
            popupButton=itemView.findViewById(R.id.menupopupButton);
            rating=itemView.findViewById(R.id.rating);
            bookings=itemView.findViewById(R.id.booking_month);


        }


    }

    @Override
    public void onStart() {
        super.onStart();
        serviceAdapter.startListening();
    }
}