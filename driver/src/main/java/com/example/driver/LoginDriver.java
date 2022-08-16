package com.example.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginDriver extends AppCompatActivity {

    TextInputLayout eye,errorEmail;
    boolean valid = true;
    TextView forgotPassword;
    private EditText mDriverEmail, mDriverPassword;
    private Button mDriverLogin, mCreateDriverButton;
    private ImageView mHomeButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_driver);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(LoginDriver.this, DriverHome.class);
                    startActivity(intent);
                    return;
                }
            }
        };

        mDriverEmail = (EditText) findViewById(R.id.DriverEmail);
        mDriverPassword = (EditText) findViewById(R.id.DriverPassword);
        eye = (TextInputLayout) findViewById(R.id.eye);
        errorEmail = (TextInputLayout) findViewById(R.id.ErrorEmail);

        mDriverLogin = (Button) findViewById(R.id.DriverLogin);

        forgotPassword = (TextView) findViewById(R.id.reset_password);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginDriver.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        mDriverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {

                final String DriverEmail = mDriverEmail.getText().toString();
                final String DriverPassword = mDriverPassword.getText().toString();

                checkfield(DriverEmail,DriverPassword);
                if(valid) {
                    mAuth.signInWithEmailAndPassword(DriverEmail, DriverPassword).addOnCompleteListener(LoginDriver.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginDriver.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                                errorEmail.setError("Please fill in Correct Email");
                                mDriverPassword.setTransformationMethod(null);
                            }
                        }


                    });
                }
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

    public boolean checkfield(String DriverEmail, String DriverPassword){

        if(DriverEmail.isEmpty()){
            errorEmail.setError("Please fill in Correct Email");
            valid = false;
        }

        if(DriverPassword.isEmpty()){
            eye.setError("Please fill in Correct password");
            mDriverPassword.setTransformationMethod(null);
            valid = false;
        }

        else {
            mDriverPassword.setTransformationMethod(new PasswordTransformationMethod());
            valid = true;
        }


        return valid;
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