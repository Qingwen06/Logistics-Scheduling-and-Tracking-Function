package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterManagerDriver extends AppCompatActivity {

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

    TextInputLayout eye,errorPhone,errorName,errorEmail;
    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isTeacherBox, isStudentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager_driver);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);

        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);

        isTeacherBox = findViewById(R.id.isTeacher);
        isStudentBox = findViewById(R.id.isStudent);
        eye = findViewById(R.id.eye);
        errorName = findViewById(R.id.ErrorName);
        errorEmail = findViewById(R.id.ErrorEmail);
        errorPhone = findViewById(R.id.ErrorPhone);

        isStudentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isTeacherBox.setChecked(false);
                }
            }
        });

        isTeacherBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isStudentBox.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkField(fullName);
//                checkField(email);
//                checkField(password);
//                checkField(phone);
                checkField(fullName,email,password,phone);

                //checkbox validation
                if(!(isTeacherBox.isChecked() || isStudentBox.isChecked())){
                    Toast.makeText(com.example.manager.RegisterManagerDriver.this, "Select The Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid){
                    // start the user registration process
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(com.example.manager.RegisterManagerDriver.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("UserEmail",email.getText().toString());
                            userInfo.put("PhoneNumber",phone.getText().toString());
                            // specify if the user is admin
                            if(isTeacherBox.isChecked()){
                                userInfo.put("isTeacher", "1");
                            }
                            if(isStudentBox.isChecked()){
                                userInfo.put("isStudent", "1");
                            }

                            df.set(userInfo);
                            if(isTeacherBox.isChecked()){

                                EditText _txtMessage = findViewById(R.id.textMessage);
                                final String username = "qing99wen@gmail.com";
                                final String epassword = "Qw19990623@";
                                String newname = fullName.getText().toString();
                                String newemail = email.getText().toString();
                                String newpassword = password.getText().toString();
                                String newphone = phone.getText().toString();
                                _txtMessage.setText("Hi " + newname + ", welcome to Logistics Scheduling and Tracking Application. You have registered as a Manager. Please download Manager application to perform task.  Below is your account information.\n\nAccount ID : " + newemail + "\nAccount Password : " + newpassword + "\nAccount Phone Number : " + newphone + "\n\nAs always we're happy to help with any questions, feel free to contact us email qing99wen@gmail.com.\nHave a nice day!");
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
                                                return new PasswordAuthentication(username, epassword);
                                            }
                                        });
                                try{
                                    javax.mail.Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(username));
                                    message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email.getText().toString() ));
                                    message.setSubject("Logistics Scheduling and Tracking Application Account Information");
                                    message.setText(messageToSend);
                                    Transport.send(message);
                                    Toast.makeText(getApplicationContext(), "email send succesfully", Toast.LENGTH_LONG).show();
                                }catch (MessagingException e){
                                    throw new RuntimeException(e);
                                }

                                startActivity(new Intent(getApplicationContext(), com.example.manager.Admin.class));
                                finish();
                            }
                            if(isStudentBox.isChecked()){

                                EditText _txtMessage = findViewById(R.id.textMessage);
                                final String username = "qing99wen@gmail.com";
                                final String epassword = "Qw19990623@";
                                String newname = fullName.getText().toString();
                                String newemail = email.getText().toString();
                                String newpassword = password.getText().toString();
                                String newphone = phone.getText().toString();
                                _txtMessage.setText("Hi " + newname + ", welcome to Logistics Scheduling and Tracking Application. You have registered as a Driver. Please download Driver application to perform delivery task. Below is your account information.\n\nAccount ID : " + newemail + "\nAccount Password : " + newpassword + "\nAccount Phone Number : " + newphone + "\n\nAs always we're happy to help with any questions, feel free to contact us email qing99wen@gmail.com.\nHave a nice day!");
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
                                                return new PasswordAuthentication(username, epassword);
                                            }
                                        });
                                try{
                                    javax.mail.Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress(username));
                                    message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email.getText().toString() ));
                                    message.setSubject("Logistics Scheduling and Tracking Application Order Detail");
                                    message.setText(messageToSend);
                                    Transport.send(message);
                                    Toast.makeText(getApplicationContext(), "email send succesfully", Toast.LENGTH_LONG).show();
                                }catch (MessagingException e){
                                    throw new RuntimeException(e);
                                }

                                startActivity(new Intent(getApplicationContext(), com.example.manager.Home.class));
                                finish();
                            }



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.manager.RegisterManagerDriver.this, "Please insert a valid email", Toast.LENGTH_SHORT).show();
                        }
                    });
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), com.example.manager.Login.class));
            }
        });
    }

    public boolean checkField(EditText fullName, EditText email, EditText password, EditText phone){

        if(fullName.getText().toString().isEmpty()){
            errorName.setError("Please fill in Full Name");
            valid = false;
        }

        String emailInput = email.getText().toString().trim();
        if(email.getText().toString().isEmpty()){
            errorEmail.setError("Please fill in Email Address");
            valid = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            errorEmail.setError("Please enter a valid email address");
            valid = false;
        }

        String passwordInput = password.getText().toString().trim();
        if(password.getText().toString().isEmpty()){
            eye.setError("Please fill in Password");
            password.setTransformationMethod(null);
            valid = false;
        }
        if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            eye.setError("Password too weak\nat least 1 lower case letter\nat least 1 upper case letter\nat least 1 special character\nat least 8 characters\nno white spaces");
            password.setTransformationMethod(null);
            valid = false;
        }

        String phoneInput = phone.getText().toString().trim();

        if(phone.getText().toString().isEmpty()){
            errorPhone.setError("Please fill in Phone Number");
            valid = false;
        }else if(phoneInput.length() < 10){
            errorPhone.setError("Please fill in correct phone number\nat least 10 number");
        }

        else {
            password.setTransformationMethod(new PasswordTransformationMethod());
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



}









































