package com.example.happening;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        auth = FirebaseAuth.getInstance();

        final Button changePwBtn = (Button) view.findViewById(R.id.changePwBtn);

        changePwBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_holder, changePasswordFragment, null);
                    transaction.addToBackStack(null);
                    transaction.commit();


                }

        });

        final Button logOutBtn = (Button) view.findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               auth.signOut();
                LoginManager.getInstance().logOut();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
                    return view;


        }
    }