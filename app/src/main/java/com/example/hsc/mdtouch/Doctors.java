package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Doctors extends AppCompatActivity {

    ArrayList<String> doctors = new ArrayList<>();
    ArrayList<String> doctorsId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            new GetDoctors().execute().get();
        } catch (InterruptedException | ExecutionException ignored) {

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.doctors_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,doctors);

        ListView listView = (ListView) findViewById(R.id.doctors_list);
        assert listView != null;
        listView.setEmptyView(findViewById(R.id.empty2));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent();
                i.putExtra("doctor", doctors.get(position));
                i.putExtra("id",doctorsId.get(position));
                setResult(RESULT_OK, i);
                finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetDoctors extends AsyncTask<Void,Void,Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(Doctors.this);
            dialog.setMessage("Getting Hospitals ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String s = getIntent().getExtras().getString("id");
            String p = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/doctor/?workplace=" + s);

            p = "{ \"data\" :  " + p + " }";


            try {
                JSONObject a = new JSONObject(p);
                JSONArray b = a.getJSONArray("data");

                for(int i=0;i<b.length();i++){

                    JSONObject c = b.getJSONObject(i);
                    String x = c.getString("firstName");
                    String y = c.getString("lastName");
                    String id = c.getString("id");

                    doctors.add(x+" "+y);
                    doctorsId.add(id);

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

        }
    }
}
