package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class PersonalFragment extends Fragment {

    String s,ec;
    TextView emergency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_tab, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        s = getActivity().getIntent().getExtras().getString("data");

        try {
            JSONObject obj = new JSONObject(s);

            String fn = obj.getString("firstName");
            String ln = obj.getString("lastName");
            String no = obj.getString("number");
            String em = obj.getString("email");
            String pv = obj.getString("provider");
            String iId = obj.getString("insuranceid");
            String ht = obj.getString("height");
            String wt = obj.getString("weight");
            String gn = obj.getString("gender");
            String dob = obj.getString("dateofbirth");
            String bg = obj.getString("bloodgroup");
            String ms = obj.getString("maritalstatus");
            ec = obj.getString("contact");
            String an = obj.getString("aadharnumber");

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView number = (TextView) view.findViewById(R.id.number);
            TextView email = (TextView) view.findViewById(R.id.email);
            TextView provider = (TextView) view.findViewById(R.id.provider);
            TextView insuranceId = (TextView) view.findViewById(R.id.insurance);
            TextView height = (TextView) view.findViewById(R.id.height);
            TextView weight = (TextView) view.findViewById(R.id.weight);
            TextView gender = (TextView) view.findViewById(R.id.gender);
            TextView dateob = (TextView) view.findViewById(R.id.dob);
            TextView blood = (TextView) view.findViewById(R.id.blood);
            TextView status = (TextView) view.findViewById(R.id.status);
            emergency = (TextView) view.findViewById(R.id.emergency);
            TextView aadhar = (TextView) view.findViewById(R.id.aadhar);

            name.setText(fn+" "+ln);
            number.setText(no);
            email.setText(em);
            provider.setText(pv);
            insuranceId.setText(iId);
            height.setText(ht);
            weight.setText(wt+" Kg");
            gender.setText(gn);
            dateob.setText(dob.substring(0,10));
            blood.setText(bg);
            status.setText(ms);
            aadhar.setText(an);

            new Load().execute().get();

        } catch (JSONException | ExecutionException | InterruptedException ignored) {

        }

        super.onViewCreated(view, savedInstanceState);

    }

    public class Load extends AsyncTask<Void,Void,Void>{

        String e1;

        @Override
        protected Void doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            s = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/emergencycontact/"+ec);

            Log.i("TAG",""+s);
            Log.i("TAG",""+ec);

            try{
                JSONObject json = new JSONObject(s);
                e1 = json.getString("number");
            } catch (JSONException ignored) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            emergency.setText(e1);
        }
    }

}