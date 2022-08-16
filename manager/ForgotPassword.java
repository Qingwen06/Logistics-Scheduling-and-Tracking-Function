package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    boolean valid = true;
    private EditText email;
    private Button resetPassword;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.reset_email);
        resetPassword = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(email);
                if(valid){
                    progressBar.setVisibility(View.VISIBLE);
                    String nemail = email.getText().toString().trim();
                    auth.sendPasswordResetEmail(nemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this,"Check your email to reset your password!", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(ForgotPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    startActivity(new Intent(getApplicationContext(),Login.class));
                }
            }
        });

    }

    public boolean checkField(EditText email){

        String nemail = email.getText().toString().trim();
        if(nemail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            valid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(nemail).matches()){
            email.setError("Plaese provide valid email");
            email.requestFocus();
            valid = false;
        }

        else{
            valid = true;
        }

        return valid;
    }

}










