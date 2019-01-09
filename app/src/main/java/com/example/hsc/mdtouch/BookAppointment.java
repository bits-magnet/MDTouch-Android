package com.example.hsc.mdtouch;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class BookAppointment extends AppCompatActivity {

    Boolean isPatient = true;
    String HId,DId,Pid;
    JSONObject json = new JSONObject();
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appointment_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Confirm Appointment");
        }

        CardView myselfCard = (CardView)findViewById(R.id.card_myself);
        CardView otherCard = (CardView) findViewById(R.id.card_other);

        final TextView myself = (TextView) findViewById(R.id.myself);
        final TextView other = (TextView) findViewById(R.id.other);

        final EditText patient = (EditText) findViewById(R.id.patient_name);

        final String s = getIntent().getExtras().getString("id");

        assert patient != null;
        patient.setText(s);
        patient.setEnabled(false);

        assert myselfCard != null;
        myselfCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                assert myself != null;
                assert other != null;
                myself.setTextColor(Color.BLUE);
                other.setTextColor(Color.BLACK);
                patient.setText(s);
                patient.setError(null);
                patient.setEnabled(false);
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
                patient.setText(" ");
                patient.setEnabled(true);

                return false;
            }
        });

        final TextView date = (TextView) findViewById(R.id.date);
        assert date != null;
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);
                int d = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog t = new DatePickerDialog(BookAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        date.setText(year + "-" +  monthOfYear + "-" + dayOfMonth);
                        date.setError(null);
                    }
                },y,m,d);
                t.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                t.setTitle("Select Date");
                t.show();
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
                Reset();

                String s = finalStates.get(position);

                ArrayList<String> cities = LoadPlace("cities", s);
                adapt(city, cities);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        assert city != null;
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Reset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView request = (TextView) findViewById(R.id.request);
        assert request != null;
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Validate()){

                    dialog = new ProgressDialog(BookAppointment.this);
                    dialog.setMessage("Requesting ...");
                    dialog.setCancelable(false);
                    dialog.show();

                    EditText mId = (EditText) findViewById(R.id.patient_name);
                    assert mId != null;
                    Pid = mId.getText().toString();

                    TextView mDate = (TextView) findViewById(R.id.date);
                    assert mDate != null;
                    String date = mDate.getText().toString();

                    EditText mProblem = (EditText) findViewById(R.id.problem);
                    assert mProblem != null;
                    String problem = mProblem.getText().toString();

                    try {
                        json.accumulate("patient",Pid);
                        json.accumulate("appointmentdate",date);
                        json.accumulate("message",problem);
                        json.accumulate("location",HId);
                        json.accumulate("doctor",DId);

                    } catch (JSONException ignored) {

                    }


                    try {
                        new PostAppointment().execute("https://mdtouch.herokuapp.com/MDTouch/api/appointment/").get();
                    } catch (InterruptedException | ExecutionException ignored) {

                    }

                    dialog.dismiss();

                    Toast.makeText(BookAppointment.this,"Appointment Added Successfully",Toast.LENGTH_LONG).show();

                    Intent i = new Intent();



                    setResult(RESULT_OK, i);
                    finish();

                }

            }
        });


    }

    public void Reset(){

        TextView h = (TextView) findViewById(R.id.hospital);
        TextView c = (TextView) findViewById(R.id.doctor);

        assert h != null;
        h.setText("Select Hospital >");
        h.setTextColor(Color.BLACK);

        assert c != null;
        c.setText("Select Doctor >");
        c.setTextColor(Color.BLACK);
    }

    public void GetHospital(View v){

        Intent i = new   Intent(BookAppointment.this,Hospitals.class);

        Spinner spin = (Spinner) findViewById(R.id.cities);
        assert spin != null;
        String s = spin.getSelectedItem().toString();

        i.putExtra("city", s);

        startActivityForResult(i, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        String s;

        if(requestCode == 1 && resultCode == RESULT_OK){

            s = data.getStringExtra("hospital");
            HId = data.getStringExtra("id");

            TextView h = (TextView) findViewById(R.id.hospital);
            assert h != null;
            h.setText(s);
            h.setTextColor(Color.BLUE);

        }

        if(requestCode == 2 && resultCode == RESULT_OK){

            s = data.getStringExtra("doctor");
            DId = data.getStringExtra("id");

            TextView d = (TextView) findViewById(R.id.doctor);
            assert d != null;

            d.setText(s);
            d.setTextColor(Color.BLUE);

        }

    }

    public void GetDoctor(View v){

        Intent i = new   Intent(BookAppointment.this,Doctors.class);
        i.putExtra("id",HId);
        startActivityForResult(i, 2);

    }

    public Boolean Validate(){

        TextView other = (TextView) findViewById(R.id.other);
        TextView hospital = (TextView) findViewById(R.id.hospital);
        TextView doctor = (TextView) findViewById(R.id.doctor);
        TextView date = (TextView) findViewById(R.id.date);

        EditText id = (EditText) findViewById(R.id.patient_name);
        EditText problem = (EditText) findViewById(R.id.problem);

        assert other != null;
        assert id != null;
        if(other.getCurrentTextColor() == Color.BLUE){

            if(id.getText().toString().equals(" ")){
                id.setError("Field is Empty!");
                return false;
            }else{
                try {
                    new CheckPatient().execute().get();
                } catch (InterruptedException | ExecutionException ignored) {

                }

                if(!isPatient){
                    Toast.makeText(this,"No Patient Found!",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }


        }

        assert date != null;
        if(date.getText().toString().equals("")){
            date.setError("Field is Empty!");
            return false;
        }

        assert hospital != null;
        if(hospital.getText().toString().equals("Select Hospital >")){
            Toast.makeText(this,"Please Select Hospital!",Toast.LENGTH_LONG).show();
            return false;
        }

        assert doctor != null;
        if(doctor.getText().toString().equals("Select Doctor >")){
            Toast.makeText(this,"Please Select Doctor!",Toast.LENGTH_LONG).show();
            return false;
        }

        assert problem != null;
        if(problem.getText().toString().length()<=20){
            problem.setError("Please Describe Your Problem More!");
            return false;
        }


        return true;
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

    public class PostAppointment extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try{
                return HttpPost(params[0]);
            }catch (IOException e) {
                Log.i("TAG",e.getMessage());
                return "ERROR";
            }

        }

        private String HttpPost(String mUrl) throws IOException {
            URL url = new URL(mUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            setPostRequest(conn);

            conn.connect();

            return conn.getResponseMessage();

        }

        private void setPostRequest(HttpURLConnection conn) throws IOException {

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.write(json.toString());
            writer.flush();
            writer.close();
            out.close();
        }

    }

    public class CheckPatient extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String data = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/patient/"+Pid);

            Log.i("TAG", "" + data);

            if(data == null)
                isPatient=false;

            return null;
        }

    }


}
