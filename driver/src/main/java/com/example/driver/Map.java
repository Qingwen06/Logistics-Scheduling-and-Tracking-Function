package com.example.driver;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//start
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.driver.R;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Map extends FragmentActivity implements OnMapReadyCallback, LocationListener, TaskLoadedCallback {

    //testing
    MarkerOptions place1, place2;
    //    MarkerOptions destination,currentlocation;
    MarkerOptions destination;
    MarkerOptions currentlocation;
    Button getDirection;
    Polyline currentPolyline;
    //end testing

    private GoogleMap mMap;

    private DatabaseReference reference;
    private LocationManager manager;

    private final int MIN_TIME = 1000; //1sec
    private final int MIN_DISTANCE = 1; //1meter

    Marker myMaker;


    //EditText etSource,etDestination;
    TextView etSource,etDestination,etphone,etlatitude,etlongitude;
    Button btTrack,next;

    //testingtestingtestingtestingtesting

    ArrayList<MarkerOptions> arrayList=new ArrayList<MarkerOptions>();

//    LatLng destination = new LatLng(2.2524, 102.2731);


    //endtestingendtestingendtestingendtesting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //testing
//        getDirection = findViewById(R.id.btnGetDirection);
//        getDirection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new FetchURL(Map.this).execute(getUrl(currentlocation.getPosition(), destination.getPosition(), "driving"), "driving");
//            }
//        });

//        String url = getUrl(currentlocation.getPosition(), destination.getPosition(), "driving");
//        new FetchURL(Map.this).execute(url, "driving");

        //27.658143,85.3199503
        //27.667491,85.3208583
//        destination = new MarkerOptions().position(new LatLng(2.2524, 102.2731)).title("Location 1");
//        arrayList.add(destination);

//        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location 2");


        //end testing

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        etSource = findViewById(R.id.et_source);
        etDestination = findViewById(R.id.et_destination);

        etphone = findViewById(R.id.r_phone);
        etlatitude = findViewById(R.id.et_latitude);
        etlongitude = findViewById(R.id.et_longitude);

        String address = getIntent().getStringExtra("Address");
        etDestination.setText(address);
        String phone = getIntent().getStringExtra("Phone");
        etphone.setText(phone);

        reference = FirebaseDatabase.getInstance().getReference("delivering").child(phone);
        // FirebaseDatabase.getInstance().getReference().setValue("this is tracker app");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
        getLocationUpdates();

        readChanges();

//testingtestingtestingtestingtestingtestingtestingtestingtesting



        btTrack = findViewById(R.id.bt_track);
        next = findViewById(R.id.next);

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sSource = etSource.getText().toString().trim();
                String sDestination = etDestination.getText().toString().trim();

                if (sSource.equals("") && sDestination.equals("")){

                    Toast.makeText(getApplicationContext(), "Enter both location",Toast.LENGTH_SHORT).show();
                }else {
                    DisplayTrack(sSource,sDestination);
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Reach.class);
                intent.putExtra("Address",address);
                intent.putExtra("Phone",phone);
                startActivity(intent);
            }
        });


//endtestingendtestingendtestingendtestingendtestingendtestingendtesting

        //new testing code

//        arrayList.add(destination);

        //end new testing code

    }

    //testingtestingtestingtestingtestingtestingtestingtestingtesting
    private void DisplayTrack(String sSource, String sDestination) {

        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
//endtestingendtestingendtestingendtestingendtestingendtestingendtesting

    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        MyLocation location = dataSnapshot.getValue(MyLocation.class);
                        if (location != null) {

                            currentlocation = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Location 1");

                            String url = getUrl(currentlocation.getPosition(), destination.getPosition(), "driving");
                            new FetchURL(Map.this).execute(url, "driving");

                            myMaker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
//                            LatLng currentlocation = new LatLng(location.getLatitude(), location.getLongitude());
                            LatLng currentlocation = new LatLng(location.getLatitude(), location.getLongitude());
//                            currentlocation = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
//                            arrayList.add(currentlocation);

//                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentlocation, 18);
//                            mMap.animateCamera(cameraUpdate);

//                            for (int i=0; i<arrayList.size(); i++) {
//                                mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Destination"));
//                                mMap.addMarker(arrayList.get(i));
//                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
//                                mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
//                            }

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 17),2000,null);

                            //calculate distance

                            double startLatitude = location.getLatitude();
                            double startLongitude = location.getLongitude();

                            String latitude = getIntent().getStringExtra("Latitude");
                            String longitude = getIntent().getStringExtra("Longitude");
                            double endLatitude = Double.parseDouble(latitude);
                            double endLongitude = Double.parseDouble(longitude);

                            float[] results = new float[1];
                            Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
                            float distance = results[0];
                            int distance2 = Math.round(distance);
                    //        int kilometre = (int) (distance/1000);

                            TextView estimatedTime = findViewById(R.id.estimated_time);
