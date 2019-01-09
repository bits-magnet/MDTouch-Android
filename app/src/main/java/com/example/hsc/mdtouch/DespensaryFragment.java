package com.example.hsc.mdtouch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DespensaryFragment extends Fragment {

    List<MedicalServicesDetails> despensary = new ArrayList<>();
    List<String> despensaryName = new ArrayList<>();

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

        View view = inflater.inflate(R.layout.despensary_tab, container,false);

        ListView l1 = (ListView) view.findViewById(R.id.despensary_services_list);

        ArrayAdapter<String> a1 = new ArrayAdapter<>(getContext(),R.layout.list_item,despensaryName);

        TextView e1 = (TextView) view.findViewById(R.id.empty_despensary);

        if (l1 != null) {
            l1.setAdapter(a1);
            l1.setEmptyView(e1);
        }

        assert l1 != null;
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View content = getLayoutInflater().inflate(R.layout.medical_services,null);

                TextView title = (TextView) content.findViewById(R.id.title);
                TextView name = (TextView) content.findViewById(R.id.name);
                TextView address= (TextView) content.findViewById(R.id.address);
                TextView city = (TextView) content.findViewById(R.id.city);
                TextView contact = (TextView) content.findViewById(R.id.contact);

                title.setText("Despensary");
                name.setText(despensary.get(position).getName());
                address.setText(despensary.get(position).getAddress());
                city.setText(despensary.get(position).getCity());
                contact.setText(despensary.get(position).getContact());

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.setView(content);

                AlertDialog d = dialog.create();
                d.show();

            }
        });

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

            s1 = helper.get("https://mdtouch.herokuapp.com/MDTouch/api/dispensaries/");

            s1 = "{ \"data\" :  " + s1 + " }";

            try{

                JSONObject j1 = new JSONObject(s1);

                JSONArray a1 = j1.getJSONArray("data");

                for(int i=0;i<a1.length();i++){

                    JSONObject a = a1.getJSONObject(i);

                    String b = a.getString("name");
                    String c = a.getString("address");
                    String d = a.getString("city");
                    String e = a.getString("state");
                    String f = a.getString("contact");

                    despensaryName.add(b);

                    MedicalServicesDetails m = new MedicalServicesDetails(b,c,d+", "+e,f);
                    despensary.add(m);
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