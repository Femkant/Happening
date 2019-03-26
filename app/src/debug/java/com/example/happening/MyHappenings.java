package com.example.happening;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.happening.DbStuff.Data;
import com.example.happening.DbStuff.DbActions;
import com.example.happening.DbStuff.GetHappeningsRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyHappenings extends Fragment {
    private long delayDate1 = 0, delayDate2 = 0;
    private AtomicBoolean getRequestSent = new AtomicBoolean(false);

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
        final EditText selectDate1EditText = (EditText) view.findViewById(R.id.mySelectDate1EditText);
        final EditText selectDate2EditText = (EditText) view.findViewById(R.id.mySelectDate2EditText);

        final ArrayList<Happening> list = Data.getInstance().getMyHappeningsList();

        //Set correct dates
        if(list.size()>0){
            selectDate1EditText.setText(list.get(0).getDate());
            selectDate2EditText.setText(list.get(list.size()-1).getDate());
        }else{
            selectDate1EditText.setText(CommonTools.getToDay());
            selectDate2EditText.setText(CommonTools.getToDay());
        }


        selectDate1EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis()>delayDate1+delayDate1+Data.getInstance().BUTTON_DELAY_MS) {
                    delayDate1 = System.currentTimeMillis();
                    CommonTools.pickDate(getActivity(), selectDate1EditText, true);
                }
            }
        });

        selectDate2EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis()>delayDate2+delayDate1+Data.getInstance().BUTTON_DELAY_MS) {
                    delayDate2 = System.currentTimeMillis();
                    CommonTools.pickDate(getActivity(), selectDate2EditText, true);
                }
            }
        });

        ListView mListView = (ListView) view.findViewById(R.id.listView);

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

        Button getSelected = (Button) view.findViewById(R.id.myGetSelectedBtn);

        getSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if request already is in progress
                if(!getRequestSent.get()) {
                    Date date1, date2;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date1 = format.parse(selectDate1EditText.getText().toString());
                        date2 = format.parse(selectDate2EditText.getText().toString());

                        //Date one must be equal or smaller than two
                        if (date1.compareTo(date2) == 0 || date1.compareTo(date2) < 0) {

                            Data.getInstance().acquireWrite("Mainhappening");
                            Data.getInstance().setUpdateHappeningList(list, true);
                            Data.getInstance().releaseWrite("Mainhappening");

                            getRequestSent.set(true);
                            DbActions.getInstance().getHappeningsFromDB(
                                    getActivity(),
                                    adapter,
                                    new GetHappeningsRequest(
                                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                            selectDate1EditText.getText().toString(),
                                            selectDate2EditText.getText().toString()),
                                            getRequestSent);
                        } else {
                            //Show alert vindow
                            String title = "Alert";
                            AlertDialog.Builder alert;
                            alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle(title);
                            alert.setMessage("First date cannot be later than second!");
                            alert.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            alert.show();
                        }
                    } catch (ParseException e) {

                    }
                }
                else {
                    Toast.makeText(getContext(),"In progress",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}
