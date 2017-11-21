package com.adeyeye.medicalcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.adeyeye.medicalcompanion.RegisterActivity.DISPLAY_NAME_KEY;
import static com.adeyeye.medicalcompanion.RegisterActivity.USER_PREFS;


public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private FirebaseAuth mAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Session session;
    private DatabaseReference mDatabaseUsers;
    private Button mSignUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initModel();

//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//
//        if (session.loggedIn()){
//            startActivity(new Intent(LoginActivity.this, DoctorProfileActivity.class));
//            finish();
//        }
//
//
////        // TODO: Grab an instance of FirebaseAuth
//       mAuth = FirebaseAuth.getInstance();
    }

    private void initView() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mSignUp = (Button) findViewById(R.id.login_sign_in_button);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error

                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                            } else

                            {
                                Intent intent =
                                    new Intent(getApplicationContext(), NavigationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
            }

        });

    }

    /*private void startCheckUser() {
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("doctors");
        mDatabaseUsers.keepSynced(true);
       // checkUserExist(mAuth,mDatabaseUsers);
    }*/

    // Executed when Sign in button pressed
    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.adeyeye.medicalcompanion.RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("") || password.equals("")) return;
        Toast.makeText(this, "Login in progress....", Toast.LENGTH_SHORT).show();


        // TODO: Use FirebaseAuth to sign in with email & password
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Medic Companion", "signInWithEmail() onComplete: " + task.isSuccessful());

                if (!task.isSuccessful()){
                    Log.d("Medic Companion", "Problem signing in: " + task.getException());
                    showErrorDialog("There was a problem signing in");

                }else {
                    session.setLoggedIn(true);
                    Intent intent = new Intent(LoginActivity.this, DoctorProfileActivity.class);
                    startActivity(intent);
                    finish();
                }





            }
        });


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
                        setupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
//            Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(mIntent);
        }
    }

    private void initModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("doctors");
        mDatabaseUsers.keepSynced(true);
    }



    // TODO: Show error on screen with an alert dialog
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}