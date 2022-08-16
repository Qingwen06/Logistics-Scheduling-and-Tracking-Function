package com.example.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class myadapter2 extends FirebaseRecyclerAdapter<com.example.manager.model2, com.example.manager.myadapter2.myviewholder>
{

    FirebaseFirestore fStore;

    private ListenerRegistration registration;

    public myadapter2(@NonNull FirebaseRecyclerOptions<com.example.manager.model2> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final com.example.manager.model2 model2)
    {
        holder.name.setText(model2.getName());
        holder.status.setText(model2.getStatus());
        holder.email.setText(model2.getEmail());
        holder.phone.setText(model2.getPhone());
        Glide.with(holder.img.getContext()).load(model2.getPurl()).into(holder.img);

        final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                .setExpanded(true,2000)
                .create();
        View myview=dialogPlus.getHolderView();
        final TextView status=myview.findViewById(R.id.ustatus);
        status.setText(model2.getStatus());

        String dstatus = status.getText().toString();

//        String dstatus = "Completed";

        if (dstatus.equals("Cancelled")) {

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialogcontent3))
                            .setExpanded(true,2100)
                            .create();

                    View myview=dialogPlus.getHolderView();
                    final TextView purl=myview.findViewById(R.id.uimgurl);
                    final TextView name=myview.findViewById(R.id.uname);
                    final TextView status=myview.findViewById(R.id.ustatus);
                    final TextView email=myview.findViewById(R.id.uemail);
                    final TextView phone=myview.findViewById(R.id.uphone);
                    final TextView reason=myview.findViewById(R.id.reason);
                    final TextView Item=myview.findViewById(R.id.uitem);
                    final TextView Weight=myview.findViewById(R.id.uweight);
                    final TextView Date=myview.findViewById(R.id.udate);
                    final TextView Deliverydate=myview.findViewById(R.id.udeliverydate);
                    final TextView Canceldate=myview.findViewById(R.id.ucanceldate);

                    purl.setText(model2.getPurl());
                    name.setText(model2.getName());
                    status.setText(model2.getStatus());
                    email.setText(model2.getEmail());
                    phone.setText(model2.getPhone());
                    reason.setText(model2.getReason());

                    TextView dname = myview.findViewById(R.id.dname);
                    TextView demail = myview.findViewById(R.id.demail);
                    TextView dphone = myview.findViewById(R.id.dphone);

                    Item.setText(model2.getItem());
                    Weight.setText(model2.getWeight());
                    Date.setText(model2.getDate());
                    Deliverydate.setText(model2.getDeliverydate());
                    Canceldate.setText(model2.getCanceldate());

                    fStore = FirebaseFirestore.getInstance();
                    final DocumentReference docRef = fStore.collection("Users").document(model2.getDeliveryid());
                    registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            dname.setText(value.getString("FullName"));
                            demail.setText(value.getString("UserEmail"));
                            dphone.setText(value.getString("PhoneNumber"));
                        }
                    });

                    dialogPlus.show();
                }
            });

        }

        if (dstatus.equals("Completed")) {

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialogcontent2))
                            .setExpanded(true,2100)
                            .create();

                    View myview=dialogPlus.getHolderView();
                    final TextView purl=myview.findViewById(R.id.uimgurl);
                    final TextView name=myview.findViewById(R.id.uname);
                    final TextView status=myview.findViewById(R.id.ustatus);
                    final TextView email=myview.findViewById(R.id.uemail);
                    final TextView phone=myview.findViewById(R.id.uphone);
                    final TextView rname=myview.findViewById(R.id.rname);
                    final TextView rphone=myview.findViewById(R.id.rphone);
                    final TextView rIC=myview.findViewById(R.id.rIC);
                    final ImageView rImage=myview.findViewById(R.id.rImage);
                    final TextView Item=myview.findViewById(R.id.uitem);
                    final TextView Weight=myview.findViewById(R.id.uweight);
                    final TextView Date=myview.findViewById(R.id.udate);
                    final TextView Deliverydate=myview.findViewById(R.id.udeliverydate);
                    final TextView Completedate=myview.findViewById(R.id.ucompletedate);

                    purl.setText(model2.getPurl());
                    name.setText(model2.getName());
                    status.setText(model2.getStatus());
                    email.setText(model2.getEmail());
                    phone.setText(model2.getPhone());
                    rname.setText(model2.getReceiverName());
                    rphone.setText(model2.getReceiverPhone());
                    rIC.setText(model2.getReceiverIC());

                    Picasso.get().load(model2.getImageurl()).into(rImage);

                    Item.setText(model2.getItem());
                    Weight.setText(model2.getWeight());
                    Date.setText(model2.getDate());
                    Deliverydate.setText(model2.getDeliverydate());
                    Completedate.setText(model2.getCompletedate());

                    TextView dname = myview.findViewById(R.id.dname);
                    TextView demail = myview.findViewById(R.id.demail);
                    TextView dphone = myview.findViewById(R.id.dphone);

                    fStore = FirebaseFirestore.getInstance();
                    final DocumentReference docRef = fStore.collection("Users").document(model2.getDeliveryid());
                    registration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            dname.setText(value.getString("FullName"));
                            demail.setText(value.getString("UserEmail"));
                            dphone.setText(value.getString("PhoneNumber"));
                        }
                    });

                    dialogPlus.show();
                }
            });

        }


//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.dialogcontent2))
//                        .setExpanded(true,2000)
//                        .create();
//
//                View myview=dialogPlus.getHolderView();
//                final TextView purl=myview.findViewById(R.id.uimgurl);
//                final TextView name=myview.findViewById(R.id.uname);
//                final TextView status=myview.findViewById(R.id.ustatus);
//                final TextView email=myview.findViewById(R.id.uemail);
//                final TextView phone=myview.findViewById(R.id.uphone);
////                Button submit=myview.findViewById(R.id.usubmit);
//
//                purl.setText(model2.getPurl());
//                name.setText(model2.getName());
//                status.setText(model2.getStatus());
//                email.setText(model2.getEmail());
//                phone.setText(model2.getPhone());
//
//                dialogPlus.show();

                ////////submit update

//                submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Map<String,Object> map=new HashMap<>();
//                        map.put("purl",purl.getText().toString());
//                        map.put("name",name.getText().toString());
//                        map.put("email",email.getText().toString());
//                        map.put("status",status.getText().toString());
//                        map.put("phone",phone.getText().toString());
//
//                        FirebaseDatabase.getInstance().getReference().child("students")
//                                .child(getRef(position).getKey()).updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        dialogPlus.dismiss();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        dialogPlus.dismiss();
//                                    }
//                                });
//                    }
//                });


//            }
//        });


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
//                builder.setTitle("Delete Panel");
//                builder.setMessage("Delete...?");
//
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        FirebaseDatabase.getInstance().getReference().child("students")
//                                .child(getRef(position).getKey()).removeValue();
//                    }
//                });
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//                builder.show();
//            }
//        });

    } // End of OnBindViewMethod


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow2,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        //        ImageView edit,delete;
        ImageView edit;
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
//            delete=(ImageView)itemView.findViewById(R.id.deleteicon);
        }
    }
}
