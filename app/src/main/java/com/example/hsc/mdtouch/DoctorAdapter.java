package com.example.hsc.mdtouch;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class DoctorAdapter extends ArrayAdapter<Appointment> {

    public DoctorAdapter(Activity context, ArrayList<Appointment> appoint) {
        super(context, 0, appoint);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.my_doctors_list, parent, false);
        }

        Appointment appoint = getItem(position);

        TextView mName = (TextView) listItemView.findViewById(R.id.my_doctor);
        String name = appoint.getDoctor();
        mName.setText(name);

        TextView mHospital = (TextView) listItemView.findViewById(R.id.my_hospital);
        String hospital = appoint.getHospital();
        mHospital.setText(hospital);

        return listItemView;
    }

}