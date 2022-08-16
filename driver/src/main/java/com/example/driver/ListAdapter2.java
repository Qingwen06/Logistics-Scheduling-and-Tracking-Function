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

public class ListAdapter2 extends ArrayAdapter {

    private Activity mContext;
    List<Students2> studentsList2;


    public ListAdapter2(Activity mContext, List<Students2> studentsList2){
        super(mContext, R.layout.list_item_done,studentsList2);
        this.mContext = mContext;
        this.studentsList2 = studentsList2;
    }

    @NonNull
    @Override
    public View getView(int position2, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_item_done, null, true);

        TextView rcphone = listItemView.findViewById(R.id.rcphone);
        TextView rcname = listItemView.findViewById(R.id.rcname);
        TextView rcemail = listItemView.findViewById(R.id.rcemail);
        TextView rcpurl = listItemView.findViewById(R.id.rcpurl);
        TextView rcstatus = listItemView.findViewById(R.id.rcstatus);

        Students2 students2 = studentsList2.get(position2);

        rcphone.setText(students2.getPhone());
        rcname.setText(students2.getName());
        rcemail.setText(students2.getEmail());
        rcpurl.setText(students2.getPurl());
        rcstatus.setText(students2.getStatus());

        return listItemView;
    }

}