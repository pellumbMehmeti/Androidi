package com.projekti.grupi3.sportre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReservationRetrieveAll extends AppCompatActivity {

    private RecyclerView mReservationList;
    private DatabaseReference mDatabase;
    private Button mRemoveReservation;
    public FirebaseUser mUCurrentUser;

    private Query mQueryCurrentUser;

    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_retrieve_all);

        mRemoveReservation=findViewById(R.id.reserve_remove_btn);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Reservation");

        String currentUserId=mAuth.getCurrentUser().getUid();

        mQueryCurrentUser=mDatabase.orderByChild("Uid").equalTo(currentUserId);









        mReservationList= findViewById(R.id.myReservation_list);
        mReservationList.setHasFixedSize(true);
        mReservationList.setLayoutManager(new LinearLayoutManager(ReservationRetrieveAll.this));
    }

    @Override
    protected void onStart() {

        super.onStart();

 FirebaseRecyclerAdapter<ReservationRetrieve, ReserveRetrieveViewHolder> FirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ReservationRetrieve, ReserveRetrieveViewHolder>(
                ReservationRetrieve.class,
                R.layout.activity_single_reservation_item,
                ReserveRetrieveViewHolder.class,
                mQueryCurrentUser
        ){

            @Override

            protected void populateViewHolder(ReserveRetrieveViewHolder viewHolder, ReservationRetrieve model, final int position) {

                final String post_key=getRef(position).getKey();

                viewHolder.setFieldName(model.getFieldName());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setUserId(model.getUserId());

               /* viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleField=new Intent(Main2Activity.this,FieldSingleActivity.class);
                        singleField.putExtra("field_id",post_key);
                        startActivity(singleField);//me ta qel veq 1 fushe per rezervim

                    }
                });*/
            }
        };}
      //  FirebaseRecyclerAdapter

    public  class ReserveRetrieveViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public ReserveRetrieveViewHolder(View itemView) {
            super(itemView);

            mView= itemView ;

        }

        public void setFieldName(String FieldName)
        {
            TextView reservation_fieldName= mView.findViewById(R.id.reserve_fieldName_txt);
            reservation_fieldName.setText(FieldName);
        }
        public void setDate(String Date)
        {
            TextView reservation_date= mView.findViewById(R.id.reserve_date_txt);
            reservation_date.setText(Date);
        }
        public void setTime(String Time)

        {
            TextView reservation_time= mView.findViewById(R.id.reserve_time_txt);
            reservation_time.setText(Time);
        }
        public void setUserId(String usernameId)

        {
            //TextView reservation_time= mView.findViewById(R.id.reserve_time_txt);
            //String useri=;
        }
    }
}
