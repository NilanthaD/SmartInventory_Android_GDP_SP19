package com.example.s528116.smartinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {

    private EditText titleET, messageET;
    private TextView cancelBTN;
    private Button submitBTN;
    private String title, message,userEmail;

    private FirebaseFirestore db;

    private DocumentReference userRef;
    private CollectionReference messageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        titleET = findViewById(R.id.titleET);
        messageET = findViewById(R.id.messageET);
        submitBTN = findViewById(R.id.submitBTN);
        cancelBTN = findViewById(R.id.cancelBTN);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userName");

        Toast.makeText(this, "User Name : "+ userEmail, Toast.LENGTH_SHORT).show();



        db = FirebaseFirestore.getInstance();



        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             title = titleET.getText().toString();
             message = messageET.getText().toString();
             userRef = db.collection("users").document(userEmail);
             messageRef = db.collection("messages");
             if (title.equals("")||message.equals("")){
                 Toast.makeText(ContactUs.this, "Title or Message cannot be empty", Toast.LENGTH_LONG).show();
                 return;
             }

             else {
                 Map<String, Object> contactUs = new HashMap<>();
                 contactUs.put("title", title);
                 contactUs.put("message", message);
                 contactUs.put("status", "sent");
                 contactUs.put("composed_date", new Timestamp(new Date()));
                 contactUs.put("userId", userEmail);
                 contactUs.put("from", userEmail);
                 contactUs.put("to", "Admin");
                // userRef.collection("messages").document().set(contactUs);
                 messageRef.document().set(contactUs);

                 messageET.setText("");
                 titleET.setText("");




                 finish();

             }

            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
