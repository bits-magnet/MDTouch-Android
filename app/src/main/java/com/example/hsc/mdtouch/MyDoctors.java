package com.example.hsc.mdtouch;

import android.graphics.Color;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class MyDoctors extends AppCompatActivity {

    List<Appointment> doctors = new ArrayList<>();
    private DoctorAdapter adapter;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_doctors_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Doctors");
        }

        ListView list = (ListView) findViewById(R.id.my_doctors_list);
        adapter = new DoctorAdapter(this, new ArrayList<Appointment>());
        assert list != null;
        list.setAdapter(adapter);

        new Load().execute();

        TextView empty = (TextView) findViewById(R.id.empty_my_doctors);
        list.setEmptyView(empty);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MyDoctors.this,Chat.class);
                i.putExtra("receiver",doctors.get(position).getDoctor());

                i.putExtra("user","p");

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

    public class Load extends AsyncTask<Void,String,List<Appointment>>{

        ProgressDialog dialog;
        String data = "",doctor = "", hospital = "";


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(MyDoctors.this);
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected List<Appointment> doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            id = getIntent().getExtras().getString("id");

            data = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/appointment/?patient="+id+"&status=1");

            data = "{ \"data\" :  " + data + " }";

            try{

                JSONObject json = new JSONObject(data);
                JSONArray a = json.getJSONArray("data");

                for(int i=0;i<a.length();i++){

                    JSONObject b = a.getJSONObject(i);

                    String doctor = b.getString("doctor");
                    String location = b.getString("location");

                    Appointment mDoctor = new Appointment(doctor,location);
                    doctors.add(mDoctor);

                }

            } catch (JSONException ignored) {}

            for(int i=0;i<doctors.size();i++){

                String id = doctors.get(i).getDoctor();
                doctor = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/doctor/"+id);

                String hospitalId = doctors.get(i).getHospital();
                hospital = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/hospital/"+hospitalId);

                try{

                    JSONObject j = new JSONObject(hospital);
                    doctors.get(i).setHospital(j.getString("name"));

                } catch (JSONException ignored) {}

                try {
                    JSONObject j = new JSONObject(doctor);
                    doctors.get(i).setDoctor(j.getString("firstName") + " " + j.getString("lastName"));

                } catch (JSONException ignored) {}
            }

            return doctors;

        }


        @Override
        protected void onPostExecute(List<Appointment> doctors) {

            if(dialog.isShowing())
                dialog.dismiss();

            adapter.clear();
            if(doctors!=null && !doctors.isEmpty()){
                adapter.addAll(doctors);
            }
        }
    }

}