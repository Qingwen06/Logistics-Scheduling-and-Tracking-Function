package com.example.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Driver extends AppCompatActivity {

    private static  final String TAG = "FireStoreListActivity";

    private FirebaseAuth mFirebaseAuth;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    ListView myListView;
    List<com.example.manager.Users> studentsList2;
    DatabaseReference studentDbRef;

    ListView myListViewInside;
    List<Students> studentsListInside;
    DatabaseReference studentDbRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        mFirebaseAuth = FirebaseAuth.getInstance();

        myListView = findViewById(R.id.myListView);
        studentsList2 = new ArrayList<>();
        studentDbRef = FirebaseDatabase.getInstance().getReference("students");

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                com.example.manager.Users users = studentsList2.get(position);
                showUpdateDialog(users.getFullName(), users.getPhoneNumber(), users.getUserEmail());
                return false;
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users")
                .orderBy("isStudent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            studentsList2.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                com.example.manager.Users users = document.toObject(com.example.manager.Users.class);
                                studentsList2.add(users);
                                com.example.manager.ListAdapter2 adapter = new com.example.manager.ListAdapter2(com.example.manager.Driver.this,studentsList2,users.usersID = document.getId());
                                myListView.setAdapter(adapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        
    }

    public void showUpdateDialog(String FullName, String PhoneNumber, String UserEmail){

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.driverjob, null);
        mDialog.setView(mDialogView);

        TextView dname = mDialogView.findViewById(R.id.dname);
        dname.setText(FullName);

        myListViewInside = mDialogView.findViewById(R.id.myListViewInside);
        studentsListInside = new ArrayList<>();

        studentDbRef2 = FirebaseDatabase.getInstance().getReference("students");
        studentDbRef2.orderByChild("deliveryman").equalTo(FullName).addValueEventListener(new ValueEventListener() {
//        studentDbRef2.orderByChild("status").equalTo("Delivering").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsListInside.clear();
                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()){
                    Students students = studentDatasnap.getValue(Students.class);
                    studentsListInside.add(students);
                }
                com.example.manager.ListAdapterInside adapter = new com.example.manager.ListAdapterInside(com.example.manager.Driver.this,studentsListInside);
                myListViewInside.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        myListViewInside.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Students students = studentsListInside.get(position);
                showUpdateDialogInside(students.getPhone(), students.getName(), students.getEmail(), students.getPurl(), students.getDeliveryman(), students.getDeliveryid(), students.getLatitude(), students.getLongitude(), students.getStatus(), students.getReceiverIC(), students.getReceiverName(), students.getReceiverPhone(), students.getImageurl(), students.getReason(), students.getItem(), students.getWeight(), students.getDate(), students.getDeliverydate(), students.getCompletedate(), students.getCanceldate());
                return false;
            }
        });

        mDialog.show();

    }



    private void showUpdateDialogInside(String phone, String name, String email, String purl, String deliveryman, String deliverymanid, String latitude, String longitude, String status, String receiverIC, String receiverName, String receiverPhone, String imageurl, String reason, String item, String weight, String date, String deliverydate, String completedate, String canceldate){

        if(status.equals("Delivering")) {
            AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View mDialogView = inflater.inflate(R.layout.update_dialog, null);
            mDialog.setView(mDialogView);

            TextView Deliveryman = mDialogView.findViewById(R.id.delivery_man);
            TextView DeliverymanID = mDialogView.findViewById(R.id.delivery_man_id);
            TextView rcUpdatephone = mDialogView.findViewById(R.id.rcUpdatephone);
            TextView rcUpdatename = mDialogView.findViewById(R.id.rcUpdatename);
            TextView rcUpdateemail = mDialogView.findViewById(R.id.rcUpdateemail);
            TextView rcUpdatepurl = mDialogView.findViewById(R.id.rcUpdatepurl);
            TextView rcUpdatelatitude = mDialogView.findViewById(R.id.textlatitude);
            TextView rcUpdatelongitude = mDialogView.findViewById(R.id.textlongitude);
            TextView rcUpdateitem = mDialogView.findViewById(R.id.rcUpdateitem);
            TextView rcUpdateweight = mDialogView.findViewById(R.id.rcUpdateweight);
            TextView rcUpdatedate = mDialogView.findViewById(R.id.rcUpdatedate);
            TextView rcUpdatedeliverydate = mDialogView.findViewById(R.id.rcUpdatedeliverydate);
            Button btnUpdate = mDialogView.findViewById(R.id.btnUpdate);

            rcUpdatephone.setText(phone);
            rcUpdatename.setText(name);
            rcUpdateemail.setText(email);
            rcUpdatepurl.setText(purl);
            Deliveryman.setText(deliveryman);
            DeliverymanID.setText(deliverymanid);
            rcUpdateitem.setText(item);
            rcUpdateweight.setText(weight);
            rcUpdatedate.setText(date);
            rcUpdatedeliverydate.setText(deliverydate);

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
                        Toast.makeText(com.example.manager.Driver.this, "Logout", Toast.LENGTH_LONG).show();
                    }
                }
            });

            rcUpdatelatitude.setText(latitude);
            rcUpdatelongitude.setText(longitude);
            mDialog.setTitle("Receiver Details");
            mDialog.show();

            //TRACKING
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newPhone = rcUpdatephone.getText().toString();
                    String deLatitude = rcUpdatelatitude.getText().toString();
                    String deLongitude = rcUpdatelongitude.getText().toString();

                    Intent intent = new Intent(com.example.manager.Driver.this, com.example.manager.MapTracking.class);
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

            TextView rItem = mDialogView.findViewById(R.id.uitem);
            TextView rWeight = mDialogView.findViewById(R.id.uweight);
            TextView rDate = mDialogView.findViewById(R.id.udate);
            TextView rDeliverydate = mDialogView.findViewById(R.id.udeliverydate);
            TextView rCompletedate = mDialogView.findViewById(R.id.ucompletedate);

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

            rItem.setText(item);
            rWeight.setText(weight);
            rDate.setText(date);
            rDeliverydate.setText(deliverydate);
            rCompletedate.setText(completedate);

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
                        Toast.makeText(com.example.manager.Driver.this, "Logout", Toast.LENGTH_LONG).show();
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

            TextView rItem = mDialogView.findViewById(R.id.uitem);
            TextView rWeight = mDialogView.findViewById(R.id.uweight);
            TextView rDate = mDialogView.findViewById(R.id.udate);
            TextView rDeliverydate = mDialogView.findViewById(R.id.udeliverydate);
            TextView rCanceldate = mDialogView.findViewById(R.id.ucanceldate);

            rPhone.setText(phone);
            rName.setText(name);
            rEmail.setText(email);
            rAddress.setText(purl);
            ustatus.setText(status);

            Deliveryman.setText(deliveryman);
            creason.setText(reason);

            rItem.setText(item);
            rWeight.setText(weight);
            rDate.setText(date);
            rDeliverydate.setText(deliverydate);
            rCanceldate.setText(canceldate);

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
                        Toast.makeText(com.example.manager.Driver.this, "Logout", Toast.LENGTH_LONG).show();
                    }
                }
            });

            mDialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intent = new Intent(this, com.example.manager.Admin.class);
                startActivity(intent);
                Toast.makeText(this,"Going Home Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.driver:
                Intent intent2 = new Intent(this, com.example.manager.Driver.class);
                startActivity(intent2);
                Toast.makeText(this,"Going Driver Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.order:
                Intent intent3 = new Intent(this, com.example.manager.Order.class);
                startActivity(intent3);
                Toast.makeText(this,"Going Order Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profile:
                Intent intent4 = new Intent(this, com.example.manager.Profile.class);
                startActivity(intent4);
                Toast.makeText(this,"Going Profile Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout:
                mFirebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), com.example.manager.Login.class));
                Toast.makeText(this,"Log Out", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}