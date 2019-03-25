package com.example.happening;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.happening.DbStuff.AddHappening;
import com.google.firebase.auth.FirebaseAuth;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateHappening extends Fragment {


    private EditText editTextDate;
    private EditText editTextTime;
    private TextView timePickerValueTextView;
    private boolean check = true;
    private AlertDialog.Builder alert;

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

        editTextDate = (EditText) view.findViewById(R.id.editTextDate);


        ImageButton buttonDate = view.findViewById(R.id.buttonDate);
        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                editTextDate.setText(year + "/" + (month + 1) + "/" + day);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        editTextTime = (EditText) view.findViewById(R.id.editTextTime);
//        ImageButton buttonTime = (ImageButton) view.findViewById(R.id.buttonTime);

        ImageButton timePickerDialogButton = (ImageButton) view.findViewById(R.id.buttonTime);
        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        String time = String.format("%02d:%02d", hour, minute);

                        timePickerValueTextView = (TextView) getView().findViewById(R.id.editTextTime);
                        timePickerValueTextView.setText(time);
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, is24Hour);

                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }

        });


        Button createButton = (Button) view.findViewById(R.id.buttonCreate);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createButtonPressed(view);
            }
        });
        return view;
    }

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
                Happening happening = new Happening(
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        name,
                        date,
                        time,
                        city,
                        description);
                addHappeningToDB(happening);
                //Replaced with toast in AddHappening
//                errorString = "You successfully created a Happening!";
//                callAlert(errorString);
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


    private boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        boolean flag = true;

        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            flag = false;
        }
        return flag;

    }

    private boolean isValidTime(String time) {
        boolean flag = true;

        if (!time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) {
            flag = false;
        }
        return flag;
    }

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

    private boolean addHappeningToDB(final Happening happening){
        Thread th = new Thread(new AddHappening(happening, getActivity()));

        th.start();

        return false;
    }
}