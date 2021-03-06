package com.example.hsc.mdtouch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PatientAppointments extends AppCompatActivity {

    List<Appointment> appoints = new ArrayList<>();
    private AppointmentAdapter adapter;

    String pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_appointments_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Appointments");
        }

        ListView list = (ListView) findViewById(R.id.appointment_list);
        adapter = new AppointmentAdapter(this, new ArrayList<Appointment>());
        assert list != null;
        list.setAdapter(adapter);

        new Load().execute();

        TextView empty = (TextView) findViewById(R.id.empty);
        list.setEmptyView(empty);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View content = getLayoutInflater().inflate(R.layout.appointment_dialog,null);

                TextView date = (TextView) content.findViewById(R.id.date);
                TextView doctor= (TextView) content.findViewById(R.id.doctor);
                TextView hospital = (TextView) content.findViewById(R.id.hospital);
                TextView status = (TextView) content.findViewById(R.id.status);
                TextView problem = (TextView) content.findViewById(R.id.problem);

                date.setText(appoints.get(position).getDate());
                doctor.setText(appoints.get(position).getDoctor());
                hospital.setText(appoints.get(position).getHospital());
                status.setText(appoints.get(position).getStatus());
                problem.setText(appoints.get(position).getProblem());

                AlertDialog.Builder dialog = new AlertDialog.Builder(PatientAppointments.this);
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.setView(content);

                AlertDialog d = dialog.create();
                d.show();
            }
        });

    }


    public void BookAppointment(View v){

        Intent i = new Intent(PatientAppointments.this,BookAppointment.class);
        pId = getIntent().getExtras().getString("id");
        i.putExtra("id", pId);
        startActivityForResult(i, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1 && resultCode == RESULT_OK){

        }


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

    private class Load extends AsyncTask<Void,String,List<Appointment>> {

        ProgressDialog dialog;

        String data = "";
        String doctors = "";
        String hospitals = "";

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(PatientAppointments.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected List<Appointment> doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            data = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/appointment/?patient"+pId);

            data = "{ \"data\" :  " + data + " }";

            try{
                JSONObject json = new JSONObject(data);
                JSONArray a = json.getJSONArray("data");

                for(int i=0;i<a.length();i++){

                    JSONObject b = a.getJSONObject(i);

                    String date = b.getString("appointmentdate");
                    String doctor = b.getString("doctor");
                    String location = b.getString("location");
                    String patient = b.getString("patient");
                    String message = b.getString("message");
                    String status = b.getString("status");

                    if(status.equals("1"))
                        status = "Accepted";
                    else if(status.equals("0"))
                        status = "Pending ..";
                    else
                        status = "Rejected";

                    Appointment appoint = new Appointment(patient,location,doctor,message,date,status);
                    appoints.add(appoint);
                }
            } catch (JSONException ignored) {}

            for(int i=0;i<appoints.size();i++){

                publishProgress(""+i,""+appoints.size());

                String id = appoints.get(i).getDoctor();
                doctors = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/doctor/"+id);

                String hospitalId = appoints.get(i).getHospital();
                hospitals = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/hospital/"+hospitalId);

                try{

                    JSONObject j = new JSONObject(hospitals);
                    appoints.get(i).setHospital(j.getString("name"));

                } catch (JSONException ignored) {

                }

                try {
                    JSONObject j = new JSONObject(doctors);
                    appoints.get(i).setDoctor(j.getString("firstName") + " " + j.getString("lastName"));

                } catch (JSONException ignored) {

                }


            }

            return appoints;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            float p = 100*Float.parseFloat(values[0])/Float.parseFloat(values[1])*1.4f;

            dialog.setProgress((int)p);

        }

        @Override
        protected void onPostExecute(List<Appointment> appoints) {

            if(dialog.isShowing())
                dialog.dismiss();

            adapter.clear();
            if(appoints!=null && !appoints.isEmpty()){
                adapter.addAll(appoints);
            }
        }
    }

}
