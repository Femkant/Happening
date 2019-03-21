package com.example.happening;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    private FirebaseAuth auth;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        final Button changeButton = (Button)view.findViewById(R.id.changeButton);

        auth = FirebaseAuth.getInstance();

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView oldPwText = (TextView) view.findViewById(R.id.oldPwText);
                TextView newPwText = (TextView) view.findViewById(R.id.newPwText);

                if (oldPwText.getText().length() >= 1 && newPwText.getText().length() >= 1) {

                    final String newPw = newPwText.getText().toString();
                    String oldPw = oldPwText.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPw);


                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPw).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Password Changed successfully", Toast.LENGTH_LONG).show();
                                            //finish here
                                        } else {
                                            Log.e("ERROR", task.getException().toString());
                                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "Fields can not be empty!", Toast.LENGTH_LONG).show();
                }
                }
        });

                return view;
            }
        }
