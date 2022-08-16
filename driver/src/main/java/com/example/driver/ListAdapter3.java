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

public class ListAdapter3 extends ArrayAdapter {

    private Activity mContext;
    List<Students3> studentsList3;


    public ListAdapter3(Activity mContext, List<Students3> studentsList3){
        super(mContext, R.layout.list_item_cancel,studentsList3);
        this.mContext = mContext;
        this.studentsList3 = studentsList3;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_item_cancel, null, true);

        TextView rcphone = listItemView.findViewById(R.id.rcphone);
        TextView rcname = listItemView.findViewById(R.id.rcname);
        TextView rcemail = listItemView.findViewById(R.id.rcemail);
        TextView rcpurl = listItemView.findViewById(R.id.rcpurl);
        TextView rcstatus = listItemView.findViewById(R.id.rcstatus);

        Students3 students3 = studentsList3.get(position);

        rcphone.setText(students3.getPhone());
        rcname.setText(students3.getName());
        rcemail.setText(students3.getEmail());
        rcpurl.setText(students3.getPurl());
        rcstatus.setText(students3.getStatus());

        return listItemView;
    }

}