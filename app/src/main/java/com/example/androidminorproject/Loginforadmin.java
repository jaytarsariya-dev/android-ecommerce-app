package com.example.androidminorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Loginforadmin extends AppCompatActivity {

    EditText edt1,edt2;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginforadmin);

        edt1=findViewById(R.id.email_l);
        edt2=findViewById(R.id.password_l);
        btn1=findViewById(R.id.login_a);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str1=edt1.getText().toString().toLowerCase();
                String str2=edt2.getText().toString();

                if (str1.isEmpty() || str2.isEmpty()) {
                    if (str1.isEmpty())
                        edt1.setError("Please fill the detail");
                    else if (str2.isEmpty())
                        edt2.setError("Please fill the detail");
                }
                else {
                    // Proper admin authorization should be configured separately.
                    // Intent i=new Intent(getApplicationContext(),Adminpanel.class);
                    // startActivity(i);
                    // finish();
                    Toast.makeText(getApplicationContext(), "Admin authorization requires backend configuration", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}