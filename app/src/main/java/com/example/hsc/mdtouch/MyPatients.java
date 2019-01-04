package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyPatients extends AppCompatActivity {

    ArrayList<String> patients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        try {
            new Load().execute().get();
        } catch (InterruptedException | ExecutionException ignored) {

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_patients_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Patients");
        }

        ListView list = (ListView) findViewById(R.id.my_patients_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MyPatients.this,R.layout.list_item,patients);
        assert list != null;
        list.setAdapter(adapter);

        TextView empty = (TextView) findViewById(R.id.empty_my_patients);
        list.setEmptyView(empty);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MyPatients.this,Chat.class);
                i.putExtra("receiver",patients.get(position));

                String name = getIntent().getExtras().getString("name");
                i.putExtra("sender",name);

                startActivity(i);

            }
        });
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

    public class Load extends AsyncTask<Void,String,Void> {

        ProgressDialog dialog;
        String data = "",patient = "";

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(MyPatients.this);
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String id = getIntent().getExtras().getString("id");

            data = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/appointment/?doctor="+id+"&status=1");

            data = "{ \"data\" :  " + data + " }";

            try{

                JSONObject json = new JSONObject(data);
                JSONArray a = json.getJSONArray("data");

                for(int i=0;i<a.length();i++){

                    JSONObject b = a.getJSONObject(i);

                    patient = b.getString("patient");

                    patients.add(patient);

                }

            } catch (JSONException ignored) {}

            for(int i=0;i<patients.size();i++){

                patient = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/patient/"+patients.get(i));

                Log.i("TAG", "" + patient);

                try {
                    JSONObject j = new JSONObject(patient);
                    patients.set(i, j.getString("firstName") + " " + j.getString("lastName"));
                    Log.i("TAG", "im done");

                } catch (JSONException ignored) {}
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(dialog.isShowing())
                dialog.dismiss();
        }


    }

}
