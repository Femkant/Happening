package com.example.happening;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyHappenings extends Fragment {


    public MyHappenings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_happenings, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My happenings");

        if (!MySharedPref.getInstance().getSharedPref().loadNightModeState()){
            FrameLayout frameLayout =(FrameLayout) view.findViewById(R.id.bg_my_happenings);
            frameLayout.setBackgroundResource(R.drawable.bg);
        }

        return view;
    }

}
