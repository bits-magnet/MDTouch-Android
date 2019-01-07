package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Events extends AppCompatActivity {

    EventAdapter adapter;
    ArrayList<EventItems> events = new ArrayList<EventItems>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        try {
            new Load().execute().get();
        } catch (InterruptedException | ExecutionException ignored) {

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Events");
        }

        adapter = new EventAdapter(this, new ArrayList<EventItems>());

        ListView listView = (ListView) findViewById(R.id.events_list);
        assert listView != null;
        listView.setEmptyView(findViewById(R.id.empty_events));
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Load extends AsyncTask<Void,Void,Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(Events.this);
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String p = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/event/");

            p = "{ \"data\" :  " + p + " }";


            try {
                JSONObject a = new JSONObject(p);
                JSONArray b = a.getJSONArray("data");

                for(int i=0;i<b.length();i++){

                    JSONObject c = b.getJSONObject(i);
                    String el = c.getString("eventlocation");
                    String ci = c.getString("city");
                    String st = c.getString("state");
                    String ti = c.getString("title");
                    String de = c.getString("description");
                    String pic = c.getString("pic");
                    String da = c.getString("dateofevent");
                    String re = c.getString("totalregistered");
                    String hid = c.getString("hospitalid");
                    String bid = c.getString("bloodbankid");
                    String did = c.getString("dispensaryid");
                    String tid = c.getString("testcentreid");

                    Log.i("TAG", ci);

                    EventItems e = new EventItems(el,ci,st,ti,de,pic,da,re,hid,bid,did,tid);
                    events.add(e);

                }

            } catch (JSONException ignored) {
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(dialog.isShowing())
                dialog.dismiss();

            adapter.clear();
            if(events!=null && !events.isEmpty()){
                adapter.addAll(events);
            }
        }
    }

}
