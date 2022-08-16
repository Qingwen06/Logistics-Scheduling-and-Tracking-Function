package com.example.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CusResult extends AppCompatActivity {

    ListView myListView;
    List<Studentscus> studentsList;
    DatabaseReference studentDbRef;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_result);

        TextView textFIND = findViewById(R.id.textFind);
        TextView keyFIND = findViewById(R.id.key);

        String textFind = getIntent().getStringExtra("textFind");
        textFIND.setText(textFind);
//        String keyFind = getIntent().getStringExtra("keyFind");
//        keyFIND.setText(keyFind);


        myListView = findViewById(R.id.myListView);
        studentsList = new ArrayList<>();
        studentDbRef = FirebaseDatabase.getInstance().getReference("students");
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Studentscus studentscus = studentsList.get(position);
                showUpdateDialog(studentscus.getPhone(), studentscus.getName(), studentscus.getEmail(), studentscus.getPurl(), studentscus.getDeliveryman(), studentscus.getDeliveryid(), studentscus.getLatitude(), studentscus.getLongitude(), studentscus.getStatus(), studentscus.getReceiverIC(), studentscus.getReceiverName(), studentscus.getReceiverPhone(), studentscus.getImageurl(), studentscus.getReason());

                return false;
            }
        });

        studentDbRef.orderByChild("key").equalTo(textFind).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    studentsList.clear();
                    for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()){
                        Studentscus studentscus = studentDatasnap.getValue(Studentscus.class);
                        studentsList.add(studentscus);
                    }
                    ListAdapterCus adapter = new ListAdapterCus(CusResult.this,studentsList);
                    myListView.setAdapter(adapter);
                }else{
                    TextView noResult = findViewById(R.id.no_result);
                    noResult.setText("No Result Please Use Correct Order ID");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void showUpdateDialog(String phone, String name, String email, String purl, String deliveryman, String deliverymanid, String latitude, String longitude, String status, String receiverIC, String receiverName, String receiverPhone, String imageurl, String reason){

        if(status.equals("Free")) {

            AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View mDialogView = inflater.inflate(R.layout.dialog_free, null);
            mDialog.setView(mDialogView);

            TextView rPhone = mDialogView.findViewById(R.id.uphone);
            TextView rName = mDialogView.findViewById(R.id.uname);
            TextView rEmail = mDialogView.findViewById(R.id.uemail);
            TextView rAddress = mDialogView.findViewById(R.id.uimgurl);
            //TextView ustatus = mDialogView.findViewById(R.id.ustatus);

            rPhone.setText(phone);
            rName.setText(name);
            rEmail.setText(email);
            rAddress.setText(purl);
            //ustatus.setText(status);

            mDialog.show();
        }

        if(status.equals("Delivering")) {

            AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View mDialogView = inflater.inflate(R.layout.cus_track, null);
            mDialog.setView(mDialogView);

            TextView Deliveryman = mDialogView.findViewById(R.id.delivery_man);
            TextView DeliverymanID = mDialogView.findViewById(R.id.delivery_man_id);
            TextView rcUpdatephone = mDialogView.findViewById(R.id.rcUpdatephone);
            TextView rcUpdatename = mDialogView.findViewById(R.id.rcUpdatename);
            TextView rcUpdateemail = mDialogView.findViewById(R.id.rcUpdateemail);
            TextView rcUpdatepurl = mDialogView.findViewById(R.id.rcUpdatepurl);
            TextView rcUpdatelatitude = mDialogView.findViewById(R.id.textlatitude);
            TextView rcUpdatelongitude = mDialogView.findViewById(R.id.textlongitude);
            TextView rckey = mDialogView.findViewById(R.id.key);
            Button btnUpdate = mDialogView.findViewById(R.id.btnUpdate);

            rcUpdatephone.setText(phone);
            rcUpdatename.setText(name);
            rcUpdateemail.setText(email);
            rcUpdatepurl.setText(purl);
            Deliveryman.setText(deliveryman);
            DeliverymanID.setText(deliverymanid);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = fStore.collection("Users").document(deliverymanid);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        TextView DeliveryManPhone = mDialogView.findViewById(R.id.delivery_man_phone);
                        TextView DeliveryManEmail = mDialogView.findViewById(R.id.delivery_man_email);
                        DeliveryManPhone.setText(documentSnapshot.getString("PhoneNumber"));
                        DeliveryManEmail.setText(documentSnapshot.getString("UserEmail"));
                    } else {

                    }
                }
            });

            rcUpdatelatitude.setText(latitude);
            rcUpdatelongitude.setText(longitude);

            mDialog.setTitle("Order Detail");
            mDialog.show();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //here will update data in database
                    //now get values from view

                    String newPhone = rcUpdatephone.getText().toString();
                    String deLatitude = rcUpdatelatitude.getText().toString();
                    String deLongitude = rcUpdatelongitude.getText().toString();

                    Intent intent = new Intent(CusResult.this, CusMapTrack.class);
                    intent.putExtra("Phone", newPhone);
                    intent.putExtra("Latitude", deLatitude);
                    intent.putExtra("Longitude", deLongitude);
                    startActivity(intent);

                }
            });

        }

        if(status.equals("Completed")) {
            AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View mDialogView = inflater.inflate(R.layout.dialog_completed, null);
            mDialog.setView(mDialogView);

            TextView rPhone = mDialogView.findViewById(R.id.uphone);
            TextView rName = mDialogView.findViewById(R.id.uname);
            TextView rEmail = mDialogView.findViewById(R.id.uemail);
            TextView rAddress = mDialogView.findViewById(R.id.uimgurl);
            TextView ustatus = mDialogView.findViewById(R.id.ustatus);
            TextView newrName = mDialogView.findViewById(R.id.rname);
            TextView newrPhone = mDialogView.findViewById(R.id.rphone);
            TextView newrIC= mDialogView.findViewById(R.id.rIC);

            TextView Deliveryman = mDialogView.findViewById(R.id.dname);

            ImageView rImage = mDialogView.findViewById(R.id.rImage);

            rPhone.setText(phone);
            rName.setText(name);
            rEmail.setText(email);
            rAddress.setText(purl);
            ustatus.setText(status);
            newrName.setText(receiverName);
            newrPhone.setText(receiverPhone);
            newrIC.setText(receiverIC);

            Deliveryman.setText(deliveryman);

            Picasso.get().load(imageurl).into(rImage);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = fStore.collection("Users").document(deliverymanid);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        TextView DeliveryManPhone = mDialogView.findViewById(R.id.dphone);
                        TextView DeliveryManEmail = mDialogView.findViewById(R.id.demail);
                        DeliveryManPhone.setText(documentSnapshot.getString("PhoneNumber"));
                        DeliveryManEmail.setText(documentSnapshot.getString("UserEmail"));
                    } else {

                    }
                }
            });

            mDialog.show();
        }

        if(status.equals("Cancelled")) {
            AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View mDialogView = inflater.inflate(R.layout.dialog_cancelled, null);
            mDialog.setView(mDialogView);

            TextView rPhone = mDialogView.findViewById(R.id.uphone);
            TextView rName = mDialogView.findViewById(R.id.uname);
            TextView rEmail = mDialogView.findViewById(R.id.uemail);
            TextView rAddress = mDialogView.findViewById(R.id.uimgurl);
            TextView ustatus = mDialogView.findViewById(R.id.ustatus);

            TextView Deliveryman = mDialogView.findViewById(R.id.dname);
            TextView creason = mDialogView.findViewById(R.id.reason);

            rPhone.setText(phone);
            rName.setText(name);
            rEmail.setText(email);
            rAddress.setText(purl);
            ustatus.setText(status);

            Deliveryman.setText(deliveryman);
            creason.setText(reason);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = fStore.collection("Users").document(deliverymanid);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        TextView DeliveryManPhone = mDialogView.findViewById(R.id.dphone);
                        TextView DeliveryManEmail = mDialogView.findViewById(R.id.demail);
                        DeliveryManPhone.setText(documentSnapshot.getString("PhoneNumber"));
                        DeliveryManEmail.setText(documentSnapshot.getString("UserEmail"));
                    } else {

                    }
                }
            });

            mDialog.show();
        }

    }












}