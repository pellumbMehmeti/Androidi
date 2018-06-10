package com.example.admin.resport;//per github

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class Main2Activity extends AppCompatActivity {

    private RecyclerView mFieldList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

            mDatabase= FirebaseDatabase.getInstance().getReference().child("Field");


            mFieldList= findViewById(R.id.field_list);
            mFieldList.setHasFixedSize(true);
            mFieldList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter <FieldRetrieve, FieldRetrieveViewHolder> FirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<FieldRetrieve, FieldRetrieveViewHolder>(
                FieldRetrieve.class,
                R.layout.row_field,
                FieldRetrieveViewHolder.class,
                mDatabase
        ) {

            @Override
            protected void populateViewHolder(FieldRetrieveViewHolder viewHolder, FieldRetrieve model, final int position) {

               final String post_key=getRef(position).getKey();

                    viewHolder.setFieldName(model.getFieldName());
                    viewHolder.setPrice(model.getPrice());
                    viewHolder.setImage(getApplicationContext(),model.getImage());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent singleField=new Intent(Main2Activity.this,FieldSingleActivity.class);
                            singleField.putExtra("field_id",post_key);
                            startActivity(singleField);//me ta qel veq 1 fushe per rezervim

                        }
                    });
            }
        };

        mFieldList.setAdapter(FirebaseRecyclerAdapter);
         }

    public  class FieldRetrieveViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public FieldRetrieveViewHolder(View itemView) {
            super(itemView);

            mView= itemView ;

        }

        public void setFieldName(String FieldName)
        {
            TextView post_fieldName= mView.findViewById(R.id.post_fieldName);
            post_fieldName.setText(FieldName);
        }
        public void setPrice(String Price)
        {
            TextView post_price= mView.findViewById(R.id.post_price);
            post_price.setText(Price);
        }
        public void setImage(Context ctx, String Image)

        {
            ImageView post_image= mView.findViewById(R.id.post_image);
            Picasso.get().load(Image).into(post_image);
            //mundet me pas gabim te picasso
        }
    }
}
