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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SupplyHistoryRV extends AppCompatActivity {

    private RecyclerView supplyHistoryRV;
    private RecyclerView.Adapter supplyHistoryAdapter;
    private RecyclerView.LayoutManager supplyHistryLayoutManager;

   // private Intent supplyHistoryIntent = new Intent();
    private String userEmail;
    private FirebaseFirestore db;
    private DocumentReference supplyItemDocRef;
    CollectionReference supplyItemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_history_rv);
        db = FirebaseFirestore.getInstance();
        Intent supplyHistoryIntent = getIntent();
        userEmail = supplyHistoryIntent.getStringExtra("userEmail");
        supplyItemsCollection = db.collection("users").document(userEmail).collection("supplyList");

//      Read the user supplyList from the database and populate the view
        final ArrayList<SupplyHistory> supplyListAL = new ArrayList<>();
        supplyItemsCollection.orderBy("createdDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc: task.getResult()){
                        supplyListAL.add(new SupplyHistory(userEmail,doc.getString("imageURL"),doc.getString("status"), doc.getString("itemId"), doc.getLong("unitPrice"),
                                doc.getLong("supplyAmount"), doc.getDate("createdDate"),doc.getString("message"), doc.getLong("totalValue"),
                                doc.getString("paymentStatus"), doc.getString("itemDocId"), doc.getId()));
                    }
                    supplyHistoryRV = findViewById(R.id.supplyHistoryRV);
                    supplyHistoryRV.setHasFixedSize(true);
                    supplyHistryLayoutManager = new LinearLayoutManager(SupplyHistoryRV.this);
                    supplyHistoryAdapter = new SupplyHistoryAdapter(supplyListAL, SupplyHistoryRV.this);
                    supplyHistoryRV.setLayoutManager(supplyHistryLayoutManager);
                    supplyHistoryRV.setAdapter(supplyHistoryAdapter);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                SupplyHistoryRV.this.finish();
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, Aboutus.class);
                aboutIntent.putExtra("userName", userEmail);
                startActivity(aboutIntent);
                break;
            case R.id.Messages:
                Intent messagesIntent = new Intent(this, MessagesRV.class);
                messagesIntent.putExtra("userEmail", userEmail);
                startActivity(messagesIntent);
                break;
            case R.id.SupplyHistory:
                Intent supplyHistoryIntent = new Intent(this, SupplyHistoryRV.class);
                supplyHistoryIntent.putExtra("userEmail", userEmail);
                startActivity(supplyHistoryIntent);
                break;
            case R.id.back:
                Intent j = new Intent(this, ItemListRV.class);
                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                j.putExtra("userEmail", userEmail);
                startActivity(j);
                SupplyHistoryRV.this.finish();
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
