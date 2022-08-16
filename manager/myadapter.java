package com.example.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.hdodenhof.circleimageview.CircleImageView;


public class myadapter extends FirebaseRecyclerAdapter<model, com.example.manager.myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options)
    {
        super(options);
    }

    EditText purl;
    Button btConvert;

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final model model)
    {
        holder.name.setText(model.getName());
        holder.status.setText(model.getStatus());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getPhone());
        Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true,1600)
                        .create();

                View myview=dialogPlus.getHolderView();
                purl=myview.findViewById(R.id.uimgurl);
                final EditText name=myview.findViewById(R.id.uname);
                final TextView status=myview.findViewById(R.id.ustatus);
                final EditText email=myview.findViewById(R.id.uemail);
                final EditText phone=myview.findViewById(R.id.uphone);
                final EditText item=myview.findViewById(R.id.uitem);
                final EditText weight=myview.findViewById(R.id.uweight);
                final TextView date=myview.findViewById(R.id.udate);
                Button submit=myview.findViewById(R.id.usubmit);

                final EditText _txtMessage = myview.findViewById(R.id.textMessage);
                final TextView test = myview.findViewById(R.id.testing);

                purl.setText(model.getPurl());
                name.setText(model.getName());
                status.setText(model.getStatus());
                email.setText(model.getEmail());
                phone.setText(model.getPhone());
                item.setText(model.getItem());
                weight.setText(model.getWeight());
                date.setText(model.getDate());
                test.setText(getRandomString(8) + model.getPhone());
                dialogPlus.show();


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("purl",purl.getText().toString());
                        map.put("name",name.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("phone",phone.getText().toString());
                        map.put("status",status.getText().toString());
                        map.put("item",item.getText().toString());
                        map.put("weight",weight.getText().toString());
                        map.put("date",date.getText().toString());
                        map.put("key",test.getText().toString());

                        String newname = name.getText().toString();
                        String newpurl = purl.getText().toString();
                        String newphone = phone.getText().toString();


                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });

                        final String username = "qing99wen@gmail.com";
                        final String password = "Qw19990623@";


                        String newkey = test.getText().toString();

                        _txtMessage.setText("Hi " + newname + ", welcome to Logistics Scheduling and Tracking Application.\nYou can now get the latest update of your orders in our application. Below is your order details.\n\nOrder ID : " + newkey + "\nOrder Delivery Address : " + newpurl + "\nOrder Phone Number : " + newphone + "\n\nAs always we're happy to help with any questions, feel free to contact us email qing99wen@gmail.com.\nHave a nice day!");
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
                            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email.getText().toString() ));
                            message.setSubject("Logistics Scheduling and Tracking Application Order Detail");
                            message.setText(messageToSend);
                            Transport.send(message);
//                            Toast.makeText(getApplicationContext(), "email send succesfully", Toast.LENGTH_LONG).show();

                        }catch (MessagingException e){
                            throw new RuntimeException(e);
                        }
                    }
                });
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete Order");
                builder.setMessage("Are you sure to delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

    } // End of OnBindViewMethod

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

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        ImageView edit,delete;
        TextView name,status,email,phone;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView) itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.nametext);
            status=(TextView)itemView.findViewById(R.id.statustext);
            email=(TextView)itemView.findViewById(R.id.emailtext);
            phone=(TextView)itemView.findViewById(R.id.phonetext);

            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);
        }
    }

}
