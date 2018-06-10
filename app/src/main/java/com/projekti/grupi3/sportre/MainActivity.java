package com.projekti.grupi3.sportre;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private Spinner typeSpinner;
    private Button bLogin;

    private FirebaseAuth mAuth;
    private FirebaseUser mUCurrentUser;
    private DatabaseReference mDatabaseUser;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        typeSpinner= findViewById(R.id.spinner1);


        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }



    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String type = typeSpinner.getSelectedItem().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }




        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    type
                            );
                            onAuthSuccess(task.getResult().getUser());

                         FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                       // Intent loginIntent = new Intent(MainActivity.this,AddField.class);
                                      //  startActivity(loginIntent);
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    public void goToLogin()
    {
        Intent goToLoginIntent = new Intent(MainActivity.this, Login_Activity.class);
        startActivity(goToLoginIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
            case R.id.button_login:
                goToLogin();
                break;
        }
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
                        Intent intentResident = new Intent(MainActivity.this, userActivity2.class);
                        startActivity(intentResident);
                        finish();
                    }
                    else if (userType.equals("Sport Terrain Owner")) {
                        Intent intentMain = new Intent(MainActivity.this, OwnerActivity.class);
                        startActivity(intentMain);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}