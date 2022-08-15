package com.example.workatyourwill2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class ManageOTP extends AppCompatActivity {
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four,otp_textbox_five,otp_textbox_six;
Button verifyOTP;
String PhNo;
FirebaseAuth mAuth;
FirebaseFirestore firebaseFirestore;
FirebaseUser firebaseUser;

String otpID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_o_t_p);
        otp_textbox_one=findViewById(R.id.otp_edit_box1);
        otp_textbox_two=findViewById(R.id.otp_edit_box2);
        otp_textbox_three=findViewById(R.id.otp_edit_box3);
        otp_textbox_four=findViewById(R.id.otp_edit_box4);
        otp_textbox_five=findViewById(R.id.otp_edit_box5);
        otp_textbox_six=findViewById(R.id.otp_edit_box6);
        verifyOTP=findViewById(R.id.verifyOTP);
        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four,otp_textbox_five,otp_textbox_six};

        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));
        otp_textbox_five.addTextChangedListener(new GenericTextWatcher(otp_textbox_five, edit));
        otp_textbox_six.addTextChangedListener(new GenericTextWatcher(otp_textbox_six, edit));
        Intent intent=getIntent();
        mAuth=FirebaseAuth.getInstance();
       PhNo= intent.getStringExtra("mobile").toString();
        initiateOTP();
        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c1=otp_textbox_one.getText().toString();
                String c2=otp_textbox_two.getText().toString();
                String c3=otp_textbox_three.getText().toString();
                String c4=otp_textbox_four.getText().toString();
                String c5=otp_textbox_five.getText().toString();
                String c6=otp_textbox_six.getText().toString();
                String enteredOTP=c1+c2+c3+c4+c5+c6;
                if(c1.isEmpty()||c2.isEmpty()||c3.isEmpty()||c4.isEmpty()||c5.isEmpty()||c6.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter OTP",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpID,enteredOTP);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });



    }
    private void initiateOTP(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpID=s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });        // OnVerificationStateChangedCallbacks

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser=mAuth.getCurrentUser();
                            firebaseFirestore=FirebaseFirestore.getInstance();
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

                                                    startActivity(new Intent(ManageOTP.this,Worker_Home.class));

                                                    finish();




                                                }
                                                else{
                                                    startActivity(new Intent(ManageOTP.this,ServiceRegister.class));
                                                    finish();
                                                }
//                progressDialog.dismiss();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                                                Toast.makeText(ManageOTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ManageOTP.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });


                            startActivity(new Intent(ManageOTP.this,Worker_Details.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"SignIn Code error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}