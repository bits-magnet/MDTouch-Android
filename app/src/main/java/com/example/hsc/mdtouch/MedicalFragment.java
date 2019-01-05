package com.example.hsc.mdtouch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicalFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

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

        view =  inflater.inflate(R.layout.medical_tab, container,false);

        String username = getActivity().getIntent().getExtras().getString("username");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("patients").child(username);

        Listener(ref, "Allergies",R.id.allergies);
        Listener(ref, "Current Medications", R.id.current_medication);
        Listener(ref, "Past Medications", R.id.past_medication);
        Listener(ref, "Chronic Disease", R.id.disease);
        Listener(ref, "Surgeries", R.id.surgeries);
        Listener(ref, "Injuries", R.id.injuries);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}