package com.example.happening;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.happening.DbStuff.Data;
import com.example.happening.DbStuff.DbActions;
import com.example.happening.DbStuff.GetHappeningsRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class CommonTools {

    /**
     * starts time picker
     * @param activity
     * @param textView
     * @param retainDate
     */
    public static void pickDate(Activity activity, final TextView textView, boolean retainDate){
        final int year;
        final int month;
        final int day;

        if(retainDate) {
            String tmp = textView.getText().toString();
            year = Integer.parseInt(tmp.substring(0, 4));
            month = Integer.parseInt(tmp.substring(5, 7)) - 1;
            day = Integer.parseInt(tmp.substring(8, 10));
        }
        else{
            Date date = Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String monthStr;
                        month = month + 1;
                        monthStr = (month < 10)? "0"+String.valueOf(month) : String.valueOf(month);

                        String dayStr = (day<10)? "0"+String.valueOf(day) : String.valueOf(day);

                        textView.setText(year + "-" + monthStr + "-" + dayStr);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Starts time picker
     * @param activity
     * @param textView
     */
    public static void pickTime(Activity activity, final TextView textView){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                String time = String.format("%02d:%02d", hour, minute);
                textView.setText(time);
            }
        };

        Calendar now = Calendar.getInstance();
        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = now.get(java.util.Calendar.MINUTE);

        // Whether show time in 24 hour format or not.
        boolean is24Hour = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, onTimeSetListener, hour, minute, is24Hour);

        timePickerDialog.setTitle("Please select time.");

        timePickerDialog.show();
    }

    /**
     * Gets current day in correct format
     * @return
     */
    public static String getToDay(){
        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH)+1;
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        String dateStr = String.valueOf(year);
        dateStr += (month < 10)? "-0"+String.valueOf(month) : "-"+String.valueOf(month);
        dateStr += (day < 10)? "-0"+String.valueOf(day) : "-"+String.valueOf(day);

        return dateStr;
    }

    /**
     * Adds dates and compares
     * @return
     */
    public static Comparator<Happening> getCompByDate()
    {
        Comparator comp = new Comparator<Happening>(){
            @Override
            public int compare(Happening s1, Happening s2)
            {
                Integer s1Date = Integer.valueOf(s1.getDate().substring(0,4))*10000;
                s1Date += Integer.valueOf(s1.getDate().substring(5,7))*100;
                s1Date += Integer.valueOf(s1.getDate().substring(8,10));

                Integer s2Date = Integer.valueOf(s2.getDate().substring(0,4))*10000;
                s2Date += Integer.valueOf(s2.getDate().substring(5,7))*100;
                s2Date += Integer.valueOf(s2.getDate().substring(8,10));

                return s1Date.compareTo(s2Date);
            }
        };
        return comp;
    }

}
