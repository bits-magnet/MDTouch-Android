package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appointment_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CardView myselfCard = (CardView)findViewById(R.id.card_myself);
        CardView otherCard = (CardView) findViewById(R.id.card_other);

        final TextView myself = (TextView) findViewById(R.id.myself);
        final TextView other = (TextView) findViewById(R.id.other);

        final EditText patient = (EditText) findViewById(R.id.patient_name);

        final String s = getIntent().getExtras().getString("name");

        assert patient != null;
        patient.setText(s);

        assert myselfCard != null;
        myselfCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                assert myself != null;
                assert other != null;
                myself.setTextColor(Color.BLUE);
                other.setTextColor(Color.BLACK);
                assert patient != null;
                patient.setText(s);

                return false;
            }
        });

        assert otherCard != null;
        otherCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                assert myself != null;
                assert other != null;
                myself.setTextColor(Color.BLACK);
                other.setTextColor(Color.BLUE);
                assert patient != null;
                patient.setText("");

                return false;
            }
        });

        final Spinner state = (Spinner) findViewById(R.id.states);
        final Spinner city = (Spinner) findViewById(R.id.cities);
        ArrayList<String> states;
        states = LoadPlace("states","");

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,states);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assert state != null;
        state.setAdapter(adp);

        final ArrayList<String> finalStates = states;
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = finalStates.get(position);

                ArrayList<String> cities = LoadPlace("cities", s);
                adapt(city, cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void RequestDoctor(View v){

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

    public void adapt(Spinner city, ArrayList<String> cities){

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,cities);
        city.setAdapter(adp);

    }

    private ArrayList<String> LoadPlace(String place,String city) {

        String json = null;

        ArrayList<String> places = new ArrayList<String>();

        try{
            InputStream in = getAssets().open("data.json");

            byte[] buffer = new byte[in.available()];

            in.read(buffer);
            in.close();

            json = new  String(buffer,"UTF-8");

        } catch (IOException ignored) {}

        try {
            JSONObject o = new JSONObject(json);

            if(place.equals("cities")){
                JSONObject s = new JSONObject(o.getString("cities"));
                JSONArray y = s.getJSONArray(city);

                for(int i=0;i<y.length();i++)
                    places.add(y.getString(i));

            }else{

                JSONArray a = o.getJSONArray(place);
                for(int i=0;i<a.length();i++)
                    places.add(a.getString(i));

            }
        } catch (JSONException ignored) {}

        return places;
    }

}
