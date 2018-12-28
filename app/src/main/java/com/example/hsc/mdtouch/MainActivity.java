package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private static String URL = "https://mdtouch.herokuapp.com/MDTouch/api/";


    public static final String PREF = "first_time";
    boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this,PREF,"true"));
        Intent intro = new Intent(MainActivity.this,PagerActivity.class);
        intro.putExtra(PREF,isFirstTime);

        if(isFirstTime){
            startActivity(intro);
        }

        setContentView(R.layout.activity_main);

        final TextView patientText = (TextView) findViewById(R.id.patienttext);
        assert patientText != null;
        patientText.setTextColor(Color.GREEN);

        Button login = (Button) findViewById(R.id.doctor_login);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    EditText username = (EditText) findViewById(R.id.username);
                    EditText password = (EditText) findViewById(R.id.password);

                    if(patientText.getCurrentTextColor() == Color.GREEN){

                        assert username != null;
                        assert password != null;
                        new CheckUser(username.getText().toString(),password.getText().toString(),"p").execute();

                    } else {

                        assert username != null;
                        assert password != null;
                        new CheckUser(username.getText().toString(),password.getText().toString(),"d").execute();
                    }

                }
            }
        });

        final LinearLayout patientLayout = (LinearLayout) findViewById(R.id.patientlayout);
        final LinearLayout doctorLayout = (LinearLayout) findViewById(R.id.doctorlayout);

        final TextView doctorText = (TextView)findViewById(R.id.doctortext);

        assert patientLayout != null;
        patientLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                patientLayout.setBackgroundColor(Color.BLACK);
                patientText.setTextColor(Color.GREEN);

                assert doctorLayout != null;
                doctorLayout.setBackgroundColor(Color.parseColor("#2b2a2a"));
                assert doctorText != null;
                doctorText.setTextColor(Color.WHITE);

                return false;
            }
        });

        assert doctorLayout != null;
        doctorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                doctorLayout.setBackgroundColor(Color.BLACK);
                assert doctorText != null;
                doctorText.setTextColor(Color.GREEN);

                patientLayout.setBackgroundColor(Color.parseColor("#2b2a2a"));
                patientText.setTextColor(Color.WHITE);

                return false;
            }
        });

    }

    public boolean validate(){

        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);

        assert username != null;
        if(username.getText().length()<=0){
            username.setError("Field is Empty!");
            return false;
        }

        assert password != null;
        if(password.getText().length()<=0){
            password.setError("Field is Empty!");
            return false;
        }

        return true;
    }

    public void ForgetPassword(View view) {

        Intent i = new Intent("android.intent.action.VIEW", Uri.parse("https://mdtouch.herokuapp.com/password/"));
        startActivity(i);

    }

    private class CheckUser extends AsyncTask<Void,Void,Boolean>{
        String username,password;
        String fn,ln,no,str,s1,s2;
        String url = URL;

        public CheckUser(String a, String b,String c){
            username = a;
            password = b;

            if(c.equals("p"))
                url+="patient/?username="+username+"&password="+password;
            else
                url+="login/";
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Validating ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            str = helper.get(url);

            if(str.length()!=2) {

                try {
                    str = str.substring(1, str.length() - 1);

                    JSONObject o = new JSONObject(str);

                    s1 = o.getString("username");
                    s2 = o.getString("password");

                    fn = o.getString("firstName");
                    ln = o.getString("lastName");
                    no = o.getString("number");

                } catch (JSONException ignored) {

                }

                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(dialog.isShowing())
                dialog.dismiss();

            if(aBoolean) {

                Intent i;

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref;

                if(url.contains("patient")){

                    ref = database.getReference().child("patients").child(s1);
                    ref.child("password").setValue(s2);

                    i = new Intent(MainActivity.this,PatientProfile.class);
                    i.putExtra("name",fn+" "+ln);
                    i.putExtra("number",no);
                    i.putExtra("username",username);
                    i.putExtra("data",str);
                }else{

                    ref = database.getReference().child("doctors");
                    ref.child(s1).setValue(s2);

                    i = new Intent(MainActivity.this,DoctorProfile.class);
                }
                startActivity(i);

                Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_SHORT).show();
                finish();

            }else{

                Snackbar.make(findViewById(R.id.root), "Invalid Details, Please try again!", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }

        }
    }


}
