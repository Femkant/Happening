package com.example.happening;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;


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

        ListView mListView = (ListView) view.findViewById(R.id.listView);

        final ArrayList<Happening> list = new ArrayList<>();
        list.add(new Happening("User name","Happening name","2014/01/02","18:45","Hässleholm","Greta ska fisa"));
        list.add(new Happening("TannaMakarn","Bacon","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an " +
                "Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an " +
                "Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an " +
                "Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an " +
                "Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an Baconregn på 57:an "));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));
        list.add(new Happening("Lora","Päronplock","2018/01/02","18:45","Ängelholm","Baconregn på 57:an"));

        final HappeningListAdapter adapter = new HappeningListAdapter(getContext(), R.layout.adapter_view_happening, list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Happening happening = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object", happening);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ShowHappening showHappening = new ShowHappening();
                showHappening.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_holder, showHappening, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
