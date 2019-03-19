package com.example.happening;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;


public class CreateEvent extends AppCompatActivity {

    private EditText editTextDate;
    private EditText editTextTime;
    private boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        setDate();
        setTime();

    }


    private void setDate() {
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        ImageButton buttonDate = (ImageButton) findViewById(R.id.buttonDate);

        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                editTextDate.setText(year + "/" + (month + 1) + "/" + day);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }

        });
    }

    private void setTime() {
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        ImageButton buttonTime = (ImageButton) findViewById(R.id.buttonTime);

        ImageButton timePickerDialogButton = (ImageButton) findViewById(R.id.buttonTime);
        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        String time = String.format("%02d:%02d", hour, minute);

                        TextView timePickerValueTextView = (TextView) findViewById(R.id.editTextTime);
                        timePickerValueTextView.setText(time);
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, onTimeSetListener, hour, minute, is24Hour);

                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });
    }

    public void createButtonPressed(View view) {
        EditText eName = findViewById(R.id.editTextName);
        String name = eName.getText().toString();

        String date = editTextDate.getText().toString();

        String time = editTextTime.getText().toString();

        EditText eCity = findViewById(R.id.editTextCity);
        String city = eCity.getText().toString();

        TextView tDes = findViewById(R.id.textViewDescription);
        String description = tDes.getText().toString();

        check = true;

        if (description.length() > 300) {
            callAlert("To long description, please use max 300 characters.");
        } else if (description.length() < 1) {
            check = false;
            callAlert("Please add a description");
        }

        //TO DO.. ADD CITY ERROR DETECTION

        if (!isValidTime(time)) {
            check = false;
            callAlert("Invalid time format!");
        }

        if (!isValidDate(date)) {
            check = false;
            callAlert("Invalid date format!");
        }

        if (name.length() > 100 || name.length() < 2) {
            check = false;
            callAlert("Invalid name!");
        }

        //CREATE HAPPENING OBJECT??
        if (check) {
            Happening happening = new Happening(name, date, time, city, description);
            callAlert("You successfully created a Happening!");
        }
    }

    private void callAlert(String error) {
        String title = "Alert";
        if (check){title = "Congratulations!";}
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (check){
                            finish();
                        }else
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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
}




