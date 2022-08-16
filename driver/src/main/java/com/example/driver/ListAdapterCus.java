package com.example.driver;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.driver.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListAdapterCus extends ArrayAdapter {

    private Activity mContext;
    List<Studentscus> studentsList;


    public ListAdapterCus(Activity mContext, List<Studentscus> studentsList){
        super(mContext, R.layout.list_cusitem,studentsList);
        this.mContext = mContext;
        this.studentsList = studentsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_cusitem, null, true);

        TextView rcphone = listItemView.findViewById(R.id.rcphone);
        TextView rcname = listItemView.findViewById(R.id.rcname);
        TextView rcemail = listItemView.findViewById(R.id.rcemail);
        TextView rcpurl = listItemView.findViewById(R.id.rcpurl);
        TextView rcstatus = listItemView.findViewById(R.id.rcstatus);

        Studentscus studentscus = studentsList.get(position);

        rcphone.setText(studentscus.getPhone());
        rcname.setText(studentscus.getName());
        rcemail.setText(studentscus.getEmail());
        rcpurl.setText(studentscus.getPurl());

        if(studentscus.getStatus().equals("Free")){
            rcstatus.setText("Delivery Start Soon");
        }else {
            rcstatus.setText(studentscus.getStatus());
        }


        return listItemView;
    }

}













