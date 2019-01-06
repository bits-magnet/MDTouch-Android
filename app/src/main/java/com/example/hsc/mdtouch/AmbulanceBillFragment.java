package com.example.hsc.mdtouch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AmbulanceBillFragment extends Fragment {

    List<String> bills = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            new Load().execute().get();
        } catch (InterruptedException | ExecutionException ignored) {

        }

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ambulance_bill_tab, container,false);

        ListView l1 = (ListView) view.findViewById(R.id.ambulance_bill_list);

        ArrayAdapter<String> a1 = new ArrayAdapter<>(getContext(),R.layout.list_item,bills);

        TextView e1 = (TextView) view.findViewById(R.id.empty_ambulance_bill);

        if (l1 != null) {
            l1.setAdapter(a1);
            l1.setEmptyView(e1);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public class Load extends AsyncTask<Void,Void,Void> {

        ProgressDialog dialog;
        String s1;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHelper helper = new HttpHelper();

            String id = getActivity().getIntent().getExtras().getString("id");

            s1 = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/ambulancebilling/?patientid"+id);

            s1 = "{ \"data\" :  " + s1 + " }";

            try{

                JSONObject j1 = new JSONObject(s1);

                JSONArray a1 = j1.getJSONArray("data");

                for(int i=0;i<a1.length();i++){

                    JSONObject b = a1.getJSONObject(i);
                    bills.add(b.getString("destination"));

                }

            } catch (JSONException ignored) {}

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
        }
    }

}