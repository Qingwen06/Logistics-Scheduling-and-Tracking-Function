package com.example.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    ImageButton button,btndriver;
    ListView myListView;
    List<Students> studentsList;
    DatabaseReference studentDbRef;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mFirebaseAuth = FirebaseAuth.getInstance();

        button = (ImageButton) findViewById(R.id.addorder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddorder();
            }
        });

        btndriver = (ImageButton) findViewById(R.id.adddriver);
        btndriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendriver();
            }
        });

        myListView = findViewById(R.id.myListView);
        studentsList = new ArrayList<>();
        studentDbRef = FirebaseDatabase.getInstance().getReference("students");

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Students students = studentsList.get(position);
                showUpdateDialog(students.getPhone(), students.getName(), students.getEmail(), students.getPurl(), students.getDeliveryman(), students.getDeliveryid(), students.getLatitude(), students.getLongitude(), students.getItem(), students.getWeight(), students.getDate(), students.getDeliverydate());

                return false;
            }
        });

        studentDbRef.orderByChild("status").equalTo("Delivering").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsList.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()){

                    Students students = studentDatasnap.getValue(Students.class);
                    studentsList.add(students);
                }

                com.example.manager.ListAdapter adapter = new com.example.manager.ListAdapter(com.example.manager.Admin.this,studentsList);
                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateDialog(String phone, String name, String email, String purl, String deliveryman, String deliverymanid, String latitude, String longitude, String item, String weight, String date, String deliverydate){

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.update_dialog, null);

        mDialog.setView(mDialogView);

        TextView Deliveryman = mDialogView.findViewById(R.id.delivery_man);
        TextView DeliverymanID = mDialogView.findViewById(R.id.delivery_man_id);


////                fAuth = FirebaseAuth.getInstance();
////                fStore = FirebaseFirestore.getInstance();
////                userId = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("Users").document(userId);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                fullName.setText(documentSnapshot.getString("FullName"));
//                ID.setText(userId);
//            }
//        });

        TextView rcUpdatephone = mDialogView.findViewById(R.id.rcUpdatephone);
        TextView rcUpdatename = mDialogView.findViewById(R.id.rcUpdatename);
        TextView rcUpdateemail = mDialogView.findViewById(R.id.rcUpdateemail);
        TextView rcUpdatepurl = mDialogView.findViewById(R.id.rcUpdatepurl);
//        TextView rcUpdatestatus = mDialogView.findViewById(R.id.rcUpdatestatus);
//        TextView rcUpdatestatuscombine = mDialogView.findViewById(R.id.rcUpdatestatuscombine);
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
                if(documentSnapshot != null){

                    TextView DeliveryManPhone = mDialogView.findViewById(R.id.delivery_man_phone);
                    TextView DeliveryManEmail = mDialogView.findViewById(R.id.delivery_man_email);
                    DeliveryManPhone.setText(documentSnapshot.getString("PhoneNumber"));
                    DeliveryManEmail.setText(documentSnapshot.getString("UserEmail"));

                }else{
                    Toast.makeText(com.example.manager.Admin.this,"Logout",Toast.LENGTH_LONG).show();
                }
            }
        });

//        rcUpdatestatus.setText();
//        rcUpdatestatuscombine.setText("Delivering_"+userId);

        rcUpdatelatitude.setText(latitude);
        rcUpdatelongitude.setText(longitude);

//        mDialog.setTitle("Updating" + name +" record");
        mDialog.setTitle("Order Details");
        mDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here will update data in database
                //now get values from view

                String newPhone = rcUpdatephone.getText().toString();
//                String newName = rcUpdatename.getText().toString();
//                String newEmail = rcUpdateemail.getText().toString();
//                String newPurl = rcUpdatepurl.getText().toString();
//                String newStatus = rcUpdatestatus.getText().toString();
//
//                String newStatuscombine = rcUpdatestatuscombine.getText().toString();
//                String newDeliveryman = fullName.getText().toString();
//                String newDeliverymanid = ID.getText().toString();
                String deLatitude = rcUpdatelatitude.getText().toString();
                String deLongitude = rcUpdatelongitude.getText().toString();
//
//                updateData(newPhone,newName,newEmail,newPurl,newStatus,newStatuscombine,newDeliveryman,newDeliverymanid,newLatitude,newLongitude);

//                Toast.makeText(DriverHome.this,"Delivery Start", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent (com.example.manager.Admin.this, com.example.manager.MapTracking.class);
//                intent.putExtra("Address",newPurl);
                intent.putExtra("Phone",newPhone);
                intent.putExtra("Latitude",deLatitude);
                intent.putExtra("Longitude",deLongitude);
                startActivity(intent);
//                openAddorder();

            }
        });
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

    public void openAddorder() {
        Intent intent = new Intent(this, com.example.manager.Order.class);
        startActivity(intent);
    }

    public void opendriver() {
        Intent intent = new Intent(this, Driver.class);
        startActivity(intent);
    }

}