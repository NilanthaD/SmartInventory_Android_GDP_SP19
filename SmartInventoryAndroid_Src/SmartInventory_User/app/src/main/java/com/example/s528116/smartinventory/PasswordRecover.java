package com.example.s528116.smartinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecover extends AppCompatActivity {
    private EditText userET;
    private Button sendReqBTN;
    private TextView cancleTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);

        final Intent i = getIntent();
        userET = findViewById(R.id.userET);
        userET.setText(i.getStringExtra("userName").toString());
        mAuth = FirebaseAuth.getInstance();
        sendReqBTN = findViewById(R.id.sendReqBTN);
        cancleTV = findViewById(R.id.cancleTV);

        sendReqBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userET.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(PasswordRecover.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordRecover.this, "Reset password email sent.", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordRecover.this);
                                builder.setMessage("Email sent to your email. Please follow the directions to reset the password.").setCancelable(false)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
//                                startActivity(new Intent(PasswordRecover.this, MainActivity.class));
                                finish();
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(PasswordRecover.this, "Error Occured : "+message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        cancleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
