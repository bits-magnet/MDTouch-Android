package com.example.hsc.mdtouch;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewPatientProfile extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    AlertDialog.Builder dialog;
    Spinner spin;

    String username;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);

        Initialize();

        username = getIntent().getExtras().getString("username");

        ref = database.getReference().child("patients").child(username);

        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        assert viewPager != null;
        viewPager.setAdapter(adapter);

        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

    }


    private void Initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

    }

    public void CommonDialog(final String name, final String array[], final int id){

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

                ref = database.getReference().child("patients").child(username) ;
                ref.child(name).setValue(array[spin.getSelectedItemPosition()]);

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

        CommonDialog("Occupation", profession, R.id.profession);

    }

    public void Allergies(View v){

        String allergies[] = {"Lactose","Soy","Seafood","Nuts","Eggs","Fish","Mushroom","Gluten","Penicillin","Sulpha Drugs",
                                "Local Anthesia","Aspirin","Insulin","X-Ray Dye","Pollen","Mold","Pets","Other"};

        CommonDialog2("Allergies", allergies, R.id.allergies);

    }

    public void CurrentMedications(View v){

        String cMedication[] = {"Ulgel a susp 200ml","Calapure a lotion 100ml","Nilac a gel 20gm","Trump a syrup 100ml",
                "Cligel a gel 15gm","Calak a lotion 50ml","Cosome a syrup 100ml","Bricrax a expt 100ml","Femicinol a gel 10gm","Other"};

        CommonDialog2("Current Medications",cMedication,R.id.current_medication);

    }

    public void PastMedications(View v){

        String pMedication[] = {"Ulgel a susp 200ml","Calapure a lotion 100ml","Nilac a gel 20gm","Trump a syrup 100ml",
                "Cligel a gel 15gm","Calak a lotion 50ml","Cosome a syrup 100ml","Bricrax a expt 100ml","Femicinol a gel 10gm","Other"};

        CommonDialog2("Past Medications",pMedication,R.id.past_medication);

    }

    public void ChronicDisease(View v){

        String disease[] = {"Diabetes","Hypertension","PCOS","Hypothyroidism","COPD","Asthma","Heart Disease","Arthritis",
                                "Fertility Issues","Mental Issues","Mental Depression","Other"};

        CommonDialog2("Chronic Disease",disease,R.id.disease);

    }

    public void Injuries(View v){

        String injuries[] = {"Burns","Spinal Cord Injury","Skull Fracture","Spinal Fracture","Rib Fracture","Jaw Fracture","Concussion",
                                        "Amputation","Traumatic Brain Injury","Facial Trauma","Accoustic Trauma","Other"};

        CommonDialog2("Injuries",injuries,R.id.injuries);
    }

    public void Surgeries(View v){

        String surgeries[] = {"Heart","Liver","Kidney","Lungs","Brain","Facial","Cosmetic","Other"};

        CommonDialog2("Surgeries",surgeries,R.id.surgeries);
    }

    public void CommonDialog2(final String name, final String values[],final int id){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(name);

        final boolean[] selectedItems = new boolean[values.length];

        dialog.setMultiChoiceItems(values, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedItems[which] = isChecked;
            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            String s = "";

            @Override
            public void onClick(DialogInterface dialog, int which) {

                int c = 0;

                for (boolean selectedItem : selectedItems)
                    if (selectedItem)
                        c++;

                for (int i = 0; i < selectedItems.length; i++) {
                    if (selectedItems[i] && i != (c-1))
                        s = s + values[i] + ", ";
                    else if (selectedItems[i] && (i == c-1))
                        s = s + values[i] + ".";
                }

                ref.child(name).setValue(s);

                TextView text = (TextView) findViewById(id);
                assert text != null;
                if(!s.equals(""))
                    text.setText(s);
                else
                    text.setText("Add Details");
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
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



}







