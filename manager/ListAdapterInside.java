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

public class ListAdapterInside extends ArrayAdapter {

    private Activity mContext;
    List<Students> studentsListInside;

    public ListAdapterInside(Activity mContext, List<Students> studentsListInside){
        super(mContext, R.layout.list_item_inside,studentsListInside);
        this.mContext = mContext;
        this.studentsListInside = studentsListInside;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemViewInside = inflater.inflate(R.layout.list_item_inside, null, true);

        TextView rcphone = listItemViewInside.findViewById(R.id.rcphone);
        TextView rcname = listItemViewInside.findViewById(R.id.rcname);
        TextView rcemail = listItemViewInside.findViewById(R.id.rcemail);
        TextView rcpurl = listItemViewInside.findViewById(R.id.rcpurl);
        TextView rcstatus = listItemViewInside.findViewById(R.id.rcstatus);

        Students students = studentsListInside.get(position);

        rcphone.setText(students.getPhone());
        rcname.setText(students.getName());
        rcemail.setText(students.getEmail());
        rcpurl.setText(students.getPurl());
        rcstatus.setText(students.getStatus());



        return listItemViewInside;
    }

}













