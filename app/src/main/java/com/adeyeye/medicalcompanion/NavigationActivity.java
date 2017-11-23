package com.adeyeye.medicalcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mUserReference;
    private DatabaseReference mPatientReference;
    private String doctor_id;
    private Doctor doctor;
    private String patient_id;
    private Patient patient;
    private PatientListAdapter mPatientAdapter;
    private RecyclerView mRecyClerView;
    LinearLayoutManager mLinLayoutManager;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private List<PatientsModel> patientList;
    ArrayList<PatientsModel> entries = new ArrayList<>();
    private String PatientKey;
    private Query queryRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        initView();
        initModel();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               OpenAddPatient();
            }
        });
        mRecyClerView = (RecyclerView) findViewById(R.id.rec_patient);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void OpenAddPatient() {
        Intent mIntent = new Intent(getApplicationContext(), PatientProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("add", "addpatient");
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }

    public void SetUpPatientListView(List<PatientsModel> mPatientList) {
        mPatientAdapter= new PatientListAdapter(getApplicationContext(), mPatientList, new PatientListListener() {
            @Override
            public void OnItemClick(PatientsModel model, int p) {
               GetDaialogBox(model);
            }

            @Override
            public void OnItemEcgClick(PatientsModel model, int p) {

        }

    });
        mLinLayoutManager = new LinearLayoutManager(this);
        mRecyClerView.setLayoutManager(mLinLayoutManager);
        mRecyClerView.setAdapter(mPatientAdapter);
    }

    public void GetPatientList() {
        doctor_id = mAuth.getCurrentUser().getUid();
        DatabaseReference userDbRef = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://mediccompanionandroid.firebaseio.com/patients");
        queryRef = userDbRef.orderByChild("doctorId").equalTo(doctor_id);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PatientsModel patients = dataSnapshot.getValue(PatientsModel.class);
                Iterator<DataSnapshot>
                    items = dataSnapshot.getChildren().iterator();
                Toast.makeText(getApplicationContext(), "Total Users : " + dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                if (entries != null){
                    entries.clear();
                }
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    PatientKey =   item.getKey();
                    PatientsModel user = item.getValue(PatientsModel.class);
                    entries.add(user);
                }
                SetUpPatientListView(entries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Main Activity", "loadname:onCancelled", databaseError.toException());
            }
        });
    }

    public void OpenEditPatient(PatientsModel model) {
        Intent mIntent = new Intent(getApplicationContext(), PatientProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("edit", "editPatient");
        bundle.putSerializable("model",model);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
    }

    private void initModel() {
        Start();
        GetPatientList();
       // Play();
    }

    private void GetDaialogBox(final PatientsModel model){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.custom_menu, null);
        deleteDialogView.findViewById(R.id.edt_patient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenEditPatient(model);
            }
        });
        deleteDialogView.findViewById(R.id.diag_patience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent mIntent = new Intent(getApplicationContext(), PatientDiseaseDescription.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("add", "addform");
                    bundle.putSerializable("patient",model);
                    mIntent.putExtras(bundle);
                    startActivity(mIntent);

            }
        });

        deleteDialogView.findViewById(R.id.Check_patient_diag_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PatientFormListActivity.class);
                intent.putExtra("model",model);
                startActivity(intent);

            }
        });
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        deleteDialog.show();
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
            Intent mIntent = new Intent(getApplicationContext(), DoctorProfileActivity.class);
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
        } else if (id == R.id.nav_logout) {
            doLogOut();

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(NavigationActivity.this, PatientNavigationActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void showAlertDialogueLogOut(){
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Logout");
        dialog.setMessage("Successfully loged out");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent moveToMain = new Intent(getApplicationContext(), LoginActivity.class);
                moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                moveToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                moveToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToMain);
                finish();
            }
        });
        dialog.show();
    }

    private void doLogOut() {
        showAlertDialogueLogOut();
    }

    public void Start() {

        mAuth = FirebaseAuth.getInstance();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("doctors");
        doctor_id = mAuth.getCurrentUser().getUid();
       // patient_id = mAuth.getCurrentUser().getUid();

        DatabaseReference
                userDbRef = FirebaseDatabase.getInstance().getReference("doctors").child(doctor_id);

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
