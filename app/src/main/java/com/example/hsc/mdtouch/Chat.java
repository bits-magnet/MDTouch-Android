package com.example.hsc.mdtouch;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ProgressBar progressBar;
    private MessageAdapter messageAdapter;
    private EditText editText;

    private DatabaseReference messagesDatabaseReference,ref1,ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String receiver = getIntent().getExtras().getString("receiver");
        final String sender = getIntent().getExtras().getString("sender");

        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitleTextColor(Color.WHITE);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(receiver);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        messagesDatabaseReference = firebaseDatabase.getReference().child("messages");

        ref1 = messagesDatabaseReference.child(sender+"_"+receiver);
        ref2 = messagesDatabaseReference.child(receiver+"_"+sender);

        ListView listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.edittext);
        ImageView sendButton = (ImageView) findViewById(R.id.sendbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        List<Message> friendlyMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.my_message, friendlyMessages,1,receiver);
        assert listView != null;
        listView.setAdapter(messageAdapter);

        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        assert sendButton != null;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Message message = new Message(editText.getText().toString());

                if (editText.getText().length() != 0) {
                    ref1.push().setValue(message);
                    ref2.push().setValue(message);
                }

                editText.setText("");
            }
        });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                Message message = dataSnapshot.getValue(Message.class);
                messageAdapter.add(message);

                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref1.addChildEventListener(childEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    protected void onPause() {
        super.onPause();
    }

    protected  void onResume(){
        super.onResume();
    }


}
