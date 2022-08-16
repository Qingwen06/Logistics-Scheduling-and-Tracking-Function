package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterManager extends AppCompatActivity {

    private EditText mManagerEmail, mManagerPassword, mManagerPhone, mManagerName;
    private Button mManagerRegisterButton, mLoginManagerButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(com.example.manager.RegisterManager.this, com.example.manager.ManagerHome.class);
                    startActivity(intent);
                    return;
                }
            }
        };

        mManagerEmail = (EditText) findViewById(R.id.ManagerEmail);
        mManagerPassword = (EditText) findViewById(R.id.ManagerPassword);
        mManagerPhone = (EditText) findViewById(R.id.ManagerPhone);
        mManagerName = (EditText) findViewById(R.id.ManagerName);

        mManagerRegisterButton = (Button) findViewById(R.id.ManagerRegisterButton);

        mManagerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ManagerEmail = mManagerEmail.getText().toString();
                final String ManagerPassword = mManagerPassword.getText().toString();
                final String ManagerName = mManagerName.getText().toString();
                final String ManagerPhone = mManagerPhone.getText().toString();

                if(TextUtils.isEmpty(ManagerEmail)){
                    mManagerEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(ManagerPassword)){
                    mManagerPassword.setError("Password is Required.");
                    return;
                }
                if(ManagerPassword.length() < 6){
                    mManagerPassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                if(TextUtils.isEmpty(ManagerName)){
                    mManagerName.setError("Name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(ManagerPhone)){
                    mManagerPhone.setError("Phone Number is Required.");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(ManagerEmail, ManagerPassword).addOnCompleteListener(com.example.manager.RegisterManager.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(com.example.manager.RegisterManager.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            String manager_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Managers").child(manager_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });

        mLoginManagerButton = (Button) findViewById(R.id.LoginManagerButton);
        mLoginManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                LoginManagerAcc();
            }
        });
    }

    public void LoginManagerAcc() {
        Intent intent = new Intent(this, com.example.manager.LoginManager.class);
        startActivity(intent);
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