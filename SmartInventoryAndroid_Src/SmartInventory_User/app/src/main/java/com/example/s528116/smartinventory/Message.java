package com.example.s528116.smartinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Message extends AppCompatActivity {

    private TextView titleTV, fromTV,toTV, dateTV, messageTV, closeTV;
    private Button replyBTN;

    private FirebaseFirestore db;
    private CollectionReference messageColRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        titleTV = findViewById(R.id.titleTV);
        fromTV = findViewById(R.id.fromTV);
        dateTV = findViewById(R.id.dateTV);
        messageTV = findViewById(R.id.messageTV);
        replyBTN = findViewById(R.id.replyBTN);
        toTV = findViewById(R.id.toTV);
        closeTV = findViewById(R.id.closeTV);

        final Intent messageIn = getIntent();
        titleTV.setText("Title : "+messageIn.getStringExtra("title"));
        fromTV.setText("From : "+messageIn.getStringExtra("from"));
        toTV.setText("To : "+messageIn.getStringExtra("to"));
        dateTV.setText("Date : "+messageIn.getStringExtra("composedDate"));
        messageTV.setText(messageIn.getStringExtra("message"));
        
       replyBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


                Intent replyInt = new Intent(Message.this, ReplyMessage.class);
                replyInt.putExtra("userEmail", messageIn.getStringExtra("userEmail"));
                replyInt.putExtra("msgDocId", messageIn.getStringExtra("msgDocId"));
                startActivity(replyInt);

           }
       });

       closeTV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

    }
}
