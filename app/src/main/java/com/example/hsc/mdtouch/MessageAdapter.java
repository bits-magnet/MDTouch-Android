package com.example.hsc.mdtouch;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.other_message, parent, false);
        }

        TextView message = (TextView) convertView.findViewById(R.id.message);
        //TextView name = (TextView) convertView.findViewById(R.id.name);

        Message m = getItem(position);
        message.setText(m.getText());

        return convertView;
    }
}
