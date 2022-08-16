package com.example.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    TextView ID,email,existingname;
    EditText fullName,phone;
    Button btn_update;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.driver_name);
        phone = findViewById(R.id.driver_phone);
        email = findViewById(R.id.driver_email);
        ID = findViewById(R.id.driver_id);
        existingname = findViewById(R.id.existingName);

        btn_update = findViewById(R.id.btn_update);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    fullName.setText(documentSnapshot.getString("FullName"));
                    phone.setText(documentSnapshot.getString("PhoneNumber"));
                    email.setText(documentSnapshot.getString("UserEmail"));
                    ID.setText(userId);
                    existingname.setText(documentSnapshot.getString("FullName"));

                    btn_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String fullname = fullName.getText().toString();
                            String newEmail = email.getText().toString();
                            String newPhone = phone.getText().toString();
                            String existingName = existingname.getText().toString();

                            if(TextUtils.isEmpty(fullName.getText()) || TextUtils.isEmpty(phone.getText()) ){

                                fullName.setError("Full Name cannot be null");
                                phone.setError("Phone Number cannot be null");

                            }else{
                                Updatedata(fullname,newEmail,newPhone,existingName);
                            }


                        }
                    });





                }else{
                    Toast.makeText(Profile.this,"Logout",Toast.LENGTH_LONG).show();
                }

            }
        });

//        Button btn_update = findViewById(R.id.btn_update);
//        btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String existing = existingName.getText().toString();
//                String newName = fullName.getText().toString();
//                existingName.setText("");
//                fullName.setText("");
//
//                Updatedata(existing,newName);
//
//            }
//        });

    }

    private void Updatedata(String fullname, String newEmail, String newPhone, String existingName) {

        Map<String, Object> userDetail = new HashMap<>();

        userDetail.put("FullName",fullname);
        userDetail.put("PhoneNumber",newPhone);

        db = FirebaseFirestore.getInstance();
        db.collection("Users").whereEqualTo("FullName",existingName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("Users").document(documentID).update(userDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Profile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Some Error Occurred",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(Profile.this, "Failed",Toast.LENGTH_SHORT).show();
                }

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