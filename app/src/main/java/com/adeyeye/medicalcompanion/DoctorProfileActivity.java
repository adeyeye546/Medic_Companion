package com.adeyeye.medicalcompanion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.description;
import static android.R.attr.name;
import static com.adeyeye.medicalcompanion.R.string.address;
import static com.adeyeye.medicalcompanion.R.string.doctor_name;
import static com.adeyeye.medicalcompanion.RegisterActivity.USER_PREFS;

public class DoctorProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserReference;
    private String doctor_id;
    private Button mDocSave;
    private AppCompatEditText mPersonalInformation;
    private AppCompatEditText mDoctorAddress;
    private AppCompatEditText mEducation;
    private EditText mDoctorGender;
    private AppCompatEditText mDoctorPhone;
    private AppCompatEditText mEmail;
    private AppCompatEditText mHistory;
    private AppCompatEditText mName;
    private Session session;
    private Doctor doctor;
    private Doctor mNewDoctorModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        session = new Session(this);

        initView();
        initModel();

    }
    private void logout(){
        session.setLoggedIn(false);
       // startActivity(new Intent(DoctorProfileActivity.this, NavigationActivity.class));

        finish();

    }


    private void initModel() {
        Start();
    }

    private void initView() {

        mPersonalInformation = (AppCompatEditText) findViewById(R.id.personal_information);


        mDoctorAddress = (AppCompatEditText) findViewById(R.id.doctor_address);
        mEducation = (AppCompatEditText) findViewById(R.id.education);
        mDoctorGender = (EditText)findViewById(R.id.doctor_gender);
        mDoctorPhone = (AppCompatEditText)findViewById(R.id.phone_number);
        mHistory = (AppCompatEditText)findViewById(R.id.doctor_work_history);
        mName = (AppCompatEditText)findViewById(R.id.doctor_name);
        mEmail = (AppCompatEditText)findViewById(R.id.doctor_email);
        mDocSave = (Button) findViewById(R.id.doctor_save_button);


        mDocSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String information = mPersonalInformation.getText().toString();
                final String address = mDoctorAddress.getText().toString();
                final String education = mEducation.getText().toString();
                final String gender = mDoctorGender.getText().toString();
                final  String history = mHistory.getText().toString();
                final String phoneNum = mDoctorPhone.getText().toString();
                final String email = mEmail.getText().toString();
                final String name = mName.getText().toString();
                DatabaseReference currentUser =
                        mUserReference.child(doctor_id);
                currentUser.child("information").setValue(information);
                currentUser.child("name").setValue(name);
                currentUser.child("email").setValue(email);
                currentUser.child("address").setValue(address);
                currentUser.child("education").setValue(education);
                currentUser.child("gender").setValue(gender);
                currentUser.child("history").setValue(history);
                currentUser.child("phoneNumber").setValue(phoneNum);
                if (!session.loggedIn()){
                    startActivity(new Intent(DoctorProfileActivity.this, NavigationActivity.class));
                    logout();
                }

                Intent mainIntent = new Intent(getApplicationContext(),BlankActivity
                                .class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

            }
        });

    }



    public void Start() {

        mAuth = FirebaseAuth.getInstance();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("doctors");
        //mStorage = FirebaseStorage.getInstance().getReference();
        doctor_id = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        if (intent.getStringExtra("add") != null) {
            if (intent.getStringExtra("add").equals("adddoctor")) {
              //  mView.SetUpAddDoctor();
            }
        } else if (intent.getStringExtra("edit") != null) {
            mNewDoctorModel = (Doctor) intent.getSerializableExtra("model");
            if (intent.getStringExtra("edit").equals("editdoctor")) {
               SetUpView(mNewDoctorModel);
            }
        }
    }

    private void SetUpView(Doctor newDoctorModel) {
        mName.setText(newDoctorModel.getName());
        mHistory.setText(newDoctorModel.getHistory());
        mDoctorPhone.setText(newDoctorModel.getPhoneNumber());
        mEducation.setText(newDoctorModel.getEducation());
        mDoctorAddress.setText(newDoctorModel.getAddress());
        mDoctorGender.setText(newDoctorModel.getGender());
        mPersonalInformation.setText(newDoctorModel.getInformation());
        mEmail.setText(newDoctorModel.getEmail());
    }

}
