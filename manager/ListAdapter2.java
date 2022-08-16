package com.example.manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListAdapter2 extends ArrayAdapter {

    private Activity mContext;
    List<com.example.manager.Users> studentsList2;
    String newdid;


    public ListAdapter2(Activity mContext, List<com.example.manager.Users> studentsList2, String newdid){
        super(mContext, R.layout.list_item2,studentsList2);
        this.mContext = mContext;
        this.studentsList2 = studentsList2;
        this.newdid = newdid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView2 = inflater.inflate(R.layout.list_item2, null, true);

        TextView dname = listItemView2.findViewById(R.id.dname);
        TextView dphone = listItemView2.findViewById(R.id.dphone);
        TextView demail = listItemView2.findViewById(R.id.demail);

        TextView did = listItemView2.findViewById(R.id.did);

        com.example.manager.Users users = studentsList2.get(position);

//        String phone = getIntent().getStringExtra("DID");
//        did.setText(phone);

        dname.setText(users.getFullName());
        dphone.setText(users.getPhoneNumber());
        demail.setText(users.getUserEmail());

        did.setText(newdid);

        return listItemView2;
    }

}













