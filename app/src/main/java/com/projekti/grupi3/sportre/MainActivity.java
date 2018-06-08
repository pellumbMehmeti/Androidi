package com.projekti.grupi3.sportre;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private Spinner typeSpinner;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private Button BLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
       BLogin = findViewById(R.id.button_login);
        mProgress=new ProgressDialog(this);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        typeSpinner=findViewById(R.id.spinner1);
        mAuth = FirebaseAuth.getInstance();

       findViewById(R.id.button_login).setOnClickListener(this);
        BLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(regIntent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            Intent loginIntent = new Intent(MainActivity.this, userActivity2.class);
            startActivity(loginIntent);
        }
    }


    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String type = typeSpinner.getSelectedItem().toString().trim();
        mProgress.setMessage("Registering User...");
        mProgress.show();


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                                   progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    type
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                                           progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Toast.makeText(MainActivity.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                                        Intent loginIntent = new Intent(MainActivity.this, userActivity2.class);
                                      startActivity(loginIntent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to Register new user!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            mProgress.dismiss();


                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }
}


