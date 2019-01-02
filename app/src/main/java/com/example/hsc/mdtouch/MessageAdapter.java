package com.example.hsc.mdtouch;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {


    int sender ;
    String receiver ;

    public MessageAdapter(Context context, int resource, List<Message> objects,int s, String r) {
        super(context, resource, objects);

        sender = s;
        receiver = r;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message m = getItem(position);

        if (convertView == null) {
            if(sender == 1) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);

                TextView message = (TextView) convertView.findViewById(R.id.message);
                message.setText(m.getText());
            }
            else {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.other_message, parent, false);

                TextView message = (TextView) convertView.findViewById(R.id.message);
                message.setText(m.getText());

                TextView name = (TextView) convertView.findViewById(R.id.name);
                name.setText(receiver);
            }
        }

        return convertView;
    }
}
