package com.example.admin.resport;//per github

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class FieldSingleActivity extends AppCompatActivity {

    private String mPost_key=null;
    private DatabaseReference mDatabase,mDatabase1;
    private FirebaseAuth mAuth;

    private ImageView mSingleFieldImage;
    private TextView mSingleFieldName;
    private TextView mSingleFieldPrice;
    private Button mRemoveField;
    private EditText mSingleReservationDate;

    private ListView mListaTermineve;

    Button btn_date;
    DatePickerDialog datePickerDialog;

   private ArrayList<String> listaOrari= new ArrayList<>();
   private ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_single);

        ShowDatePicker();

        mDatabase= FirebaseDatabase.getInstance().getReference("Field");

        mAuth=FirebaseAuth.getInstance();


        mPost_key=getIntent().getExtras().getString("field_id");



        mDatabase1=mDatabase.child(mPost_key).child("Terminet");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    String termini = ds.getValue().toString();

                    listaOrari.add(termini);

                    adapter.notifyDataSetChanged();

                }
                mListaTermineve.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
         // I MERRR TERMINET prej databazes ,duhet mi shti ne listview
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listaOrari);//munet listaOrari me kon mListaTermineve

        mSingleFieldName= findViewById(R.id.post_SingleFieldName);
        mSingleFieldPrice= findViewById(R.id.post_SingleFieldPrice);
        mSingleFieldImage= findViewById(R.id.post_SingleFieldImage);
        mRemoveField=findViewById(R.id.remove_btn);

        mSingleReservationDate= findViewById(R.id.reservation_date);

        mListaTermineve= findViewById(R.id.list_Terminet);




        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_fieldName=(String) dataSnapshot.child("Field name").getValue().toString();
                String post_fieldPrice=(String) dataSnapshot.child("Price").getValue().toString();
                String post_image=(String) dataSnapshot.child("Image").getValue().toString();
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
    public void ShowDatePicker(){
        btn_date = findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar c_date = Calendar.getInstance();
                int year = c_date.get(Calendar.YEAR);
                int month = c_date.get(Calendar.MONTH);
                int day = c_date.get(c_date.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final String dateSelected=dayOfMonth+"/"+month+"/"+year;
                        //Toast.makeText(MainActivity.this, "Month: " + month + " Day: " + dayOfMonth + " Year: " + year, Toast.LENGTH_SHORT).show();
                        mSingleReservationDate.setText(dateSelected.toString());
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
    }

}
