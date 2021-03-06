package com.adeyeye.medicalcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mPatientReference;
    private String patient_id;
    private Button mPatientSave;
    private AppCompatEditText mPatientName;
    private AppCompatEditText mPatientAddress;
    private AppCompatEditText mPatientEmail;
    private EditText mPatientGender;
    private AppCompatEditText mPatientDescription;
    private AppCompatEditText mUsername;
    private AppCompatEditText mPassword;
    private AppCompatEditText mPatientPhone;
    private Patient patient;
    private PatientsModel mNewPatientModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        initView();
        initModel();
    }
    private void initModel() {
        Start();

    }
    private void initView(){
        mPatientName = (AppCompatEditText)findViewById(R.id.patient_name);
        mPatientAddress = (AppCompatEditText) findViewById(R.id.patient_address);
        mPatientEmail = (AppCompatEditText)findViewById(R.id.patient_email);
        mPatientGender = (EditText)findViewById(R.id.patient_gender);
        mPatientDescription = (AppCompatEditText)findViewById(R.id.patient_description);
        mPatientPhone = (AppCompatEditText)findViewById(R.id.patient_phone_number);
        mPassword = (AppCompatEditText)findViewById(R.id.patient_password);
        mUsername = (AppCompatEditText)findViewById(R.id.patient_username);
        mPatientSave = (Button) findViewById(R.id.patient_save_button);

       mPatientSave.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View v) {
               final String name = mPatientName.getText().toString();
               final String address = mPatientAddress.getText().toString();
               final String email = mPatientEmail.getText().toString();
               final String gender = mPatientGender.getText().toString();
               final String description = mPatientDescription.getText().toString();
               final String phone = mPatientPhone.getText().toString();
               final String username = mUsername.getText().toString();
               final String password = mPassword.getText().toString();
               String user_id = mAuth.getCurrentUser().getUid();

               DatabaseReference currentPatient = mPatientReference.child(username);
               currentPatient.child("doctorId").setValue(user_id);
               currentPatient.child("name").setValue(name);
               currentPatient.child("address").setValue(address);
               currentPatient.child("password").setValue(password);
               currentPatient.child("username").setValue(password);
               currentPatient.child("patientId").setValue(username);
               currentPatient.child("email").setValue(email);
               currentPatient.child("gender").setValue(gender);
               currentPatient.child("description").setValue(description);
               currentPatient.child("phone").setValue(phone);
               finish();
           }
       });

    }


    public void Start(){
        mAuth = FirebaseAuth.getInstance();
        mPatientReference = FirebaseDatabase.getInstance().getReference().child("patients");
        patient_id = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        if (intent.getStringExtra("add") != null){
            if (intent.getStringExtra("add").equals("addpatient")) {
                //  mView.SetUpAddDoctor();
            }
        } else if (intent.getStringExtra("edit") != null) {
            mNewPatientModel = (PatientsModel) intent.getSerializableExtra("model");
            if (intent.getStringExtra("edit").equals("editPatient")) {
                SetUpView(mNewPatientModel);
            }
    }
}
    private void SetUpView(PatientsModel newPatientModel) {
        mPatientName.setText(newPatientModel.getName());
        mPatientAddress.setText(newPatientModel.getAddress());
        mPatientEmail.setText(newPatientModel.getEmail());
        mPatientGender.setText(newPatientModel.getGender());
        mPatientDescription.setText(newPatientModel.getDescription());
        mPatientPhone.setText(newPatientModel.getPhone());
        //mUsername.setText(newPatientModel);
        mPassword.setText(newPatientModel.getPassword());

    }
}
