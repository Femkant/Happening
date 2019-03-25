package com.example.happening;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happening.DbStuff.SocketConnect;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;
    private boolean ipBtnVisible = false;
    public static boolean refreshFlag;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        auth = FirebaseAuth.getInstance();

        if (!MySharedPref.getInstance().getSharedPref().loadNightModeState()){
            ConstraintLayout constraintLayout =(ConstraintLayout)view.findViewById(R.id.bg_settings);
            constraintLayout.setBackgroundResource(R.drawable.bg);
        }

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

        Switch mySwitch=(Switch)view.findViewById(R.id.mySwitch);
        final SharedPref sharedPref = MySharedPref.getInstance().getSharedPref();

        if (sharedPref.loadNightModeState()){
            mySwitch.setChecked(true);
        }else {
            mySwitch.setChecked(false);
        }


        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPref.setNightModeState(true);
                    restartApp();
                } else  {
                    sharedPref.setNightModeState(false);
                    restartApp();

                }
            }
        });
        final Button toggleIp = (Button) view.findViewById(R.id.toggleIP);
        final Button saveIP = (Button) view.findViewById(R.id.saveIP);
        final TextView ipAdress = (TextView) view.findViewById(R.id.ipEditText);

        ipAdress.setGravity(Gravity.CENTER);
        ipAdress.setVisibility(View.INVISIBLE);

        saveIP.setVisibility(View.INVISIBLE);

        toggleIp.setText("Show IP");
        toggleIp.getBackground().setAlpha(0);

        toggleIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipBtnVisible){
                    toggleIp.setText("Show IP");
                    toggleIp.getBackground().setAlpha(0);
                    saveIP.setVisibility(View.INVISIBLE);
                    ipAdress.setVisibility(View.INVISIBLE);
                    ipBtnVisible = false;
                }
                else {
                    toggleIp.setText("Hide IP");
                    toggleIp.getBackground().setAlpha(255);
                    saveIP.setVisibility(View.VISIBLE);
                    ipAdress.setVisibility(View.VISIBLE);
                    ipAdress.setText(SocketConnect.HOST);
                    if (MySharedPref.getInstance().getSharedPref().loadNightModeState()){
                        ipAdress.setHintTextColor(Color.parseColor("#ffffff"));
                        toggleIp.setTextColor(Color.parseColor("#ffffff"));
                    }else {
                        ipAdress.setHintTextColor(Color.parseColor("#000000"));
                        toggleIp.setTextColor(Color.parseColor("#000000"));
                    }
                    ipBtnVisible = true;
                }
            }
        });

        saveIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;
                if(new ReadWrite().writeToFile(ipAdress.getText().toString(), getContext())) {
                    SocketConnect.HOST = ipAdress.getText().toString();
                    message = "Ip is saved";
                }
                else {
                    message = "Error IP is not saved";
                }

                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });



        return view;


        }

    private void restartApp() {
        refreshFlag = true;
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();

        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);

}
    }
