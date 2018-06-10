package com.example.admin.resport;//per github

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FieldSingleActivity extends AppCompatActivity {

    private String mPost_key=null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ImageView mSingleFieldImage;
    private TextView mSingleFieldName;
    private TextView mSingleFieldPrice;
    private Button mRemoveField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_single);

        mDatabase= FirebaseDatabase.getInstance().getReference("Field");
        mAuth=FirebaseAuth.getInstance();

        mPost_key=getIntent().getExtras().getString("field-id");

        mSingleFieldName= findViewById(R.id.post_SingleFieldName);
        mSingleFieldPrice= findViewById(R.id.post_SingleFieldPrice);
        mSingleFieldImage= findViewById(R.id.post_SingleFieldImage);
        mRemoveField=findViewById(R.id.remove_btn);

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_fieldName=(String) dataSnapshot.child("Field name").toString();
                String post_fieldPrice=(String) dataSnapshot.child("Price").toString();
                String post_image=(String) dataSnapshot.child("Image").toString();
                String post_uid=(String) dataSnapshot.child("uid").getValue();

                Picasso.get().load(post_image).into(mSingleFieldImage);//munet me  pas gabim te get
                mSingleFieldName.setText(post_fieldName);
                mSingleFieldPrice.setText(post_fieldPrice);

                if(mAuth.getCurrentUser().getUid().equals(post_uid))
                {
                    mRemoveField.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRemoveField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mPost_key).removeValue();

                Intent gotoAllFields=new Intent(FieldSingleActivity.this,Main2Activity,class);
                startActivity(gotoAllFields);

            }
        });

    }
}
