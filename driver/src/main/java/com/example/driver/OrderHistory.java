package com.example.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    ListView myListView;
    List<Students> studentsList;
    DatabaseReference studentDbRef;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userId;

    private Button orderdelivering;
    private Button orderdone;
    private Button ordercancel;

//    TextView fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mFirebaseAuth = FirebaseAuth.getInstance();

        myListView = findViewById(R.id.myListView);
        studentsList = new ArrayList<>();
        studentDbRef = FirebaseDatabase.getInstance().getReference("students");

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Students students = studentsList.get(position);
                showUpdateDialog(students.getPhone(), students.getName(), students.getEmail(), students.getPurl(), students.getLatitude(), students.getLongitude(), students.getKey(), students.getItem(), students.getWeight(), students.getDate());

                return false;
            }
        });


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
//        fullName = findViewById(R.id.delivery_man);
//        TextView ID = findViewById(R.id.delivery_man_id);

//        DocumentReference documentReference = fStore.collection("Users").document(userId);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if(documentSnapshot != null) {
////                    fullName.setText(documentSnapshot.getString("FullName"));
////                    ID.setText(userId);
//                }else{
//                    Toast.makeText(OrderHistory.this,"Logout",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        studentDbRef.orderByChild("statuscombine").equalTo("Delivering_"+userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsList.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()){

                    Students students = studentDatasnap.getValue(Students.class);
                    studentsList.add(students);
                }

                ListAdapter1 adapter1 = new ListAdapter1(OrderHistory.this,studentsList);
                myListView.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderdelivering = (Button) findViewById(R.id.order_delivering);
        orderdelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                Orderdelivering();
            }
        });

        orderdone = (Button) findViewById(R.id.order_done);
        orderdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                Orderdone();
            }
        });

        ordercancel = (Button) findViewById(R.id.order_cancel);
        ordercancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                OrderCancel();
            }
        });

    }

    private void showUpdateDialog(String phone, String name, String email, String purl, String latitude, String longitude, String key, String item, String weight, String date){

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.update_dialog2, null);

        mDialog.setView(mDialogView);

        TextView fullName = mDialogView.findViewById(R.id.delivery_man);
        TextView ID = mDialogView.findViewById(R.id.delivery_man_id);

//                fAuth = FirebaseAuth.getInstance();
//                fStore = FirebaseFirestore.getInstance();
//                userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    fullName.setText(documentSnapshot.getString("FullName"));
                    ID.setText(userId);
                }else{
                    Toast.makeText(OrderHistory.this,"Logout",Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView rcUpdatephone = mDialogView.findViewById(R.id.rcUpdatephone);
        TextView rcUpdatename = mDialogView.findViewById(R.id.rcUpdatename);
        TextView rcUpdateemail = mDialogView.findViewById(R.id.rcUpdateemail);
        TextView rcUpdatepurl = mDialogView.findViewById(R.id.rcUpdatepurl);
        TextView rcUpdatestatus = mDialogView.findViewById(R.id.rcUpdatestatus);
        TextView rcUpdatestatuscombine = mDialogView.findViewById(R.id.rcUpdatestatuscombine);
        TextView rcUpdatelatitude = mDialogView.findViewById(R.id.textlatitude);
        TextView rcUpdatelongitude = mDialogView.findViewById(R.id.textlongitude);
        TextView cuskey = mDialogView.findViewById(R.id.key);
        TextView rcUpdateitem = mDialogView.findViewById(R.id.rcUpdateitem);
        TextView rcUpdateweight = mDialogView.findViewById(R.id.rcUpdateweight);
        TextView rcUpdatedate = mDialogView.findViewById(R.id.rcUpdatedate);
        TextView rcdeliverydate = mDialogView.findViewById(R.id.rcdeliverydate);

        Button btnUpdate = mDialogView.findViewById(R.id.btnUpdate);

        rcUpdatephone.setText(phone);
        rcUpdatename.setText(name);
        rcUpdateemail.setText(email);
        rcUpdatepurl.setText(purl);

        rcUpdatestatus.setText("Delivering");
        rcUpdatestatuscombine.setText("Delivering_"+userId);

        rcUpdatelatitude.setText(latitude);
        rcUpdatelongitude.setText(longitude);
        cuskey.setText(key);
        rcUpdateitem.setText(item);
        rcUpdateweight.setText(weight);
        rcUpdatedate.setText(date);

//        mDialog.setTitle("Updating" + name +" record");
        mDialog.setTitle("Receiver Details");
        mDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here will update data in database
                //now get values from view

                String newPhone = rcUpdatephone.getText().toString();
                String newName = rcUpdatename.getText().toString();
                String newEmail = rcUpdateemail.getText().toString();
                String newPurl = rcUpdatepurl.getText().toString();
                String newStatus = rcUpdatestatus.getText().toString();

                String newStatuscombine = rcUpdatestatuscombine.getText().toString();
                String newDeliveryman = fullName.getText().toString();
                String newDeliverymanid = ID.getText().toString();
                String newLatitude = rcUpdatelatitude.getText().toString();
                String newLongitude = rcUpdatelongitude.getText().toString();

                String newkey = cuskey.getText().toString();
                String newitem = rcUpdateitem.getText().toString();
                String newweight = rcUpdateweight.getText().toString();
                String newdate = rcUpdatedate.getText().toString();

                Calendar calender = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                String dateTime = simpleDateFormat.format(calender.getTime());
                rcdeliverydate.setText(dateTime);
                String deliverydate = rcdeliverydate.getText().toString();

                updateData(newPhone,newName,newEmail,newPurl,newStatus,newStatuscombine,newDeliveryman,newDeliverymanid,newLatitude,newLongitude,newkey,newitem,newweight,newdate,deliverydate);

                Toast.makeText(OrderHistory.this,"Delivery Start", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent (OrderHistory.this,Map.class);
                intent.putExtra("Address",newPurl);
                intent.putExtra("Phone",newPhone);
                intent.putExtra("Latitude",newLatitude);
                intent.putExtra("Longitude",newLongitude);
                startActivity(intent);
//                openAddorder();

            }
        });

    }

    private void updateData(String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid, String latitude, String longitude, String key, String item, String weight, String date, String deliverydate){

        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("students").child(phone);
        Students students = new Students(phone, name, email, purl, status, statuscombine, deliveryman, deliverymanid, latitude, longitude, key, item, weight, date, deliverydate);
        DbRef.setValue(students);
    }

    public void Orderdelivering() {
        Intent intent = new Intent(this, OrderHistory.class);
        startActivity(intent);
    }

    public void Orderdone() {
        Intent intent = new Intent(this, OrderDone.class);
        startActivity(intent);
    }

    public void OrderCancel() {
        Intent intent = new Intent(this, OrderCancel.class);
        startActivity(intent);
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
                Intent intent = new Intent(this, DriverHome.class);
                startActivity(intent);
                Toast.makeText(this,"Going Home Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profile:
                Intent intent2 = new Intent(this, Profile.class);
                startActivity(intent2);
                Toast.makeText(this,"Going Profile Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.order_history:
                Intent intent3 = new Intent(this, OrderHistory.class);
                startActivity(intent3);
                Toast.makeText(this,"Going Order History Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout:
                mFirebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginDriver.class));
                Toast.makeText(this,"Log Out", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}