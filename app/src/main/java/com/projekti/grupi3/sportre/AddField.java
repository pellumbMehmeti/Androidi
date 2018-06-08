package com.projekti.grupi3.sportre;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
//import android.support.v4.widget.MaterialProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddField extends AppCompatActivity {

    private ImageButton selectImg;
    private EditText field,price;
    private Spinner select_Sport;
    private Button submit_btn;

    private ProgressDialog mProgress;

    private Uri select_img_Uri=null;

    private StorageReference mStorageRef;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private FirebaseUser mUCurrentUser;

    private DatabaseReference mDatabaseUser;

    private static final int GALLERY_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        mAuth=FirebaseAuth.getInstance();
        mUCurrentUser=mAuth.getCurrentUser();

        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mUCurrentUser.getUid());//emri i tabeless users



        selectImg= (ImageButton) findViewById(R.id.field_img_btn);
        field=(EditText) findViewById(R.id.fieldName);
        price=(EditText) findViewById(R.id.edit_price);
        select_Sport=(Spinner) findViewById(R.id.sport_select);
        submit_btn=(Button) findViewById(R.id.add_btn);


        mStorageRef= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Field");

        mProgress=new ProgressDialog(this);


        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }


        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }


        });

    }
    private void startPosting() {

        mProgress.setMessage("Posting Field to Database!");
        mProgress.show();


        final String field_val=field.getText().toString().trim();
        final String price_val=price.getText().toString().trim();
        final String sport_val=select_Sport.getSelectedItem().toString().trim();

        //if(!TextUtils.isEmpty(field_val) && !TextUtils.isEmpty(price_val) && !TextUtils.isEmpty(sport_val))
       // {
            StorageReference filepath=mStorageRef.child("Field_Images").child(select_img_Uri.getLastPathSegment());

            filepath.putFile(select_img_Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final  Uri downloadUrl=taskSnapshot.getDownloadUrl();

                    final DatabaseReference newPost=mDatabase.push();


                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("Field name").setValue(field_val);
                            newPost.child("Type").setValue(sport_val);
                            newPost.child("Price").setValue(price_val);
                            newPost.child("Image").setValue(downloadUrl.toString());
                            newPost.child("Username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        Intent AddIntent = new Intent(AddField.this,OwnerActivity.class);
                                        startActivity(AddIntent);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgress.dismiss();


                }
            });
        //}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            select_img_Uri=data.getData();
            selectImg.setImageURI(select_img_Uri);
        }
    }
}
