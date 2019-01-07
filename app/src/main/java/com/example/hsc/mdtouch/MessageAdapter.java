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

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message m = getItem(position);

        if (convertView == null && m != null) {
            if( m.getSender().equals("p")) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.my_message, parent, false);

                TextView message = (TextView) convertView.findViewById(R.id.message);
                message.setText(m.getText());
            }
            else {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.other_message, parent, false);

                TextView message = (TextView) convertView.findViewById(R.id.message);
                message.setText(m.getText());

                TextView name = (TextView) convertView.findViewById(R.id.name);
                name.setText("Other");
            }
        }

        return convertView;
    }
}
