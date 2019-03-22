package com.example.happening;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainHappenings extends Fragment {


    public MainHappenings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main_happenings, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.listView);

        ArrayList<Happening> list = new ArrayList<>();
        list.add(new Happening("User name","Happening name","2014/01/02","18:45","Hässleholm","Greta ska fisa"));
        list.add(new Happening("User name","Happening name","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        HappeningListAdapter adapter = new HappeningListAdapter(getContext(), R.layout.adapter_view_happening, list);
        mListView.setAdapter(adapter);


        return view;
    }

}