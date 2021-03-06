package com.example.hsc.mdtouch;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DoctorProfile extends AppCompatActivity {

    String id,data = "";

    String q,s,h,name,city;
    TextView qualification,specialization,hospital,mName;
    String Qid="",Sid="",Hid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.doctor_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MDTouch");
        }
        toolbar.setTitleTextColor(Color.WHITE);

        id = getIntent().getExtras().getString("id");

        mName = (TextView) findViewById(R.id.name);
        qualification = (TextView) findViewById(R.id.qualification);
        specialization = (TextView) findViewById(R.id.specialization);
        hospital = (TextView) findViewById(R.id.hospital);

        new Load().execute();

    }

    public class Load extends AsyncTask<Void,Void,Void> {

        String str1,str2,str3;
        JSONObject j1,j2,j3;

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(DoctorProfile.this);
            dialog.setCancelable(false);
            dialog.setTitle("Loading ...");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            data = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/doctor/?username="+id);

            data = data.substring(1,data.length()-1);

            try {
                JSONObject json = new JSONObject(data);

                id = json.getString("id");
                Sid = json.getString("specialization");
                Hid = json.getString("workplace");
                Qid = json.getString("qualification");


                String fn,ln;
                fn = json.getString("firstName");
                ln = json.getString("lastName");
                name = fn+" "+ln;

            } catch (JSONException ignored) {

            }

            str1 = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/specialization/"+Sid);
            str2 = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/qualification/"+Qid);
            str3 = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/hospital/"+Hid);

            try {
                j1 = new JSONObject(str1);
                j2 = new JSONObject(str2);
                j3 = new JSONObject(str3);

                s = j1.getString("skill");
                q = j2.getString("degree");
                h = j3.getString("name");
                city = j3.getString("city");

            } catch (JSONException ignored) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            qualification.setText(q);
            specialization.setText(s);
            hospital.setText(h+", "+city);
            mName.setText(name);

            dialog.dismiss();

        }
    }

    public void Appointments(View v){

        Intent i = new Intent(DoctorProfile.this,DoctorAppointments.class);
        i.putExtra("id",id);
        i.putExtra("hospital",h+", "+city);
        startActivity(i);
    }

    public void YourPatients(View v){

        Intent i = new Intent(DoctorProfile.this,MyPatients.class);
        i.putExtra("name",name);
        i.putExtra("id",id);
        startActivity(i);

    }

    public void ChatDoctors(View v){

        Intent i = new Intent(DoctorProfile.this,ChatDoctors.class);
        i.putExtra("id",id);
        i.putExtra("Hid",Hid);
        i.putExtra("name",name);
        startActivity(i);

    }

    public void MedicalServices(View v){

        Intent i = new Intent(DoctorProfile.this,MedicalServices.class);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout:

                SessionManager session = new SessionManager(getApplicationContext());
                session.clearSession();

                Intent i = new Intent(DoctorProfile.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

                return true;

            case R.id.report:

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setView(R.layout.report);

                dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
