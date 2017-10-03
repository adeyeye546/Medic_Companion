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
    private AppCompatEditText mPatientPhone;
    private Patient patient;
    private Patient mNewPatientModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        initModel();
        initView();
    }
    private void initModel() {
        start();

    }
    private void initView(){
        mPatientName = (AppCompatEditText)findViewById(R.id.patient_name);
        mPatientAddress = (AppCompatEditText) findViewById(R.id.patient_address);
        mPatientEmail = (AppCompatEditText)findViewById(R.id.patient_email);
        mPatientGender = (EditText)findViewById(R.id.patient_gender);
        mPatientDescription = (AppCompatEditText)findViewById(R.id.patient_description);
        mPatientPhone = (AppCompatEditText)findViewById(R.id.patient_phone_number);
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

               DatabaseReference currentPatient = mPatientReference.child(patient_id);
               currentPatient.child("name").setValue(name);
               currentPatient.child("address").setValue(address);
               currentPatient.child("email").setValue(email);
               currentPatient.child("gender").setValue(gender);
               currentPatient.child("description").setValue(description);
               currentPatient.child("phone").setValue(phone);

               Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
           }
       });

    }
    public void start(){
        mAuth = FirebaseAuth.getInstance();
        mPatientReference = FirebaseDatabase.getInstance().getReference().child("patients");
        patient_id = mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        if (intent.getStringExtra("add") != null){
            if (intent.getStringExtra("add").equals("addpatient")) {
                //  mView.SetUpAddDoctor();
            }
        } else if (intent.getStringExtra("edit") != null) {
            mNewPatientModel = (Patient) intent.getSerializableExtra("model");
            if (intent.getStringExtra("edit").equals("editdoctor")) {
                SetUpView(mNewPatientModel);
            }
    }
}
    private void SetUpView(Patient newPatientModel) {
        mPatientName.setText(newPatientModel.getName());
        mPatientAddress.setText(newPatientModel.getAddress());
        mPatientEmail.setText(newPatientModel.getEmail());
        mPatientGender.setText(newPatientModel.getGender());
        mPatientDescription.setText(newPatientModel.getDescription());
        mPatientPhone.setText(newPatientModel.getPhone());

    }
}
