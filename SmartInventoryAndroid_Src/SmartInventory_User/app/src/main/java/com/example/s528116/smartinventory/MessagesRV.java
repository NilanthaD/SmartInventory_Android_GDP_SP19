package com.example.s528116.smartinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MessagesRV extends AppCompatActivity {
    private RecyclerView messageRV;
    private RecyclerView.Adapter messageAdapter;
    private RecyclerView.LayoutManager messageLayoutManager;

    private String userEmail;

    private FirebaseFirestore db;
    private CollectionReference messageColRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_rv);

        db = FirebaseFirestore.getInstance();
        messageColRef = db.collection("messages");

        Intent i = getIntent();
        userEmail = i.getStringExtra("userEmail");

        final ArrayList<MessageContainer> messageArrayList = new ArrayList<>();
        messageColRef.orderBy("composed_date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        if(doc.getString("userId").equals(userEmail)) {
                            messageArrayList.add(new MessageContainer(doc.getString("title"), doc.getString("from"), doc.getString("to"), doc.getDate("composed_date"),
                                    doc.getId(), doc.getString("message")));
                        }
                    }
                    messageRV = findViewById(R.id.messagesRV);
                    messageRV.setHasFixedSize(true);
                    messageLayoutManager = new LinearLayoutManager(MessagesRV.this);
                    messageAdapter = new MessageAdapter(messageArrayList, MessagesRV.this);
                    messageRV.setLayoutManager(messageLayoutManager);
                    messageRV.setAdapter(messageAdapter);
                }
            }
        });
    }
}
