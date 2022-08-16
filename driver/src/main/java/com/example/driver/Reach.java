package com.example.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.gcacace.signaturepad.views.SignaturePad;
import com.example.driver.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Reach extends AppCompatActivity {

    TextView etphone,etaddress;
    boolean valid = true;

    DatabaseReference studentDbRef;
    DatabaseReference reff;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    private Uri imageUri;
    private String imageUrl;
    private ImageView itemAdded;
    ImageButton btReset;
    ImageButton btBrowse;

//    RelativeLayout signaturepadLayout;
//    SignaturePad signaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reach);

        itemAdded = findViewById(R.id.item_added);

        etaddress = findViewById(R.id.r_address);
        etphone = findViewById(R.id.r_phone);

        String passaddress = getIntent().getStringExtra("Address");
        etaddress.setText(passaddress);
        String passphone = getIntent().getStringExtra("Phone");
        etphone.setText(passphone);

        TextView fullName = findViewById(R.id.rdrivername);
        TextView ID = findViewById(R.id.rdriverid);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    fullName.setText(documentSnapshot.getString("FullName"));
                    ID.setText(userId);
                }else{
                    Toast.makeText(Reach.this,"Logout",Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView rUpdatephone = findViewById(R.id.rphone);
        TextView rUpdatename = findViewById(R.id.rname);
        TextView rUpdateemail = findViewById(R.id.remail);
        TextView rUpdatepurl = findViewById(R.id.raddress);
        TextView rUpdatestatus = findViewById(R.id.rstatus);
        TextView rUpdatestatuscombine = findViewById(R.id.rstatuscombine);

        TextView rcUpdatelatitude = findViewById(R.id.rlatitude);
        TextView rcUpdatelongitude = findViewById(R.id.rlongitude);

        TextView rckey = findViewById(R.id.key);

        TextView rcUpdateitem = findViewById(R.id.ritem);
        TextView rcUpdateweight = findViewById(R.id.rweight);
        TextView rcUpdatedate = findViewById(R.id.rdate);
        TextView deliverydate = findViewById(R.id.rdeliverydate);
        TextView completedate = findViewById(R.id.rcompletedate);

        Button btnComplete = findViewById(R.id.bcomplete);
        Button btnCancell = findViewById(R.id.bcancell);

        reff = FirebaseDatabase.getInstance().getReference().child("students").child(passphone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rphone = dataSnapshot.child("phone").getValue().toString();
                String rname = dataSnapshot.child("name").getValue().toString();
                String remail = dataSnapshot.child("email").getValue().toString();
                String raddress = dataSnapshot.child("purl").getValue().toString();
                String rlatitude = dataSnapshot.child("latitude").getValue().toString();
                String rlongitude = dataSnapshot.child("longitude").getValue().toString();
                String rkey = dataSnapshot.child("key").getValue().toString();

                String ritem = dataSnapshot.child("item").getValue().toString();
                String rweight = dataSnapshot.child("weight").getValue().toString();
                String rdate = dataSnapshot.child("date").getValue().toString();
                String rdeliverydate = dataSnapshot.child("deliverydate").getValue().toString();

                rUpdatephone.setText(rphone);
                rUpdatename.setText(rname);
                rUpdateemail.setText(remail);
                rUpdatepurl.setText(raddress);

                rcUpdatelatitude.setText(rlatitude);
                rcUpdatelongitude.setText(rlongitude);

                rckey.setText(rkey);

                rcUpdateitem.setText(ritem);
                rcUpdateweight.setText(rweight);
                rcUpdatedate.setText(rdate);
                deliverydate.setText(rdeliverydate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here will update data in database
                //now get values from view
                rUpdatestatus.setText("Completed");
                rUpdatestatuscombine.setText("Completed / Delivered_"+userId);

                String newPhone = rUpdatephone.getText().toString();
                String newName = rUpdatename.getText().toString();
                String newEmail = rUpdateemail.getText().toString();
                String newPurl = rUpdatepurl.getText().toString();
                String newStatus = rUpdatestatus.getText().toString();
                String newStatuscombine = rUpdatestatuscombine.getText().toString();
                String newDeliveryman = fullName.getText().toString();
                String newDeliverymanid = ID.getText().toString();
                String newLatitude = rcUpdatelatitude.getText().toString();
                String newLongitude = rcUpdatelongitude.getText().toString();
                String newkey = rckey.getText().toString();

                String newitem = rcUpdateitem.getText().toString();
                String newweight = rcUpdateweight.getText().toString();
                String newdate = rcUpdatedate.getText().toString();
                String newdeliverydate = deliverydate.getText().toString();
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                String dateTime = simpleDateFormat.format(calender.getTime());
                completedate.setText(dateTime);
                String newcompletedate = completedate.getText().toString();



                EditText receiverName = findViewById(R.id.receivername);
                EditText receiverPhone = findViewById(R.id.receiverphone);
                EditText receiverIC = findViewById(R.id.receiveric);

                checkField(receiverName,receiverIC,receiverPhone);

                if(valid){
                    upload(newPhone,newName,newEmail,newPurl,newStatus,newStatuscombine,newDeliveryman,newDeliverymanid,newLatitude,newLongitude,newkey,newitem,newweight,newdate,newdeliverydate,newcompletedate);
                }


            }
        });

        btnCancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Reach.this, DeliveryCancel.class);
                intent.putExtra("Phone",passphone);
                startActivity(intent);

            }
        });

        btBrowse = findViewById(R.id.bt_browser);
        //        CropImage.activity().start(Reach.this);
        btReset = findViewById(R.id.bt_reset);

        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(Reach.this);
            }
        });
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAdded.setImageBitmap(null);
            }
        });

    }

    public boolean checkField (EditText receiverName, EditText receiverIC, EditText receiverPhone) {

        if(TextUtils.isEmpty(receiverName.getText())) {
            receiverName.setError("Receiver Name is required");
            valid = false;
        }

        if(receiverPhone.getText().toString().isEmpty()){
            receiverPhone.setError("Receiver Phone Number is required");
            valid = false;
        }else if(receiverPhone.length() < 10){
            receiverPhone.setError("Please fill in correct phone number\nat least 10 number");
            valid = false;
        }

        if(TextUtils.isEmpty(receiverIC.getText())){
            receiverIC.setError("Receiver IC Last Four Digit is required");
            valid = false;
        }else if(receiverIC.length() < 4){
            receiverIC.setError("Please fill in 4 Digit of Last IC Number");
            valid = false;
        }

            return valid;
    }


        private void updateData(String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid, String latitude, String longitude, String key, String item, String weight, String date, String deliverydate){

        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("students").child(phone);
        Students students = new Students(phone, name, email, purl, status, statuscombine, deliveryman, deliverymanid, latitude, longitude, key, item, weight, date, deliverydate);
        DbRef.setValue(students);
    }

    public void completeOrder() {
        Intent intent = new Intent(this, DriverHome.class);
        startActivity(intent);
    }

    private void upload(String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid, String latitude, String longitude, String key, String item, String weight, String date, String deliverydate, String completedate) {

        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setMessage("Uploading");
//        pd.show();

        if (imageUri != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("image").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            StorageTask uploadtask = filePath.putFile(imageUri);
            uploadtask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()){
                    throw task.getException();
                }

                return filePath.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("students").child(phone);
//                    String postId = ref.push().getKey();

                    TextView receiverName = findViewById(R.id.receivername);
                    TextView receiverPhone = findViewById(R.id.receiverphone);
                    TextView receiverIC = findViewById(R.id.receiveric);
                    String rName = receiverName.getText().toString();
                    String rPhone = receiverPhone.getText().toString();
                    String rIC = receiverIC.getText().toString();



                    HashMap<String , Object> map = new HashMap<>();
//                    map.put("postid" , postId);
//                    map.put("publisher" , FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("imageurl" , imageUrl);
                    map.put("phone" , phone);
                    map.put("name" , name);
                    map.put("email" , email);
                    map.put("purl" , purl);
                    map.put("status" , status);
                    map.put("statuscombine" , statuscombine);
                    map.put("deliveryman" , deliveryman);
                    map.put("deliveryid" , deliverymanid);
                    map.put("latitude" , latitude);
                    map.put("longitude" , longitude);
                    map.put("receiverName" , rName);
                    map.put("receiverPhone" , rPhone);
                    map.put("receiverIC" , rIC);
                    map.put("key" , key);

                    map.put("item" , item);
                    map.put("weight" , weight);
                    map.put("date" , date);
                    map.put("deliverydate" , deliverydate);
                    map.put("completedate" , completedate);

                    ref.setValue(map);

//                    DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("students");
//                    List<String> hashTags = phone.getHashtags();
//                    if (!hashTags.isEmpty()){
//                        for (String tag : hashTags){
//                            map.clear();
//
//                            map.put("tag" , tag.toLowerCase());
//                            map.put("postid" , postId);
//
//                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
//                        }
//                    }

//                    pd.dismiss();
//                    startActivity(new Intent(SellActivity.this , BSActivity.class));
//                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Reach.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            Toast.makeText(Reach.this,"Delivery Complete", Toast.LENGTH_SHORT).show();
            completeOrder();

        } else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            itemAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(Reach.this , BSActivity.class));
//            finish();
        }
    }

}



