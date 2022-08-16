package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterDriver extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText mDriverEmail, mDriverPassword, mDriverPhone, mDriverName;
    private Button mDriverRegisterButton, mLoginDriverButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(com.example.manager.RegisterDriver.this, com.example.manager.ManagerHome.class);
                    startActivity(intent);
                    return;
                }
            }
        };

        mDriverEmail = (EditText) findViewById(R.id.DriverEmail);
        mDriverPassword = (EditText) findViewById(R.id.DriverPassword);
        mDriverPhone = (EditText) findViewById(R.id.DriverPhone);
        mDriverName = (EditText) findViewById(R.id.DriverName);

        mDriverRegisterButton = (Button) findViewById(R.id.DriverRegisterButton);

        mDriverRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String DriverEmail = mDriverEmail.getText().toString();
                final String DriverPassword = mDriverPassword.getText().toString();
                final String DriverName = mDriverName.getText().toString();
                final String DriverPhone = mDriverPhone.getText().toString();

                if(TextUtils.isEmpty(DriverEmail)){
                    mDriverEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(DriverPassword)){
                    mDriverPassword.setError("Password is Required.");
                    return;
                }
                if(DriverPassword.length() < 6){
                    mDriverPassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                if(TextUtils.isEmpty(DriverName)){
                    mDriverName.setError("Name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(DriverPhone)){
                    mDriverPhone.setError("Phone Number is Required.");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(DriverEmail, DriverPassword).addOnCompleteListener(com.example.manager.RegisterDriver.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(com.example.manager.RegisterDriver.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{

                            String driver_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driver_id);
                            current_user_db.setValue(true);

                            Map<String,Object> user = new HashMap<>();
                            user.put("DName",DriverName);
                            user.put("DEmail",DriverEmail);
                            user.put("DPhone",DriverPhone);
                            current_user_db.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ driver_id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), com.example.manager.RegisterDriver.class));

                        }
                    }
                });
            }
        });

        mLoginDriverButton = (Button) findViewById(R.id.LoginDriverButton);
        mLoginDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                LoginDriverAcc();
            }
        });
    }

    public void LoginDriverAcc() {
//        Intent intent = new Intent(this, LoginDriver.class);
//        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

}