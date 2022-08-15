package com.example.workatyourwill2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Worker_Home extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker__home);
        bottomNavigationView=findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Home_Fragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.home:
                        fragment=new Home_Fragment();
                        break;
                    case R.id.profile:
                        fragment=new Profile_fragment();
                        break;
                    case R.id.Services:
                        fragment=new Services_Fragment();
                        break;
                    case R.id.Jobs_done:
                        fragment=new Jobs_done_Fragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        int pid = android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
    }
}
//signOut.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(WorkerHome.this, MainActivity.class));
//        finish();
//        }
//        });