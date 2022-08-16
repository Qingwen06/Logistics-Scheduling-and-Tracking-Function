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

public class ListAdapter extends ArrayAdapter {

    private Activity mContext;
    List<Students> studentsList;


    public ListAdapter(Activity mContext, List<Students> studentsList){
        super(mContext, R.layout.list_item,studentsList);
        this.mContext = mContext;
        this.studentsList = studentsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_item, null, true);

        TextView rcphone = listItemView.findViewById(R.id.rcphone);
        TextView rcname = listItemView.findViewById(R.id.rcname);
        TextView rcemail = listItemView.findViewById(R.id.rcemail);
        TextView rcpurl = listItemView.findViewById(R.id.rcpurl);
        TextView rcstatus = listItemView.findViewById(R.id.rcstatus);

        Students students = studentsList.get(position);

        rcphone.setText(students.getPhone());
        rcname.setText(students.getName());
        rcemail.setText(students.getEmail());
        rcpurl.setText(students.getPurl());
        rcstatus.setText(students.getStatus());

        return listItemView;
    }

}













