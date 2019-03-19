package com.example.happening;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changePwActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().hide();

        Button changePwButton = (Button)findViewById(R.id.changePwBtn);
        changePwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView oldPwText = (TextView)findViewById(R.id.oldPwText);
                TextView newPwText = (TextView)findViewById(R.id.newPwText);
                final String newPw = newPwText.getText().toString();
                String oldPw = oldPwText.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPw);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            user.updatePassword(newPw).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(changePwActivity.this, "Password Changed successfully", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(changePwActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });



    }
}
