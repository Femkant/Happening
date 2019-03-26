package com.example.happening;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.happening.DbStuff.Data;
import com.example.happening.DbStuff.DbActions;
import com.google.firebase.auth.FirebaseAuth;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateHappening extends Fragment {


    private EditText editTextDate;
    private EditText editTextTime;
    private TextView timePickerValueTextView;
    private boolean check = true;
    private AlertDialog.Builder alert;
    private long delayTime = 0, delayDate = 0;

    public CreateHappening() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_happening, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create happening");

        if (!MySharedPref.getInstance().getSharedPref().loadNightModeState()){
            ConstraintLayout constraintLayout =(ConstraintLayout) view.findViewById(R.id.bg_create);
            constraintLayout.setBackgroundResource(R.drawable.bg);
        }

        //For picking Date
        editTextDate = (EditText) view.findViewById(R.id.editTextDate);
        editTextDate.setClickable(true);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(System.currentTimeMillis() > delayDate + Data.getInstance().BUTTON_DELAY_MS) {
                    delayDate = System.currentTimeMillis();
                    CommonTools.pickDate(getActivity(), editTextDate, false);
                }
            }
        });
        //For picking Date
        ImageButton buttonDate = view.findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(System.currentTimeMillis() > delayDate + Data.getInstance().BUTTON_DELAY_MS) {
                    delayDate = System.currentTimeMillis();
                    CommonTools.pickDate(getActivity(), editTextDate, false);
                }
            }
        });
        //For picking time
        editTextTime = (EditText) view.findViewById(R.id.editTextTime);
        editTextTime.setClickable(true);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(System.currentTimeMillis() > delayTime + Data.getInstance().BUTTON_DELAY_MS) {
                    delayTime = System.currentTimeMillis();
                    CommonTools.pickTime(getActivity(), editTextTime);
                }
            }
        });

        //Start timepicker dialog
        ImageButton timePickerDialogButton = (ImageButton) view.findViewById(R.id.buttonTime);

        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(System.currentTimeMillis() > delayTime + Data.getInstance().BUTTON_DELAY_MS) {
                    delayTime = System.currentTimeMillis();
                    CommonTools.pickTime(getActivity(), editTextTime);
                }
            }

        });

        //Try to create happening
        Button createButton = (Button) view.findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createButtonPressed(view);
            }
        });
        return view;
    }

    /**
     * Checks parameters and creates happening
     * @param view
     */
    public void createButtonPressed(View view) {
        EditText eName = view.findViewById(R.id.editTextName);
        String name = eName.getText().toString();

        String date = editTextDate.getText().toString();

        String time = editTextTime.getText().toString();

        EditText eCity = view.findViewById(R.id.editTextCity);
        String city = eCity.getText().toString();

        TextView tDes = view.findViewById(R.id.textViewDescription);
        String description = tDes.getText().toString();

        check = true;
        String errorString = "";

        //Check parameters
        if (name.length() > 100 || name.length() < 2) {
            check = false;
            errorString = "Invalid name!";

        } else if (!isValidDate(date)) {
            check = false;
            errorString = "Invalid date format!";

        } else if (!isValidTime(time)) {
            check = false;
            errorString = "Invalid time format!";

        } else if (containsDigit(city) || city.length() < 2) {
            check = false;
            errorString = "Invalid city!";

        } else if (description.length() > 300) {
            check = false;
            errorString = "To long description, please use max 300 characters.";

        } else if (description.length() < 1) {
            check = false;
            errorString = "Please add a description";
        }

        if (check) {
            //Create happening
            Happening happening = new Happening(
                    FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    name,
                    date,
                    time,
                    city,
                    description);
            DbActions.getInstance().addHappeningToDB(happening,getActivity());

        } else if (!check) {
            callAlert(errorString);
        }
    }

    private void callAlert(String error) {
        String title = "Alert";
        if (check) {
            title = "Congratulations!";
        }
        alert = new AlertDialog.Builder(getContext());
        alert.setTitle(title);
        alert.setMessage(error);
        alert.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (check) {
                        } else
                            dialog.dismiss();
                    }
                });

        alert.show();
    }

    /**
     * Checks date
     * @param date
     * @return
     */
    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = true;

        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            flag = false;
        }
        return flag;

    }

    /**
     * Checks time
     * @param time
     * @return
     */
    private boolean isValidTime(String time) {
        boolean flag = true;
        if (!time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            flag = false;
        }
        return flag;
    }

    /**
     * Check if string contains digit
     * @param s
     * @return
     */
    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    containsDigit = true;
                    break;

                }
            }
        }

        return containsDigit;
    }
}