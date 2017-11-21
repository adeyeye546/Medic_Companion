package com.adeyeye.medicalcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    // Constants
    public static final String USER_PREFS = "UserPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    // private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button SignUp;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initModel();

    }

    private void initView() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        SignUp = (Button) findViewById(R.id.register_sign_up_button);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed." + task.getException(),
                                           Toast.LENGTH_SHORT).show();
                        } else {
                            checkUserExist(mAuth,mDatabaseUsers);

                        }

                    }

                });
            }
        });
    }

    private void initModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("doctors");
        mDatabaseUsers.keepSynced(true);
    }


    private void checkUserExist(FirebaseAuth mAuth,DatabaseReference mDatabaseUsers) {

        if (mAuth.getCurrentUser() != null) {
            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild(user_id)) {

                        Intent mainIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                       "You Need to setup your account",
                                       Toast.LENGTH_LONG).show();
                        Intent setupIntent =
                            new Intent(getApplicationContext(), DoctorProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("add", "adddoctor");
                        setupIntent.putExtras(bundle);
                        setupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        // public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //     // On selecting a spinner item
        //     String item = parent.getItemAtPosition(position).toString();
        //
        //     // Showing selected spinner item
        //     Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        // }
        // public void onNothingSelected(AdapterView<?> arg0) {
        //     // TODO Auto-generated method stub
        // }
        //
        // // Executed when Sign Up button is pressed.
        // public void signUp(View v) {
        //     attemptRegistration();
        // }
        //
        // private void attemptRegistration() {
        //
        //
        //     // Reset errors displayed in the form.
        //     mEmailView.setError(null);
        //     mPasswordView.setError(null);
        //
        //     // Store values at the time of the login attempt.
        //     String email = mEmailView.getText().toString();
        //     String password = mPasswordView.getText().toString();
        //
        //     boolean cancel = false;
        //     View focusView = null;
        //
        //     // Check for a valid password, if the user entered one.
        //     if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
        //         mPasswordView.setError(getString(R.string.error_invalid_password));
        //         focusView = mPasswordView;
        //         cancel = true;
        //     }
        //
        //     // Check for a valid email address.
        //     if (TextUtils.isEmpty(email)) {
        //         mEmailView.setError(getString(R.string.error_field_required));
        //         focusView = mEmailView;
        //         cancel = true;
        //     } else if (!isEmailValid(email)) {
        //         mEmailView.setError(getString(R.string.error_invalid_email));
        //         focusView = mEmailView;
        //         cancel = true;
        //     }
        //
        //     if (cancel) {
        //         // There was an error; don't attempt login and focus the first
        //         // form field with an error.
        //         focusView.requestFocus();
        //     } else {
        //         // TODO: Call create FirebaseUser() here
        //         createFirebaseUser();
        //     }
        // }
        //
        //
        // private boolean isEmailValid(String email) {
        //     // You can add more checking logic here.
        //     return email.contains("@");
        // }
        //
        // private boolean isPasswordValid(String password) {
        //     //TODO: Add own logic to check for a valid password
        //     String confirmPassword = mConfirmPasswordView.getText().toString();
        //     return confirmPassword.equals(password) && password.length() > 4;
        // }
        //
        // // TODO: Create a Firebase user
        // private void createFirebaseUser() {
        //     String email = mEmailView.getText().toString();
        //     String password = mPasswordView.getText().toString();
        //     mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        //         @Override
        //         public void onComplete(@NonNull Task<AuthResult> task) {
        //             Log.d("Medic Companion", "createUser onComplete: " + task.isSuccessful());
        //
        //             if (!task.isSuccessful()){
        //                 Log.d("Medic Companion", "user creation failed");
        //                 showErrorDialog("Registration attempt failed");
        //             }else {
        //                 saveDisplayName();
        //                // Address();
        //                 Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        //                 finish();
        //                 startActivity(intent);
        //             }
        //         }
        //     });
        // }
        //
        //
        //
        // }
        //
        // // TODO: Save the display name to Shared Preferences
        // private void saveDisplayName(){
        //     String displayName = mEmailView.getText().toString();
        //     SharedPreferences prefs = getSharedPreferences(USER_PREFS, 0);
        //     prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
        // }
        //
        // // TODO: Save the display name to Shared Preferences
        ///* private void Address(){
        //     String Address = mAddressView.getText().toString();
        //     SharedPreferences prefs = getSharedPreferences(USER_PREFS, 0);
        //     prefs.edit().putString(DISPLAY_NAME_KEY, Address).apply();
        // }*/
        //
        //
        // // TODO: Create an alert dialog to show in case registration failed
        // private void showErrorDialog(String message){
        //     new AlertDialog.Builder(this)
        //             .setTitle("Oops")
        //             .setMessage(message)
        //             .setPositiveButton(android.R.string.ok, null)
        //             .setIcon(android.R.drawable.ic_dialog_alert)
        //             .show();
        //
        // }

    }

}
