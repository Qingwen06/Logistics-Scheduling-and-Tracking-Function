package com.example.manager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MapTracking extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    String phone;
    TextView rphone,rname,remail,raddress,deliveryman,deliverymanID,deliverymanPhone,deliverymanEmail,item,weight,date,deliverydate;
    String nlatitude,nlongitude;
    double deslatitude, deslongitude;
    MarkerOptions currentlocation;
    Polyline currentPolyline;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    MarkerOptions destination;
    private GoogleMap mMap;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    Marker myMaker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracking);

        rphone = findViewById(R.id.rphone);
        rname = findViewById(R.id.rname);
        raddress = findViewById(R.id.raddress);
        remail = findViewById(R.id.remail);
        deliveryman = findViewById(R.id.deliveryman);
        deliverymanID = findViewById(R.id.deliverymanID);
        deliverymanPhone = findViewById(R.id.deliverymanPhone);
        deliverymanEmail = findViewById(R.id.deliverymanEmail);
        item = findViewById(R.id.ritem);
        weight = findViewById(R.id.rweight);
        date = findViewById(R.id.rdate);
        deliverydate = findViewById(R.id.rdeliverydate);

        phone = getIntent().getStringExtra("Phone");
        rphone.setText(phone);

        reference = FirebaseDatabase.getInstance().getReference("students").child(phone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Students students = dataSnapshot.getValue(Students.class);
                rname.setText(students.getName());
                raddress.setText(students.getPurl());
                remail.setText(students.getEmail());
                deliveryman.setText(students.getDeliveryman());
                deliverymanID.setText(students.getDeliveryid());
                String deID = deliverymanID.getText().toString();
                item.setText(students.getItem());
                weight.setText(students.getWeight());
                date.setText(students.getDate());
                deliverydate.setText(students.getDeliverydate());

                passid(deID);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ProgressBar simpleProgressBar=(ProgressBar)findViewById(R.id.simpleProgressBar); // initiate the progress bar
        simpleProgressBar.setBackgroundColor(Color.WHITE); // black background color for the progress bar

    }

    private void passid (String deid) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("Users").document(deid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){

                    deliverymanPhone.setText(documentSnapshot.getString("PhoneNumber"));
                    deliverymanEmail.setText(documentSnapshot.getString("UserEmail"));

                }else{
                    Toast.makeText(com.example.manager.MapTracking.this,"Logout",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.van);

        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 160, false);

        TextView stlatitude = findViewById(R.id.stalatitude);
        TextView stlongitude = findViewById(R.id.stalongitude);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Students students = dataSnapshot.getValue(Students.class);

                stlatitude.setText(students.getLatitude());
                stlongitude.setText(students.getLongitude());
                nlatitude = stlatitude.getText().toString();
                nlongitude = stlongitude.getText().toString();

                deslatitude = Double.parseDouble(nlatitude);
                deslongitude = Double.parseDouble(nlongitude);

                destination = new MarkerOptions().position(new LatLng(deslatitude, deslongitude)).title("Destination");
                mMap.addMarker(destination);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference("delivering").child(phone);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {

                        MyLocation location = dataSnapshot.getValue(MyLocation.class);
                        if (location != null) {

                            if (myMaker != null) {
                                myMaker.remove();
                            }

                            myMaker = mMap.addMarker(new MarkerOptions().position((new LatLng(location.getLatitude(), location.getLongitude()))).title("Driver Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            currentlocation = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Location 1");

                            myMaker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                            LatLng currentlocation2 = new LatLng(location.getLatitude(), location.getLongitude());

                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().setAllGesturesEnabled(true);
                            mMap.getUiSettings().setCompassEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlocation2, 16), 2000, null);

                            String url = getUrl(currentlocation.getPosition(), destination.getPosition(), "driving");
                            new FetchURL(com.example.manager.MapTracking.this).execute(url, "driving");

                            TextView slatitude = findViewById(R.id.stalatitude);
                            String Tlatitude = String.valueOf(location.getLatitude());
                            slatitude.setText(Tlatitude);
                            TextView slongitude = findViewById(R.id.stalongitude);
                            String Tlongitude = String.valueOf(location.getLongitude());
                            slongitude.setText(Tlongitude);

                            TextView latitude = findViewById(R.id.endlatitude);
                            String newlatitude = getIntent().getStringExtra("Latitude");
                            latitude.setText(newlatitude);
                            TextView longitude = findViewById(R.id.endlongitude);
                            String newlongitude = getIntent().getStringExtra("Longitude");
                            longitude.setText(newlongitude);

                            TextView estimatedTime = findViewById(R.id.estimated_time);

                            double endlatitude = Double.parseDouble(newlatitude);
                            double endlongitude = Double.parseDouble(newlongitude);

                            float[] results = new float[1];
                            Location.distanceBetween(location.getLatitude(), location.getLongitude(), endlatitude, endlongitude, results);
                            float distance = results[0];
                            int distance2 = Math.round(distance);

                            if (distance >= 0) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText((distance2) + "M");
                                estimatedTime.setText("1-5 min");
                            }
                            if (distance > 500) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("500 M - 1KM");
                                estimatedTime.setText("5-8 min");
                            }
                            if (distance > 1000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("1 - 3 KM");
                                estimatedTime.setText("8-10 min");
                            }
                            if (distance > 3000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("3 - 6 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if (distance > 6000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("6 - 9 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if (distance > 9000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("9 - 12 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if (distance > 12000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("12 - 15 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if (distance > 15000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("15 - 20 KM");
                                estimatedTime.setText("15-20 min");
                            }
                            if (distance > 20000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("20 - 25 KM");
                                estimatedTime.setText("20-25 min");
                            }
                            if (distance > 25000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("25 - 30 KM");
                                estimatedTime.setText("20-25 min");
                            }
                            if (distance > 30000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("More Than 30 KM");
                                estimatedTime.setText("More than 25 min");
                            }

                        }
                    }catch(Exception e){

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}