package com.example.hsc.mdtouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DoctorProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.doctor_toolbar);
        setSupportActionBar(toolbar);
    }

    public void Appointments(View v){

    }

    public void YourPatients(View v){

    }

    public void ChatDoctors(View v){

    }

    public void MedicalServices(View v){

        Intent i = new Intent(DoctorProfile.this,MedicalServices.class);
        startActivity(i);

    }

}
