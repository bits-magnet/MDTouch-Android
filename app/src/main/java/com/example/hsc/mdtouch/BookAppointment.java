package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookAppointment extends AppCompatActivity {

    ArrayList<String> hospitals = new ArrayList<>();
    Spinner hospital;
    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appointment_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        new GetHospitals().execute();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {

        }

        hospital = (Spinner) findViewById(R.id.hospital_list);
        adp = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,hospitals);
        hospital.setAdapter(adp);

    }

    public void RequestDoctor(View v){

        /*
        EditText patient = (EditText) findViewById(R.id.patient_name);
        EditText problem = (EditText) findViewById(R.id.problem);
        Spinner doctor = (Spinner) findViewById(R.id.doctor_list);
           */

        Intent i = new Intent(BookAppointment.this,Chat.class);
        startActivity(i);

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

    private class GetHospitals extends AsyncTask<Void,Void,Boolean> {

        private String URL = "https://mdtouch.herokuapp.com/MDTouch/api/hospital/";

        String s1,s2;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(BookAppointment.this);
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String jsonStr = "{ \"data\":" + helper.get(URL) +"}";

            try{

                JSONObject obj = new JSONObject(jsonStr);
                JSONArray arr = obj.getJSONArray("data");

                for(int i=0; i<arr.length(); i++) {

                    JSONObject o = arr.getJSONObject(i);

                    s1 = o.getString("name");
                    s2 = o.getString("city");

                    hospitals.add(s1+", "+s2);

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
