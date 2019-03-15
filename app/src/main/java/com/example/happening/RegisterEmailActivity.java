package com.example.happening;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterEmailActivity extends AppCompatActivity {


    FirebaseAuth auth;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        auth = FirebaseAuth.getInstance();

        final EditText email = (EditText) findViewById(R.id.emailText2);
        final EditText pw = (EditText) findViewById(R.id.passwordText2);

        registerBtn = (Button) findViewById(R.id.registerBtn);


        registerBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                register(email, pw);

            }
        });
    }

    public void register(EditText email, EditText pw) {
        auth.createUserWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterEmailActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();

                            Intent myIntent = new Intent(RegisterEmailActivity.this, MainActivity.class);
                            startActivityForResult(myIntent, 0);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(RegisterEmailActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }
}

