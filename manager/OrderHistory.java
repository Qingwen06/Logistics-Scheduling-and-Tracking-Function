package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class OrderHistory extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    RecyclerView recview;
    myadapter2 adapter;
//    FloatingActionButton fb;

    private ImageButton buttonhome;
    private Button buttonorder;
    private Button buttonorderhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mFirebaseAuth = FirebaseAuth.getInstance();

        buttonhome = (ImageButton) findViewById(R.id.home);
        buttonhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonhome();
            }
        });

        buttonorder = (Button) findViewById(R.id.order);
        buttonorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonorder();
            }
        });

        buttonorderhistory = (Button) findViewById(R.id.orderhistory);
        buttonorderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonorderhistory();
            }
        });

//        setTitle("Search here..");

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

            FirebaseRecyclerOptions<com.example.manager.model2> options =
                    new FirebaseRecyclerOptions.Builder<com.example.manager.model2>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("students")
                            .orderByChild("status")
                            .startAt("Cancelled")
                            .endAt("Completed / Delivered"), com.example.manager.model2.class)
                            .build();

        adapter=new myadapter2(options);
        recview.setAdapter(adapter);


    }

    public void buttonhome() {
        Intent intent = new Intent(this, com.example.manager.Admin.class);
        startActivity(intent);
    }

    public void buttonorder() {
        Intent intent = new Intent(this, com.example.manager.Order.class);
        startActivity(intent);
    }

    public void buttonorderhistory() {
        Intent intent = new Intent(this, com.example.manager.OrderHistory.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.searchmenu,menu);
//
//        MenuItem item=menu.findItem(R.id.search);
//
//        SearchView searchView=(SearchView)item.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//
//                processsearch(s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                processsearch(s);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    private void processsearch(String s)
//    {
//        FirebaseRecyclerOptions<model2> options =
//                new FirebaseRecyclerOptions.Builder<model2>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students").orderByChild("status").startAt(s).endAt(s+"\uf8ff"), model2.class)
//                        .build();
//
//        adapter=new myadapter2(options);
//        adapter.startListening();
//        recview.setAdapter(adapter);
//
//    }

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
                Intent intent = new Intent(this, com.example.manager.Admin.class);
                startActivity(intent);
                Toast.makeText(this,"Going Home Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.driver:
                Intent intent2 = new Intent(this, com.example.manager.Driver.class);
                startActivity(intent2);
                Toast.makeText(this,"Going Driver Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.order:
                Intent intent3 = new Intent(this, com.example.manager.Order.class);
                startActivity(intent3);
                Toast.makeText(this,"Going Order Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profile:
                Intent intent4 = new Intent(this, com.example.manager.Profile.class);
                startActivity(intent4);
                Toast.makeText(this,"Going Profile Page", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout:
                mFirebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), com.example.manager.Login.class));
                Toast.makeText(this,"Log Out", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}