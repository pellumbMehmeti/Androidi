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
    private Button registerBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser mUCurrentUser;
    private DatabaseReference mDatabaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mUCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference();

        loginEmailText = findViewById(R.id.reg_email);
        loginPassText = findViewById(R.id.reg_confirm_pass);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.signup_btn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegister();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        }




    private void sendToRegister() {

        Intent mainIntent1 = new Intent(Login_Activity.this, MainActivity.class);
        startActivity(mainIntent1);
        finish();

    }

    private void loginUser() {
        final String loginEmail = loginEmailText.getText().toString().trim();
        final String loginPass = loginPassText.getText().toString().trim();

        if(TextUtils.isEmpty(loginEmail)){
            Toast.makeText(this, "Please Enter Emailsss", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(loginPass)){
            Toast.makeText(this, "Please Enter Your PASSWORDS", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(loginEmail, loginPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            onAuthSuccess(task.getResult().getUser());

                        }
                        else {
                            Toast.makeText(Login_Activity.this, "Could not login, password or email wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {        // ndrro ne user

        //String username = usernameFromEmail(user.getEmail())
        if (user != null) {
            //Toast.makeText(signinActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
            mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userType = dataSnapshot.child("uType").getValue().toString();
                    //for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Toast.makeText(signinActivity.this, value, Toast.LENGTH_SHORT).show();

                        //String jason = (String) snapshot.getValue();
                        //Toast.makeText(signinActivity.this, jason, Toast.LENGTH_SHORT).show();
                        if (userType.equals("Regular User")) {
                            Intent intentResident = new Intent(Login_Activity.this, userActivity2.class);
                            startActivity(intentResident);
                            finish();
                        }
                        else if (userType.equals("Sport Terrain Owner")) {
                        Intent intentMain = new Intent(Login_Activity.this, OwnerActivity.class);
                        startActivity(intentMain);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }}



