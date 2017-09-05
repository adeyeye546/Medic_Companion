package com.adeyeye.medicalcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.name;
import static com.adeyeye.medicalcompanion.R.string.address;

public class DoctorProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserReferrence;
    private String doctor_id;
    private Button mDocSave;
    private AppCompatEditText mDoctorName;
    private AppCompatEditText mDoctorAddress;
    private AppCompatEditText mDoctorEmail;
    private EditText mDoctorGender;
    private AppCompatEditText mDoctorPhone;
    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        session = new Session(this);
        if (!session.loggedIn()){
            logout();
        }
        initView();
        initModel();
    }
    private void logout(){
        session.setLoggedIn(false);
        finish();
        startActivity(new Intent(DoctorProfileActivity.this, LoginActivity.class));
    }

    private void initModel() {
        Start();
    }

    private void initView() {
        mDoctorName = (AppCompatEditText) findViewById(R.id.doctor_name);
        mDoctorAddress = (AppCompatEditText) findViewById(R.id.doctor_address);
        mDoctorEmail = (AppCompatEditText) findViewById(R.id.doctor_email);
        mDoctorGender = (EditText)findViewById(R.id.doctor_gender);
        mDoctorPhone = (AppCompatEditText)findViewById(R.id.phone_number);
        mDocSave = (Button) findViewById(R.id.doctor_save_button);

        mDocSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mDoctorName.getText().toString();
                final String address = mDoctorAddress.getText().toString();
                final String email = mDoctorEmail.getText().toString();
                final String gender = mDoctorGender.getText().toString();
                final String phoneNum = mDoctorPhone.getText().toString();
                DatabaseReference currentUser =
                        mUserReferrence.child(doctor_id);
                currentUser.child("name").setValue(name);
                currentUser.child("address").setValue(address);
                currentUser.child("email").setValue(email);
                currentUser.child("gender").setValue(gender);
                currentUser.child("phoneNumber").setValue(phoneNum);
                Intent mainIntent = new Intent(getApplicationContext(),
                        NavigationActivity
                                .class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });

    }
    public void Start() {
        mAuth = FirebaseAuth.getInstance();
        mUserReferrence = FirebaseDatabase.getInstance().getReference().child("doctors");
        //mStorage = FirebaseStorage.getInstance().getReference();
        doctor_id = mAuth.getCurrentUser().getUid();
    }

}
