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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderDone extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    ListView myListView2;
    List<Students2> studentsList2;
    DatabaseReference studentDbRef;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userId;

    private Button orderdelivering;
    private Button orderdone;
    private Button ordercancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_done);

        mFirebaseAuth = FirebaseAuth.getInstance();

        myListView2 = findViewById(R.id.myListView);
        studentsList2 = new ArrayList<>();
        studentDbRef = FirebaseDatabase.getInstance().getReference("students");

        myListView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position2, long id) {

                Students2 students2 = studentsList2.get(position2);
                showUpdateDialog(students2.getPhone(), students2.getName(), students2.getEmail(), students2.getPurl(), students2.getImageurl(), students2.getReceiverName(), students2.getReceiverPhone(), students2.getItem(), students2.getWeight(), students2.getDeliverydate(), students2.getCompletedate());

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

//        studentDbRef.orderByChild("statuscombine").startAt("Cancelled_"+userId).endAt("Completed / Delivered_"+userId).addValueEventListener(new ValueEventListener() {
        studentDbRef.orderByChild("statuscombine").equalTo("Completed / Delivered_"+userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsList2.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()){

                    Students2 students2 = studentDatasnap.getValue(Students2.class);
                    studentsList2.add(students2);
                }

                ListAdapter2 adapter2 = new ListAdapter2(OrderDone.this,studentsList2);
                myListView2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderdelivering = (Button) findViewById(R.id.order_delivering);
        orderdelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                Orderdelivering();
            }
        });

        orderdone = (Button) findViewById(R.id.order_done);
        orderdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                Orderdone();
            }
        });

        ordercancel = (Button) findViewById(R.id.order_cancel);
        ordercancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                OrderCancel();
            }
        });

    }

    private void showUpdateDialog(String phone, String name, String email, String purl, String imageurl, String receiverName, String receiverPhone, String item, String weight, String deliverydate, String completedate){

        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.orderdone_item, null);

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
                    Toast.makeText(OrderDone.this,"Logout",Toast.LENGTH_LONG).show();
                }
            }
        });

//        String imageurl2 = "https://firebasestorage.googleapis.com/v0/b/myproject-fyp2.appspot.com/o/image%2F1634022372881.null?alt=media&token=8c60d699-4fb1-453f-b77f-fadb112d465d";
        ImageView rImage = mDialogView.findViewById(R.id.rImage);
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference("students");
//        DatabaseReference getImage = databaseReference.child("imageurl");
//        DatabaseReference getImage = FirebaseDatabase.getInstance().getReference("students").child("imageurl");

        Picasso.get().load(imageurl).into(rImage);

//        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
////                String link = dataSnapshot.getValue(String.class);
////                Picasso.get().load(link).into(rImage);
//
////                Students2 students2 = dataSnapshot.getValue(Students2.class);
////                assert students2 != null;
//                Picasso.get().load(imageurl2).into(rImage);
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(OrderDone.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
//            }
//        });

//        TextView rcImage = mDialogView.findViewById(R.id.textView12);
//        rcImage.setText(imageurl);

        TextView rcUpdatephone = mDialogView.findViewById(R.id.rcUpdatephone);
        TextView rcUpdatename = mDialogView.findViewById(R.id.rcUpdatename);
        TextView rcUpdateemail = mDialogView.findViewById(R.id.rcUpdateemail);
        TextView rcUpdatepurl = mDialogView.findViewById(R.id.rcUpdatepurl);
        TextView rcUpdatestatus = mDialogView.findViewById(R.id.rcUpdatestatus);
        TextView rcUpdatestatuscombine = mDialogView.findViewById(R.id.rcUpdatestatuscombine);

        TextView rcReceivername = mDialogView.findViewById(R.id.rcReceivername);
        TextView rcReceiverphone = mDialogView.findViewById(R.id.rcReceiverphone);

        TextView rcItem = mDialogView.findViewById(R.id.rcItem);
        TextView rcWeight = mDialogView.findViewById(R.id.rcWeight);
        TextView rcDeliverydate = mDialogView.findViewById(R.id.rcDeliverydate);
        TextView rcCompletedate = mDialogView.findViewById(R.id.rcCompletedate);

//        Button btnUpdate = mDialogView.findViewById(R.id.btnUpdate);

        rcUpdatephone.setText(phone);
        rcUpdatename.setText(name);
        rcUpdateemail.setText(email);
        rcUpdatepurl.setText(purl);

        rcItem.setText(item);
        rcWeight.setText(weight);
        rcDeliverydate.setText(deliverydate);
        rcCompletedate.setText(completedate);

//        rcUpdatestatus.setText("Delivering");
        rcUpdatestatuscombine.setText("Delivering_"+userId);
        rcReceivername.setText(receiverName);
        rcReceiverphone.setText(receiverPhone);

//        mDialog.setTitle("Updating" + name +" record");
        mDialog.setTitle("Delivery Details");
        mDialog.show();

//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //here will update data in database
//                //now get values from view
//
//                String newPhone = rcUpdatephone.getText().toString();
//                String newName = rcUpdatename.getText().toString();
//                String newEmail = rcUpdateemail.getText().toString();
//                String newPurl = rcUpdatepurl.getText().toString();
//                String newStatus = rcUpdatestatus.getText().toString();
//
//                String newStatuscombine = rcUpdatestatuscombine.getText().toString();
//                String newDeliveryman = fullName.getText().toString();
//                String newDeliverymanid = ID.getText().toString();
//
//                updateData(newPhone,newName,newEmail,newPurl,newStatus,newStatuscombine,newDeliveryman,newDeliverymanid);
//
//                Toast.makeText(OrderDone.this,"Delivery Start", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent (OrderDone.this,Map.class);
//                intent.putExtra("Address",newPurl);
//                intent.putExtra("Phone",newPhone);
//                startActivity(intent);
////                openAddorder();
//
//            }
//        });

    }

//    private void updateData(String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid){
//
//        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("students").child(phone);
//        Students students = new Students(phone, name, email, purl, status, statuscombine, deliveryman, deliverymanid);
//        DbRef.setValue(students);
//    }

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
                Toast.makeText(this,"Going Order History Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profile:
                Intent intent2 = new Intent(this, Profile.class);
                startActivity(intent2);
                Toast.makeText(this,"Going Order History Page", Toast.LENGTH_SHORT).show();
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