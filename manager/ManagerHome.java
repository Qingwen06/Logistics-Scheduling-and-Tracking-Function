package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ManagerHome extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private TextView mManagerEmail, mManagerLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        mManagerEmail = findViewById(R.id.ManagerEmail);
        mManagerLogOut = findViewById(R.id.ManagerLogOut);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null){
            mManagerEmail.setText(mFirebaseUser.getEmail());
        }else{
            startActivity(new Intent(this,LoginManager.class));
            finish();
        }
    }

    public void logout (View view) {
        mFirebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(),LoginManager.class));
    }

}