package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button mManager, mDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = (Button) findViewById(R.id.manager);
        mDriver = (Button) findViewById(R.id.driver);

        mManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.manager.MainActivity.this, com.example.manager.LoginManager.class);
                startActivity(intent);
                return;
            }
        });

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.manager.MainActivity.this, com.example.manager.LoginManager.class);
                startActivity(intent);
                return;
            }
        });

    }
}