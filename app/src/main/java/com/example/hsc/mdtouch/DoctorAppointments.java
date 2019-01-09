package com.example.hsc.mdtouch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointments extends AppCompatActivity {

    List<Appointment> appoints = new ArrayList<>();
    private AppointmentAdapter adapter;
    ArrayList<String> AId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.doctor_appointments_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Appointments");
        }

        ListView list = (ListView) findViewById(R.id.doctor_appointment_list);
        adapter = new AppointmentAdapter(this, new ArrayList<Appointment>());
        assert list != null;
        list.setAdapter(adapter);

        new Load().execute();

        TextView empty = (TextView) findViewById(R.id.empty);
        list.setEmptyView(empty);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                View content = getLayoutInflater().inflate(R.layout.appointment_dialog,null);

                TextView date = (TextView) content.findViewById(R.id.date);
                TextView patient= (TextView) content.findViewById(R.id.doctor);
                TextView hospital = (TextView) content.findViewById(R.id.hospital);
                TextView patientText = (TextView)content.findViewById(R.id.patient_text);
                TextView status = (TextView) content.findViewById(R.id.status);
                TextView problem = (TextView) content.findViewById(R.id.problem);

                patientText.setText("Patient : " );

                hospital.setText(getIntent().getExtras().getString("hospital"));
                date.setText(appoints.get(position).getDate());
                patient.setText(appoints.get(position).getPatient());
                status.setText(appoints.get(position).getStatus());
                problem.setText(appoints.get(position).getProblem());

                AlertDialog.Builder dialog = new AlertDialog.Builder(DoctorAppointments.this);

                dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Put(1, position).execute();

                    }
                });

                dialog.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Put(2, position).execute();

                    }
                });

                dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
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

    private class Put extends AsyncTask<Void,Void,Void>{

        int value,position;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DoctorAppointments.this);
            dialog.setMessage("Updating ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        public Put(int value,int position){
            this.value=value;
            this.position=position;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();
            helper.put("https://mdtouch.herokuapp.com/MDTouch/api/appointment/" + AId.get(position), value);

            Log.i("TAG",helper.get("https://mdtouch.herokuapp.com/MDTouch/api/appointment/" + AId.get(position)));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }

    private class Load extends AsyncTask<Void,String,List<Appointment>>{

        ProgressDialog dialog;
        String data = "";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(DoctorAppointments.this);
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected List<Appointment> doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String id = getIntent().getExtras().getString("id");

            String patientId = "" ;

            data = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/appointment/?status=0&doctor=" + id);

            data = "{ \"data\" :  " + data + " }";

            try {
                JSONObject json = new JSONObject(data);
                JSONArray a = json.getJSONArray("data");

                for (int i = 0; i < a.length(); i++) {

                    JSONObject b = a.getJSONObject(i);

                    String date = b.getString("appointmentdate");
                    patientId = b.getString("patient");
                    String message = b.getString("message");
                    String status = b.getString("status");
                    String Aid = b.getString("id");

                    AId.add(Aid);

                    if (status.equals("1"))
                        status = "Accepted";
                    else if (status.equals("0"))
                        status = "Pending ..";
                    else
                        status = "Rejected";

                    Appointment appoint = new Appointment(patientId, "", "", message, date, status);
                    appoints.add(appoint);
                }
            } catch (JSONException ignored) {
            }

            for (int i = 0; i < appoints.size(); i++) {

                String patient = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/patient/" +patientId);


                try{

                    JSONObject j = new JSONObject(patient);
                    appoints.get(i).setPatient(j.getString("firstName") + " " + j.getString("lastName"));


                } catch (JSONException ignored) {}

            }

            return appoints;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