//                            if(distance < 1000){
//                                TextView totaldistance = findViewById(R.id.rdistance);
//                                totaldistance.setText(String.valueOf(distance)+"M");
//                            }else{
//                                int kilometre = (int) (distance/1000);
//                                TextView totaldistance = findViewById(R.id.rdistance);
//                                totaldistance.setText(String.valueOf(kilometre)+"KM");
//                            }

                            if(distance >= 0) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText((distance2)+"M");
                                estimatedTime.setText("1-5 min");
                            }
                            if(distance > 500) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("500 M - 1KM");
                                estimatedTime.setText("5-8 min");
                            }
                            if(distance > 1000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("1 - 3 KM");
                                estimatedTime.setText("8-10 min");
                            }
                            if(distance > 3000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("3 - 6 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if(distance > 6000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("6 - 9 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if(distance > 9000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("9 - 12 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if(distance > 12000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("12 - 15 KM");
                                estimatedTime.setText("10-15 min");
                            }
                            if(distance > 15000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("15 - 20 KM");
                                estimatedTime.setText("15-20 min");
                            }
                            if(distance > 20000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("20 - 25 KM");
                                estimatedTime.setText("20-25 min");
                            }
                            if(distance > 25000) {
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("25 - 30 KM");
                                estimatedTime.setText("20-25 min");
                            }
                            if(distance > 30000){
                                TextView totaldistance = findViewById(R.id.rdistance);
                                totaldistance.setText("More Than 30 KM");
                                estimatedTime.setText("More than 25 min");
                            }


                        }
                    } catch (Exception e) {
//                        Toast.makeText(Map.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLocationUpdates() {
        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                } else {
                    Toast.makeText(this, "No Provider Enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates();
            } else {
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.van);
//        Bitmap b = bitmapdraw.getBitmap();
//        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);
//
//        // Add a marker in Sydney and move the camera
//        LatLng melaka = new LatLng(2.2082, 102.2676);
//        myMaker = mMap.addMarker(new MarkerOptions().position(melaka).title("Current Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(melaka));
//
////        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
////        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(melaka, 17);
////        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(melaka, 17),1000,null);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.van);

        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 120, 160, false);

        LatLng melaka = new LatLng(2.2082, 102.2676);
        myMaker = mMap.addMarker(new MarkerOptions().position(melaka).title("Current Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);


        String latitude = getIntent().getStringExtra("Latitude");
        etlatitude.setText(latitude);
        String longitude = getIntent().getStringExtra("Longitude");
        etlongitude.setText(longitude);

        double deslatitude = Double.parseDouble(latitude);
        double deslongitude = Double.parseDouble(longitude);

        Log.d("mylog", "Added Markers");
        destination = new MarkerOptions().position(new LatLng(deslatitude, deslongitude)).title("Destination");
        mMap.addMarker(destination);

//        mMap.addMarker(place2);
//        LatLng melaka = new LatLng(27.658143, 85.3199503);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(melaka, 15),1000,null);

        //calculate distance

//        double startLatitude = 2.2524;
//        double startLongitude = 102.2750;
//        double endLatitude = 2.2522934;
//        double endLongitude = 102.2748819;


//        float[] results = new float[1];
//        Location.distanceBetween(startLatitude,startLongitude,endLatitude,endLongitude,results);
//        float distance = results[0];
////        int kilometre = (int) (distance/1000);
//
////        Toast.makeText(this, (distance)+" M", Toast.LENGTH_SHORT).show();
//        TextView totaldistance = findViewById(R.id.rdistance);
//        totaldistance.setText(String.valueOf(results[0]));

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
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null){
            saveLocation(location);
        }else{
            Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(Location location) {
        reference.setValue(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}