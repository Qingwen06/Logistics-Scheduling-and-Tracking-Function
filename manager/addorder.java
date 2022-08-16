package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

//import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class addorder extends FragmentActivity implements OnMapReadyCallback
{
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +    //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    boolean valid = true;
    double deslongitude,deslatitude;
    TextView newlatitude,newlongitude;
    String latitu,longitu;

    EditText add_name, add_email, add_phone, add_status, add_purl, add_latitude, add_longitude, add_item, add_weight;
    TextView add_date;
    Button submit,back;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText etPlace;
    Button btConvert;
    //TextView tvAddress,latitude,longitude;
    TextView tvAddress;

    private GoogleMap mMap;
    Marker myMaker;
    MarkerOptions currentlocation;

    //email
    EditText _txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorder);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        //testing

        etPlace = findViewById(R.id.add_purl);
        btConvert = findViewById(R.id.bt_submit);
        tvAddress = findViewById(R.id.tv_address);
        add_latitude = findViewById(R.id.latitude);
        add_longitude = findViewById(R.id.longitude);

        newlatitude = findViewById(R.id.test);
        newlongitude = findViewById(R.id.test2);

        _txtMessage = findViewById(R.id.textMessage);

        btConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etPlace.getText())){
                    etPlace.setError("Address is required");
                }else{
                    String address = etPlace.getText().toString();
                    GeoLocation geoLocation = new GeoLocation();
                    geoLocation.getAddress(address, getApplicationContext(), new GeoHandler());
                }
            }
        });

        //end testing

        add_name = findViewById(R.id.add_name);
        add_email = findViewById(R.id.add_email);
        add_phone = findViewById(R.id.add_phone);
        add_status = findViewById(R.id.add_status);
        add_purl = findViewById(R.id.add_purl);

        add_item = findViewById(R.id.add_item);
        add_weight = findViewById(R.id.add_weight);
        add_date = findViewById(R.id.add_date);

        submit = findViewById(R.id.add_submit);
        back = findViewById(R.id.add_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Order.class));
                finish();
            }
        });
        //Save data in FireBase on button click

        TextView test = findViewById(R.id.testing);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String address = etPlace.getText().toString();
//                GeoLocation geoLocation = new GeoLocation();
//                geoLocation.getAddress(address,getApplicationContext(),new GeoHandler());

                checkField(add_name,add_email,add_phone,add_status,add_purl,add_latitude,add_longitude,add_item,add_weight);

                if(valid){

                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("students");

                    //Get all the values
                    String name = add_name.getText().toString();
                    String email = add_email.getText().toString();
                    String phone = add_phone.getText().toString();
                    String status = add_status.getText().toString();
                    String purl = add_purl.getText().toString();

                    String item = add_item.getText().toString();
                    String weight = add_weight.getText().toString();

                    Calendar calender = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                    String dateTime = simpleDateFormat.format(calender.getTime());
                    add_date.setText(dateTime);
                    String date = add_date.getText().toString();

                    String latitude = add_latitude.getText().toString();
                    String longitude = add_longitude.getText().toString();

                    test.setText(getRandomString(8) + phone);
                    String key = test.getText().toString();

                    model helperClass = new model(name, email, phone, status, purl, latitude, longitude, key, item, weight, date);
                    reference.child(phone).setValue(helperClass);
                    Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();

                    final String username = "qing99wen@gmail.com";
                    final String password = "Qw19990623@";
                    _txtMessage.setText("Hi " + name + ", welcome to Logistics Scheduling and Tracking Application.\nYou can now get the latest update of your orders in our application. Below is your order details.\n\nOrder ID : " + key + "\nOrder Delivery Address : " + purl + "\nOrder Phone Number : " + phone + "\n\nAs always we're happy to help with any questions, feel free to contact us email qing99wen@gmail.com.\nHave a nice day!");
                    String messageToSend = _txtMessage.getText().toString();
                    Properties props = new Properties();
                    props.put("mail.smtp.auth","true");
                    props.put("mail.smtp.starttls.enable","true");
                    props.put("mail.smtp.host","smtp.gmail.com");
                    props.put("mail.smtp.port","587");
                    Session session = Session.getInstance(props,
                            new javax.mail.Authenticator(){
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });
                    try{
                        javax.mail.Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(username));
                        message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(add_email.getText().toString() ));
                        message.setSubject("Logistics Scheduling and Tracking Application Order Detail");
                        message.setText(messageToSend);
                        Transport.send(message);
                        Toast.makeText(getApplicationContext(), "email send successfully", Toast.LENGTH_LONG).show();

                    }catch (MessagingException e){
                        throw new RuntimeException(e);
                    }

                    startActivity(new Intent(getApplicationContext(),Order.class));
                    finish();

                }
            }
        });//Register Button method end
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }//onCreate Method End

    public boolean checkField (EditText add_name, EditText add_email, EditText add_phone, EditText add_status, EditText add_purl, EditText add_latitude, EditText add_longitude, EditText add_item, EditText add_weight){

        if(add_name.getText().toString().isEmpty()){
            add_name.setError("Receiver Name is required");
            valid = false;
        }

        if(add_phone.getText().toString().isEmpty()){
            add_phone.setError("Receiver Phone is required");
            valid = false;
        }else if(add_phone.length() < 10){
            add_phone.setError("Please fill in correct phone number\nat least 10 number");
            valid = false;
        }

        if(add_status.getText().toString().isEmpty()){
            add_status.setError("Status is required");
            valid = false;
        }

        if(add_purl.getText().toString().isEmpty()){
            add_purl.setError("Receiver Address is required");
            valid = false;
        }

        if(add_latitude.getText().toString().isEmpty()){
            add_latitude.setError("Please Convert to Latitude");
            valid = false;
        }

        if(add_longitude.getText().toString().isEmpty()){
            add_longitude.setError("Please Convert to Longitude");
            valid = false;
        }

        if(add_item.getText().toString().isEmpty()){
            add_item.setError("Please Describe the Item");
            valid = false;
        }

        if(add_weight.getText().toString().isEmpty()){
            add_weight.setError("Item Weight is required");
            valid = false;
        }

        String emailInput = add_email.getText().toString().trim();
        if(add_email.getText().toString().isEmpty()){
            add_email.setError("Receiver Email Address is required");
            valid = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            add_email.setError("Please enter a valid email address");
            valid = false;
        }

        else {
            valid = true;
        }

        return valid;
    }

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static String getRandomString(int i) {
        final String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        while (i > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString();
    }

    private class GeoHandler extends Handler implements com.example.manager.GeoHandler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String address;

            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    latitu = bundle.getString("latitude");
                    longitu = bundle.getString("longitude");
                    break;
                default:
                    address = null;
                    latitu = null;
                    longitu = null;
            }
            tvAddress.setText(address);
            add_latitude.setText(latitu);
            add_longitude.setText(longitu);

            newlatitude.setText(latitu);
            newlongitude.setText(longitu);
            String nlatitude = newlatitude.getText().toString();
            String nlongitude = newlongitude.getText().toString();
            deslatitude = Double.parseDouble(nlatitude);
            deslongitude = Double.parseDouble(nlongitude);

            if (myMaker != null) { myMaker.remove(); }

            currentlocation = new MarkerOptions().position(new LatLng(deslatitude, deslongitude));
            LatLng currentlocation = new LatLng(deslatitude, deslongitude);
            myMaker = mMap.addMarker(new MarkerOptions().position(currentlocation).title("Address Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlocation, 16), 1000, null);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng melaka = new LatLng(2.2523652999, 102.2750432);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(melaka, 8), 1000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

    }

}