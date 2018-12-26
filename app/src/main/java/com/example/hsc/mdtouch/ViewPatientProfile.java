package com.example.hsc.mdtouch;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ViewPatientProfile extends AppCompatActivity implements LocationListener {

    LocationManager manager;
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    AlertDialog.Builder dialog;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);

        Initialize();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assert viewPager != null;
        viewPager.setAdapter(adapter);

        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }catch(SecurityException ignored){

        }

    }

    private void Initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

    }

    public void CommonDialog(String name, final String array[], final int id){

        spin = new Spinner(this);
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle(name);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,array);

        spin.setPadding(40,10,30,0);
        spin.setAdapter(adp);
        dialog.setView(spin);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView text = (TextView) findViewById(id);
                assert text != null;
                text.setText(array[spin.getSelectedItemPosition()]);
            }
        });

        dialog.show();

    }

    public void SmokingHabits(View v){

        String smoke[] = {"I don't smoke","I used to, but I've quit","1 - 2/day","3 - 5/day","5 - 10/day","> 10/day"};

        CommonDialog("Smoking Habits", smoke, R.id.smoking_habits);
    }

    public void AlcoholConsumptions(View v){

        String alcohol[] = {"Non-drinker","Rare","Social","Regular","Heavy"};

        CommonDialog("Alcohol Consumption",alcohol,R.id.alcohol);

    }

    public void ActivityLevel(View v){

        String activity[] = {"Low","Normal","High","Very high"};

        CommonDialog("Activity Level",activity,R.id.activity);

    }

    public void FoodPreference (View v){

        String food[] = {"Vegetarian","Non-Vegetarian","Eggetarian","Vegan"};

        CommonDialog("Food Preference",food,R.id.food);

    }

    public void Occupation(View v){

        String profession[] = {"IT professional","Medical professional","Banking professional","Education","Student",
                "Home-maker","Other"};

        CommonDialog("Occupation",profession,R.id.profession);

    }

    public void Allergies(View v){

    }

    public void CurrentMedications(View v){

    }

    public void PastMedications(View v){

    }

    public void ChronicDisease(View v){

    }

    public void Injuries(View v){

    }

    public void Surgeries(View v){

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

    @Override
    public void onLocationChanged(Location location) {

        TextView l = (TextView) findViewById(R.id.location);

        Geocoder geo = new Geocoder(this,Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geo.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();

            Log.i("TAG",""+address+" "+city);

            assert l != null;
            l.setText(address+" "+city);

        } catch (IOException e) {
            Log.i("TAG",""+e.getMessage());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(this,"Please Enable GPS and Internet",Toast.LENGTH_SHORT).show();

    }
}







