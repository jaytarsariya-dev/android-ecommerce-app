package com.example.androidminorproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Named;

public class Forgot extends AppCompatActivity {
    EditText edt1;
    Button btn1;
    FirebaseAuth auth;
    TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        edt1=findViewById(R.id.email_forgot);
        btn1=findViewById(R.id.btnreset);
        auth=FirebaseAuth.getInstance();
        txt1=findViewById(R.id.logintext);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = edt1.getText().toString();
                if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    Toast.makeText(Forgot.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Forgot.this, "Check your email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Forgot.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Loginforuser.class);
                startActivity(i);
                finish();
            }
        });

    }
}