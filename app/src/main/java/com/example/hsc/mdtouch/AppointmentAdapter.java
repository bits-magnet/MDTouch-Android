package com.example.hsc.mdtouch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.Color;

import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {

    public AppointmentAdapter(Activity context, ArrayList<Appointment> appoint) {

        super(context, 0, appoint);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.appointments_list, parent, false);
        }

        Appointment appoint = getItem(position);

        TextView mDoctor = (TextView) listItemView.findViewById(R.id.with);
        String doctor = appoint.getDoctor();
        mDoctor.setText("Appointment with " + doctor);

        TextView mDate = (TextView) listItemView.findViewById(R.id.date);
        String date = appoint.getDate();
        mDate.setText("Date : "+date);

        TextView mStatus = (TextView) listItemView.findViewById(R.id.status);
        String status = appoint.getStatus();
        mStatus.setText(status);

        if(status.equals("Accepted"))
            mStatus.setTextColor(Color.GREEN);
        else if(status.equals("Rejected"))
            mStatus.setTextColor(Color.RED);

        return listItemView;
    }
}