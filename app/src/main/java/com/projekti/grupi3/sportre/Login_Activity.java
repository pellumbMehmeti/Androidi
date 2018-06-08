package com.projekti.grupi3.sportre;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ProgressBar;
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

public class Login_Activity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    // private Button loginRegBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser mUCurrentUser;
    private DatabaseReference mDatabaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mUCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mUCurrentUser.getUid()).child("uType");

        loginEmailText = findViewById(R.id.reg_email);
        loginPassText = findViewById(R.id.reg_confirm_pass);
        loginBtn = findViewById(R.id.login_btn);
        //loginRegBtn = findViewById(R.id.btn_login);

        final String loginEmail = loginEmailText.getText().toString();
        final String loginPass = loginPassText.getText().toString();



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();


            }


        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    sendToMain();


                } else {
                    // User is signed out
                    // Authenticated successfully with authData
                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        };


    }




    private void sendToMain() {

        Intent mainIntent = new Intent(Login_Activity.this, userActivity2.class);
        startActivity(mainIntent);
        finish();

    }

    private void loginUser() {
        final String loginEmail = loginEmailText.getText().toString();
        final String loginPass = loginPassText.getText().toString();

        mAuth.signInWithEmailAndPassword(loginEmail, loginPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String RegisteredUserID = currentUser.getUid();

                            DatabaseReference jLoginDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                            jLoginDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String userType = dataSnapshot.child("uType").getValue().toString();
                                    if (userType.equals("Regular User")) {
                                        Intent intentResident = new Intent(Login_Activity.this, userActivity2.class);
                                        intentResident.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentResident);
                                        finish();
                                    } else if (userType.equals("Sport Terrain Owner")) {
                                        Intent intentMain = new Intent(Login_Activity.this, AddField.class);
                                        intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentMain);
                                        finish();
                                    } else {
                                        Toast.makeText(Login_Activity.this, "Failed Login. Please Try Again", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }
}