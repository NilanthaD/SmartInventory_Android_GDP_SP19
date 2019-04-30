package com.example.s528116.smartinventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText userNameET;
    EditText passwordET;
    Button loginBTN;
    TextView signupTV;
    TextView forgotPWTV;
    private  String userGrop;
    private CheckBox rememberMeCB;

    private ProgressDialog progress;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mdb;
    private DocumentReference userRef;

    private SharedPreferences rememberMe;


    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupTV = findViewById(R.id.signupTV);
        userNameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.passwordET);
        loginBTN = findViewById(R.id.loginBTN);
        forgotPWTV = findViewById(R.id.forgotPWTV);
        rememberMeCB = findViewById(R.id.rememberMeCB);
        progress = new ProgressDialog(this);

        rememberMe = MainActivity.this.getPreferences(MODE_PRIVATE);

//      Get the saved username and password from shared preferences

        userNameET.setText(rememberMe.getString("userName",""));
        passwordET.setText(rememberMe.getString("password", ""));


        mAuth = FirebaseAuth.getInstance();

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = userNameET.getText().toString();
                final String password = passwordET.getText().toString();

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(MainActivity.this, "Please enter the user name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progress.setMessage("Login..");
                progress.show();

                mAuth.signInWithEmailAndPassword(userName,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    progress.cancel();
                                    userNameET.setText("");
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        passwordET.setText("");
//                                      if rememberME is checked, Save the username and password on shared preferences.
                                        if(rememberMeCB.isChecked()){

                                            SharedPreferences.Editor  editor = rememberMe.edit();
                                            editor.putString("userName", userName);
                                            editor.putString("password", password);
                                            editor.commit();
                                        }

                                        if (user.isEmailVerified()) {
                                            Intent i = new Intent(MainActivity.this, ItemListRV.class);
                                            i.putExtra("userEmail", userName);
                                            startActivity(i);
                                    }
                                    else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("Please verify your email..").setCancelable(false)
                                                    .setPositiveButton("Send verification email", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            user.sendEmailVerification();
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.setTitle("Email Verification");
                                            alert.show();
                                        }
                                }
                                else {
                                    progress.cancel();
                                    passwordET.setText("");
                                    Toast.makeText(MainActivity.this, "Login failed, Please check your email and password", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });
            }
        });

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Signup.class);
                startActivity(i);
            }
        });
        forgotPWTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameET.getText().toString();
                Intent i = new Intent(MainActivity.this, PasswordRecover.class);
                i.putExtra("userName", userName);
                startActivity(i);
            }
        });
    }
}
