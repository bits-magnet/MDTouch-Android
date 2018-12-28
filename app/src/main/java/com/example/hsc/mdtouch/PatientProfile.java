package com.example.hsc.mdtouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class PatientProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);

        String s1 = getIntent().getExtras().getString("name");
        String s2 = getIntent().getExtras().getString("number");

        TextView name = (TextView) findViewById(R.id.name);
        TextView number = (TextView) findViewById(R.id.number);

        assert name != null;
        name.setText(s1);
        assert number != null;
        number.setText(s2);

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

        Intent i= new Intent(PatientProfile.this,PatientAppointments.class);
        startActivity(i);

    }

    public void MyDoctors(View v){

        Intent i = new Intent(PatientProfile.this,MyDoctors.class);
        startActivity(i);

    }

    public void MedicalRecords(View v){

        Intent i = new Intent(PatientProfile.this,MedicalRecords.class);
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
