package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginManager extends AppCompatActivity {
    private EditText mManagerEmail, mManagerPassword;
    private Button mManagerLogin, mCreateManagerButton;
    private ImageView mHomeButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_manager);

        mAuth = FirebaseAuth.getInstance();

//        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if(user!=null){
//                    Intent intent = new Intent(LoginManager.this, Map.class);
//                    startActivity(intent);
//                    return;
//                }
//            }
//        };

        mManagerEmail = (EditText) findViewById(R.id.ManagerEmail);
        mManagerPassword = (EditText) findViewById(R.id.ManagerPassword);

        mManagerLogin = (Button) findViewById(R.id.ManagerLogin);

        mManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ManagerEmail = mManagerEmail.getText().toString();
                final String ManagerPassword = mManagerPassword.getText().toString();

                if(TextUtils.isEmpty(ManagerEmail)){
                    mManagerEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(ManagerPassword)){
                    mManagerPassword.setError("Password is Required.");
                    return;
                }

                mAuth.signInWithEmailAndPassword(ManagerEmail, ManagerPassword).addOnCompleteListener(com.example.manager.LoginManager.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(com.example.manager.LoginManager.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mCreateManagerButton = (Button) findViewById(R.id.CreateManagerButton);
        mCreateManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateManagerAcc();
            }
        });

        mHomeButton = (ImageView) findViewById(R.id.HomeButton);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                HomeButton();
            }
        });

    }

    public void CreateManagerAcc() {
        Intent intent = new Intent(this, RegisterManager.class);
        startActivity(intent);
    }

    public void HomeButton() {
        Intent intent = new Intent(this, MainActivity.class);
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