package com.example.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class DeliveryCancel extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_cancel);

        TextView rUpdatephone = findViewById(R.id.rphone);
        TextView rUpdatename = findViewById(R.id.rname);
        TextView rUpdateemail = findViewById(R.id.remail);
        TextView rUpdatepurl = findViewById(R.id.raddress);
        TextView rUpdatestatus = findViewById(R.id.rstatus);
        TextView rUpdatestatuscombine = findViewById(R.id.rstatuscombine);
        TextView rcUpdatelatitude = findViewById(R.id.rlatitude);
        TextView rcUpdatelongitude = findViewById(R.id.rlongitude);
        EditText rReason = findViewById(R.id.rreason);
        Button btnCancel = findViewById(R.id.bcancel);
        TextView fullName = findViewById(R.id.rdrivername);
        TextView ID = findViewById(R.id.rdriverid);
        TextView rckey = findViewById(R.id.key);

        TextView rcUpdateitem = findViewById(R.id.ritem);
        TextView rcUpdateweight = findViewById(R.id.rweight);
        TextView rcUpdatedate = findViewById(R.id.rdate);
        TextView deliverydate = findViewById(R.id.rdeliverydate);
        TextView canceldate = findViewById(R.id.rcanceldate);

        String passphone = getIntent().getStringExtra("Phone");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    fullName.setText(documentSnapshot.getString("FullName"));
                    ID.setText(userId);
                }else{
                    Toast.makeText(DeliveryCancel.this,"Logout",Toast.LENGTH_LONG).show();
                }
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("students").child(passphone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rphone = dataSnapshot.child("phone").getValue().toString();
                String rname = dataSnapshot.child("name").getValue().toString();
                String remail = dataSnapshot.child("email").getValue().toString();
                String raddress = dataSnapshot.child("purl").getValue().toString();
                String rlatitude = dataSnapshot.child("latitude").getValue().toString();
                String rlongitude = dataSnapshot.child("longitude").getValue().toString();

                String rkey = dataSnapshot.child("key").getValue().toString();

                String ritem = dataSnapshot.child("item").getValue().toString();
                String rweight = dataSnapshot.child("weight").getValue().toString();
                String rdate = dataSnapshot.child("date").getValue().toString();
                String rdeliverydate = dataSnapshot.child("deliverydate").getValue().toString();

                rUpdatephone.setText(rphone);
                rUpdatename.setText(rname);
                rUpdateemail.setText(remail);
                rUpdatepurl.setText(raddress);

                rcUpdatelatitude.setText(rlatitude);
                rcUpdatelongitude.setText(rlongitude);

                rckey.setText(rkey);

                rcUpdateitem.setText(ritem);
                rcUpdateweight.setText(rweight);
                rcUpdatedate.setText(rdate);
                deliverydate.setText(rdeliverydate);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here will update data in database
                //now get values from view
                rUpdatestatus.setText("Cancelled");
                rUpdatestatuscombine.setText("Cancelled_"+userId);

                String newPhone = rUpdatephone.getText().toString();
                String newName = rUpdatename.getText().toString();
                String newEmail = rUpdateemail.getText().toString();
                String newPurl = rUpdatepurl.getText().toString();
                String newStatus = rUpdatestatus.getText().toString();
                String newStatuscombine = rUpdatestatuscombine.getText().toString();
                String newDeliveryman = fullName.getText().toString();
                String newDeliverymanid = ID.getText().toString();
                String newLatitude = rcUpdatelatitude.getText().toString();
                String newLongitude = rcUpdatelongitude.getText().toString();
                String newkey = rckey.getText().toString();

                String newitem = rcUpdateitem.getText().toString();
                String newweight = rcUpdateweight.getText().toString();
                String newdate = rcUpdatedate.getText().toString();
                String newdeliverydate = deliverydate.getText().toString();
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                String dateTime = simpleDateFormat.format(calender.getTime());
                canceldate.setText(dateTime);
                String newcanceldate = canceldate.getText().toString();

                if(TextUtils.isEmpty(rReason.getText())){
                    rReason.setError("Reason is required");
                }else{
                    String newReason = rReason.getText().toString();
                    upload(newPhone,newName,newEmail,newPurl,newStatus,newStatuscombine,newDeliveryman,newDeliverymanid,newLatitude,newLongitude,newReason,newkey,newitem,newweight,newdate,newdeliverydate,newcanceldate);
                    Toast.makeText(DeliveryCancel.this,"Delivery Cancel", Toast.LENGTH_SHORT).show();
                    cancelOrder();
                }

            }
        });



    }

    public void cancelOrder() {
        Intent intent = new Intent(this, DriverHome.class);
        startActivity(intent);
    }

    private void updateData(String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid, String latitude, String longitude, String key, String item, String weight, String date, String deliverydate){

        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("students").child(phone);
        Students students = new Students(phone, name, email, purl, status, statuscombine, deliveryman, deliverymanid, latitude, longitude, key, item, weight, date, deliverydate);
        DbRef.setValue(students);
    }

    private void upload(String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid, String latitude, String longitude, String reason, String key, String item, String weight, String date, String deliverydate, String canceldate) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("students").child(phone);
//                    String postId = ref.push().getKey();

//                    signaturepadLayout = findViewById(R.id.signaturepadLayout);
//                    signaturePad = findViewById(R.id.signature_pad);
//
//                    signaturepadLayout.setDrawingCacheEnabled(true);
//                    signaturepadLayout.buildDrawingCache();
//                    Bitmap bitmap = signaturepadLayout.getDrawingCache();
//                    String newSign = bitmap.toString();


                    HashMap<String , Object> map = new HashMap<>();
//                    map.put("postid" , postId);
//                    map.put("publisher" , FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    map.put("imageurl" , imageUrl);
                    map.put("phone" , phone);
                    map.put("name" , name);
                    map.put("email" , email);
                    map.put("purl" , purl);
                    map.put("status" , status);
                    map.put("statuscombine" , statuscombine);
                    map.put("deliveryman" , deliveryman);
                    map.put("deliveryid" , deliverymanid);
                    map.put("latitude" , latitude);
                    map.put("longitude" , longitude);
                    map.put("reason" , reason);
                    map.put("key" , key);

                    map.put("item" , item);
                    map.put("weight" , weight);
                    map.put("date" , date);
                    map.put("deliverydate" , deliverydate);
                    map.put("canceldate" , canceldate);

                    ref.setValue(map);

//                    DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("students");
//                    List<String> hashTags = phone.getHashtags();
//                    if (!hashTags.isEmpty()){
//                        for (String tag : hashTags){
//                            map.clear();
//
//                            map.put("tag" , tag.toLowerCase());
//                            map.put("postid" , postId);
//
//                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
//                        }
//                    }

//                    pd.dismiss();
//                    startActivity(new Intent(SellActivity.this , BSActivity.class));
//                    finish();
                }

}