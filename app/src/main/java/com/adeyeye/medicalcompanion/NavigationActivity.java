package com.adeyeye.medicalcompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserReference;
    private DatabaseReference mPatientReference;
    private String doctor_id;
    private Doctor doctor;
    private String patient_id;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Start();
        Play();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_disease) {
           Intent intent = new Intent(getApplicationContext(), Diseases_listActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_drug) {
            Intent intent = new Intent(getApplicationContext(), Drug_listActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Drug", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_healthtip) {
            //Fragment Object
            HealthTipFragment healthTipFragment = new HealthTipFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_for_fragment, healthTipFragment,
                    healthTipFragment.getTag()).commit();
            //Toast.makeText(this, "Health Tips", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_doctor) {
            Toast.makeText(this, "Doctor", Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(getApplicationContext(), BlankActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("edit", "editdoctor");
            bundle.putSerializable("model", doctor);
            mIntent.putExtras(bundle);
            startActivity(mIntent);
        }else if (id == R.id.nav_patient){
            Toast.makeText(this, "Patient", Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(getApplicationContext(), PatientProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("edit", "editpatient");
            bundle.putSerializable("model", patient);
            mIntent.putExtras(bundle);
            startActivity(mIntent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Start() {

        mAuth = FirebaseAuth.getInstance();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("doctors");
       // mUserReference = FirebaseDatabase.getInstance().getReference().child("patients");
        //mStorage = FirebaseStorage.getInstance().getReference();
        doctor_id = mAuth.getCurrentUser().getUid();
       // patient_id = mAuth.getCurrentUser().getUid();

        DatabaseReference
                userDbRef = FirebaseDatabase.getInstance().getReference("doctors").child(doctor_id);
      //  DatabaseReference userPbRef = FirebaseDatabase.getInstance().getReference("patients").child(patient_id);

        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                doctor = dataSnapshot.getValue(Doctor.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Main Activity", "loadname:onCancelled", databaseError.toException());
            }
        });
       /* userPbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patient = dataSnapshot.getValue(Patient.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Main Activity", "loadname:onCancelled", databaseError.toException());
            }
        });*/
    }
    public void Play(){
        mAuth = FirebaseAuth.getInstance();
        mPatientReference = FirebaseDatabase.getInstance().getReference().child("patients");

        patient_id = mAuth.getCurrentUser().getUid();


        DatabaseReference
                userDataRef = FirebaseDatabase.getInstance().getReference("patients").child(patient_id);
        //  DatabaseReference userPbRef = FirebaseDatabase.getInstance().getReference("patients").child(patient_id);
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patient = dataSnapshot.getValue(Patient.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Main Activity", "loadname:onCancelled", databaseError.toException());
            }
        });
    }
}
