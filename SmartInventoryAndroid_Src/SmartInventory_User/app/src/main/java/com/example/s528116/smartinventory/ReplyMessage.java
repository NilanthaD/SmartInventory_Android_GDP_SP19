package com.example.s528116.smartinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReplyMessage extends AppCompatActivity {

    private String msgDocId, user;
    private String from, message, status, title, to, userId;
    private Date composed_date;

    private EditText titleET, messageET;
    private TextView toTV, fromTV, closeTV;
    private Button sendBTN;

    private FirebaseFirestore db;
    private CollectionReference messageColRef;
    private DocumentReference messageDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_message);
        final Intent repInt = getIntent();
        user = repInt.getStringExtra("userEmail");
        msgDocId = repInt.getStringExtra("msgDocId");


        titleET = findViewById(R.id.titleET);
        toTV = findViewById(R.id.toTV);
        closeTV = findViewById(R.id.closeTV);
        messageET = findViewById(R.id.messageET);
        sendBTN = findViewById(R.id.sendBTN);
        fromTV = findViewById(R.id.fromTV);

        db = FirebaseFirestore.getInstance();
        messageDocRef = db.collection("messages").document(msgDocId);
        messageColRef=db.collection("messages");

        messageDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    String date = FormatDate.getDate(doc.getDate("composed_date"));
                    title ="Re: "+doc.getString("title");
                    titleET.setText("Title : Re: "+ doc.getString("title"));
                    fromTV.setText("From : "+ doc.getString("userId"));
                    toTV.setText("To : Admin");
                    messageET.setText("\n\n\nFrom : "+doc.getString("from")+"\nTo :"+doc.getString("to")+"\nDate :"+date+"\n"+doc.getString("message"));
                }
            }
        });

        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = messageET.getText().toString();

                Map<String, Object> replyMap = new HashMap<>();
                replyMap.put("composed_date", new Timestamp(new Date()));
                replyMap.put("from", user);
                replyMap.put("message", message);
                replyMap.put("title", title);
                replyMap.put("to", "Admin");
                replyMap.put("userId", user);
                messageColRef.document().set(replyMap);
                Intent messageIntent = new Intent(ReplyMessage.this, MessagesRV.class);
                messageIntent.putExtra("userEmail", user);
                messageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(messageIntent);
                ReplyMessage.this.finish();
            }
        });

        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(ReplyMessage.this, MessagesRV.class);
                messageIntent.putExtra("userEmail", user);
                messageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(messageIntent);
                ReplyMessage.this.finish();
            }
        });
    }
}
