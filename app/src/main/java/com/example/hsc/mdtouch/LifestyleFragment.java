package com.example.hsc.mdtouch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LifestyleFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    public void Listener(DatabaseReference ref, String name , final int id){

        DatabaseReference r1 = ref.child(name);

        r1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView t = (TextView) view.findViewById(id);

                if (t != null) {
                    t.setText(dataSnapshot.getValue(String.class));

                    if(t.getText().equals(""))
                        t.setText("Add Details");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.lifestyle_tab, container,false);

        String username = getActivity().getIntent().getExtras().getString("username");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("patients").child(username);

        Listener(ref, "Smoking Habits",R.id.smoking_habits);
        Listener(ref, "Alcohol Consumption", R.id.alcohol);
        Listener(ref, "Activity Level", R.id.activity);
        Listener(ref, "Food Preference", R.id.food);
        Listener(ref, "Occupation", R.id.profession);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }





}