package com.example.s528116.smartinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Item_Detail extends AppCompatActivity {

    private ImageView imageIV;
    private TextView itemNameTV, priceTV, quntityNeededTV, requiredByTV, detailsTV, cancleRequestTV;
    private EditText supplyAmountET, messageET;
    private Button submitRequestBTN;
    private String userEmail, docId, itemId, supplyAmount, message, imageURL, supplyRequestMsgId, supplyReqDocId, itemName;
    private long unitsRequired, unitPrice, supplyAmt, totalValue;
    private FirebaseFirestore db;
    private DocumentReference itemRef, userRef;
    private CollectionReference supplyRequstRef;
    private StorageReference imageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__detail);

        imageIV = findViewById(R.id.imageIV);
        itemNameTV = findViewById(R.id.itemNameTV);
        priceTV = findViewById(R.id.priceTV);
        quntityNeededTV = findViewById(R.id.quntityNeededTV);
        requiredByTV = findViewById(R.id.requiredByTV);
        detailsTV = findViewById(R.id.detailsTV);
        supplyAmountET = findViewById(R.id.supplyAmountET);
        messageET = findViewById(R.id.messageET);
        cancleRequestTV = findViewById(R.id.cancleRequestTV);
        submitRequestBTN = findViewById(R.id.submitRequestBTN);

        db = FirebaseFirestore.getInstance();

//      Get data from the Intent
        Intent i = getIntent();
        userEmail = i.getStringExtra("userEmail");

        itemId = i.getStringExtra("itemId");
        unitPrice = i.getLongExtra("unitPrice",0);
        docId = i.getStringExtra("documentId");
        imageURL = i.getStringExtra("imageURL");
        //imageIV.setImageResource(i.getIntExtra("image",0));
        Picasso.get().load(imageURL).into(imageIV);
        itemNameTV.setText(i.getStringExtra("itemName"));
        priceTV.setText("Buying price :$" + i.getLongExtra("unitPrice",0));
        quntityNeededTV.setText("Quntity Needed : " + i.getLongExtra("qntyRequired",0));
        requiredByTV.setText("Required By :" + i.getStringExtra("requiredBy"));
//        Get an instance of the items
        itemRef = db.collection("items").document(docId);
        supplyRequstRef = db.collection("supplyRequests");
        itemRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                detailsTV.setText(documentSnapshot.get("itemDetails").toString());
                unitsRequired = documentSnapshot.getLong("unitRequired");
                itemName = documentSnapshot.getString("itemName");
            }
        });

//      if usr click "Submit Request", it will read the number of items and the message and save the data under user --> SupplyList
        submitRequestBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplyAmount = supplyAmountET.getText().toString();
                message = messageET.getText().toString();
                supplyAmt = Integer.parseInt(supplyAmount);
                totalValue = unitPrice * supplyAmt;
                if (supplyAmt > 0 && supplyAmt<= unitsRequired) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Item_Detail.this);
                    builder.setTitle("Conformation..")
                            .setMessage("Make a request to supply "+supplyAmount+" items.\n\nAdmin will response to this request within one business day")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Map<String, Object> supplyRequest = new HashMap<>();
                            supplyRequest.put("itemDocId", docId);
                            supplyRequest.put("itemId", itemId);
                            supplyRequest.put("imageURL", imageURL);
                            supplyRequest.put("message", message);
                            supplyRequest.put("supplyAmount", supplyAmt);
                            supplyRequest.put("paymentStatus", "notSet");
                            supplyRequest.put("status", "Pending");
                            supplyRequest.put("unitPrice", unitPrice);
                            supplyRequest.put("totalValue", totalValue);
                            supplyRequest.put("itemName", itemName);
                            supplyRequest.put("createdDate", new Timestamp(new Date()));
                            userRef = db.collection("users").document(userEmail);
                            userRef.collection("supplyList").document().set(supplyRequest);

                            userRef.collection("supplyList").orderBy("createdDate", Query.Direction.DESCENDING).limit(1).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            Map<String, Object> supplyMap = new HashMap<>();
                                            supplyMap.put("user", userEmail);
                                            supplyReqDocId = doc.getId();
                                            supplyMap.put("supplyReqDocId", supplyReqDocId);
                                            supplyMap.put("requestDate", new Timestamp(new Date()));
                                            supplyMap.put("itemId", doc.getString("itemId"));
                                            supplyMap.put("status", "pending");
                                            supplyMap.put("itemName", itemName);
                                            supplyRequstRef.document().set(supplyMap);

                                            db.collection("supplyRequests").orderBy("requestDate", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for (QueryDocumentSnapshot doc : task.getResult()){
                                                            supplyRequestMsgId = doc.getId();
                                                            db.collection("users").document(userEmail).collection("supplyList")
                                                                    .document(supplyReqDocId).update("supplyReqMsgId", supplyRequestMsgId);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });



//                            db.collection("users").document(userEmail).collection("supplyList")
//                                    .document(supplyReqDocId).update("supplyReqMsgId", supplyRequestMsgId);

                            long newQuntyRequired = unitsRequired - supplyAmt;
                            itemRef.update("unitRequired", newQuntyRequired);  // Adjust the number of required units.
//                          Close the Item_Detail activity
                            Intent j = new Intent(Item_Detail.this, ItemListRV.class);
                            j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            j.putExtra("userEmail", userEmail);
                            startActivity(j);
                            Item_Detail.this.finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(Item_Detail.this, "Not a valid amount of units", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancleRequestTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // Menu and menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                Item_Detail.this.finish();
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, Aboutus.class);
                aboutIntent.putExtra("userName", userEmail);
                startActivity(aboutIntent);
                break;
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
            case R.id.back:
                Intent j = new Intent(this, ItemListRV.class);
                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                j.putExtra("userEmail", userEmail);
                startActivity(j);
                Item_Detail.this.finish();
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
