package com.example.happening;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.happening.DbStuff.SocketConnect;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button facebookBtn;
    Button emailBtn;
    EditText emailLogIn;
    EditText passwordLogIn;
    CallbackManager callbackManager;
    ToolBar tb;
    PopupWindow popUp;
    LinearLayout layout;
    LinearLayout.LayoutParams params;
    LinearLayout mainLayout;
    boolean click = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        tb = new ToolBar();
        callbackManager = CallbackManager.Factory.create();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        emailBtn = (Button)findViewById(R.id.emailBtn);

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            signInEmail();

            }
        });

        TextView registerTextBtn = (TextView)findViewById(R.id.registerTextBtn);
        registerTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this, RegisterEmailActivity.class);
                startActivityForResult(myIntent, 0);

            }
        });



        facebookBtn = (Button) findViewById(R.id.facebookBtn);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookBtn.setEnabled(false);
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        handleFacebookToken(loginResult.getAccessToken());


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "User cancelled it", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //Read saved ipaddress
        SocketConnect.HOST = new ReadWriteIP().readFromFile(this);
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null){
            updateUI();
        }
    }

    public void signInEmail(){

        emailLogIn = (EditText)findViewById(R.id.emailLogIn);
        passwordLogIn = (EditText)findViewById(R.id.passwordLogIn);


        (auth.signInWithEmailAndPassword(emailLogIn.getText().toString(), passwordLogIn.getText().toString()))
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(MainActivity.this, ToolBar.class);
                    startActivityForResult(myIntent, 0);
                }
                else {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void handleFacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    facebookBtn.setEnabled(true);
                    updateUI();

                } else {
                    facebookBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Could not register to firebase", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI() {
        Toast.makeText(MainActivity.this, "You're logged in ", Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(MainActivity.this, ToolBar.class);
        startActivityForResult(myIntent, 0);

    }
}
