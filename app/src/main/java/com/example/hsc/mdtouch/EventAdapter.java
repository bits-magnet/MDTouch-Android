package com.example.hsc.mdtouch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<EventItems> {

    public EventAdapter(Activity context, ArrayList<EventItems> event) {

        super(context, 0, event);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_item, parent, false);
        }

        EventItems event = getItem(position);

        new Downloading(listItemView.findViewById(R.id.pic)).execute(event.getPic());

        TextView mTitle = (TextView) listItemView.findViewById(R.id.title);
        TextView mDescription = (TextView) listItemView.findViewById(R.id.description);
        TextView mDate = (TextView) listItemView.findViewById(R.id.date);
        TextView mRegistered = (TextView) listItemView.findViewById(R.id.registered);

        mTitle.setText(event.getTitle());
        mDescription.setText(event.getDescription());
        mDate.setText(event.getDate());
        mRegistered.setText(event.getRegistered());

        return listItemView;
    }

    public class Downloading extends AsyncTask<String,Void,Bitmap>{

        ImageView imageView;

        public Downloading(View v){
            imageView = (ImageView) v;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String url = params[0];
            Bitmap bitmap = null;

            try{

                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (IOException ignored) {

            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

}

