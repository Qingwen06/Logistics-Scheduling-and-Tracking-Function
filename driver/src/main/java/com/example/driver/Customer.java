package com.example.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Customer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        EditText cusFind = findViewById(R.id.cus_find);
//        EditText key = findViewById(R.id.key);
        Button Find = findViewById(R.id.find);

        Find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(TextUtils.isEmpty(cusFind.getText()) || TextUtils.isEmpty(key.getText()) ){
                if(TextUtils.isEmpty(cusFind.getText()) ){
                    cusFind.setError("OrderID is required");
//                    key.setError("Key is required");
                }else{

                    String textFind = cusFind.getText().toString();
//                    String keyFind = key.getText().toString();

                    Intent intent = new Intent(Customer.this, CusResult.class);
                    intent.putExtra("textFind",textFind);
//                    intent.putExtra("keyFind",keyFind);
                    startActivity(intent);
                    return;

                }

            }
        });

    }

}