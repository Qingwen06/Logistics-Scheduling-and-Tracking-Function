package com.example.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.driver.R;

public class MainActivity extends AppCompatActivity {

    private Button mCustomer, mDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomer = (Button) findViewById(R.id.customer);
        mDriver = (Button) findViewById(R.id.driver);

        mCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Customer.class);
                startActivity(intent);
                return;
            }
        });

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginDriver.class);
                startActivity(intent);
                return;
            }
        });
    }
}