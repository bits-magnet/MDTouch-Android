package com.example.hsc.mdtouch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PatientProfile extends AppCompatActivity {

    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MDTouch");
        }
        toolbar.setTitleTextColor(Color.WHITE);

        s1 = getIntent().getExtras().getString("name");
        String s2 = getIntent().getExtras().getString("number");

        TextView name = (TextView) findViewById(R.id.name);
        TextView number = (TextView) findViewById(R.id.number);

        assert name != null;
        name.setText(s1);
        assert number != null;
        number.setText(s2);

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

                Intent i = new Intent(PatientProfile.this,MainActivity.class);
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

    public void viewProfile(View v){

        Intent i = new Intent(PatientProfile.this,ViewPatientProfile.class);

        String s = getIntent().getExtras().getString("data");
        String s1 = getIntent().getExtras().getString("username");
        i.putExtra("username",s1);
        i.putExtra("data",s);

        startActivity(i);

    }

    public void Appointments(View v){

        String s1 = getIntent().getExtras().getString("id");
        Intent i= new Intent(PatientProfile.this,PatientAppointments.class);
        i.putExtra("id",s1);
        startActivity(i);

    }

    public void MyDoctors(View v){

        Intent i = new Intent(PatientProfile.this,MyDoctors.class);

        String id = getIntent().getExtras().getString("id");
        i.putExtra("id",id);
        i.putExtra("name",s1);
        startActivity(i);

    }

    public void MedicalRecords(View v){

        Intent i = new Intent(PatientProfile.this,MedicalRecords.class);
        String id = getIntent().getExtras().getString("id");
        i.putExtra("id",id);
        startActivity(i);

    }

    public void MedicalServices(View v){

        Intent i = new Intent(PatientProfile.this,MedicalServices.class);
        startActivity(i);

    }

    public void Events(View v){

        Intent i = new Intent(PatientProfile.this,Events.class);
        startActivity(i);

    }

}
