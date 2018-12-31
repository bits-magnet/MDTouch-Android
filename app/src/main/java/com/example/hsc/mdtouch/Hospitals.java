package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Hospitals extends AppCompatActivity {

    ArrayList<String> hospitals = new ArrayList<>();
    ArrayList<String> hospitalsId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            new GetHospitals().execute().get();
        } catch (InterruptedException | ExecutionException ignored) {

        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.hospitals_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,hospitals);

        ListView listView = (ListView) findViewById(R.id.hospitals_list);
        assert listView != null;
        listView.setEmptyView(findViewById(R.id.empty1));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent();
                i.putExtra("hospital",hospitals.get(position));
                i.putExtra("id",hospitalsId.get(position));
                setResult(RESULT_OK, i);
                finish();

            }
        });


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

    private class GetHospitals extends AsyncTask<Void,Void,Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new ProgressDialog(Hospitals.this);
            dialog.setMessage("Getting Hospitals ...");
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String s = getIntent().getExtras().getString("city");
            String p = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/hospital/?city=" + s);

            p = "{ \"data\" :  " + p + " }";


            try {
                JSONObject a = new JSONObject(p);
                JSONArray b = a.getJSONArray("data");

                for(int i=0;i<b.length();i++){

                    JSONObject c = b.getJSONObject(i);
                    String d = c.getString("name");
                    String id = c.getString("id");

                    hospitals.add(d);
                    hospitalsId.add(id);

                }


            } catch (JSONException e) {
                Log.i("TAG",e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(dialog.isShowing())
                dialog.dismiss();

        }
    }
}
