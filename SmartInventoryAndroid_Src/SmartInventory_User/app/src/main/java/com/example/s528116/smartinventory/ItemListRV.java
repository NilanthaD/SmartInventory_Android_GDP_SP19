package com.example.s528116.smartinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class ItemListRV extends AppCompatActivity {
    private RecyclerView itemsRV;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager itemsLayoutManager;

    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    private CollectionReference itemCollection;


    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_rv);

        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("itemImages");
        itemCollection = db.collection("items");
        final Date today = new Date();

        Intent i = getIntent();
        userEmail = i.getStringExtra("userEmail");

        final ArrayList<ItemContainer> itemListArray = new ArrayList<>();
        itemCollection.orderBy("itemPostedDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i=0;
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        if(today.before(doc.getTimestamp("requiredBefore").toDate())) {
                            itemListArray.add(new ItemContainer(userEmail, doc.getId(), doc.getString("imageURL"), doc.getString("itemId"), doc.getString("itemName"),
                                    doc.getLong("untPrice"), doc.getLong("unitRequired"), doc.getTimestamp("requiredBefore").toDate()));
                            i++;
                        }
                    }

                    itemsRV = findViewById(R.id.itemsRV);
                    itemsRV.setHasFixedSize(true);
                    itemsLayoutManager = new LinearLayoutManager(ItemListRV.this);
                    itemsAdapter = new ItemsAdapter(itemListArray, ItemListRV.this);
                    itemsRV.setLayoutManager(itemsLayoutManager);
                    itemsRV.setAdapter(itemsAdapter);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return (true);

            case R.id.SupplyHistory:
                Intent supplyHistoryIntent = new Intent(this, SupplyHistoryRV.class);
                supplyHistoryIntent.putExtra("userEmail", userEmail);
                startActivity(supplyHistoryIntent);
                break;
            case R.id.Messages:
                Intent messagesIntent = new Intent(this, MessagesRV.class);
                messagesIntent.putExtra("userEmail", userEmail);
                startActivity(messagesIntent);
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, Aboutus.class);
                aboutIntent.putExtra("userName", userEmail);
                startActivity(aboutIntent);
                break;
            case R.id.ContactUs:
                Intent contactIntent = new Intent(this, ContactUs.class);
                contactIntent.putExtra("userName", userEmail);
                startActivity(contactIntent);
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
