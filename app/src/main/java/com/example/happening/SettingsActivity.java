package com.example.happening;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsActivity extends AppCompatActivity {

    private Button logOutBtn;
    private Button changePwBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        changePwBtn = (Button)findViewById(R.id.changePwBtn);
        changePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(SettingsActivity.this, changePwActivity.class);
                startActivityForResult(myIntent, 0);


            }
        });

        logOutBtn = (Button)findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.signOut();
                LoginManager.getInstance().logOut();

                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivityForResult(myIntent, 0);

                Toast.makeText(SettingsActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
