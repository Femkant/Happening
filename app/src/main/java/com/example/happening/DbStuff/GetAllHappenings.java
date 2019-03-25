package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;

/**
 * Class to run thread to get all happenning from DB
 */
public class GetAllHappenings implements Runnable {
    private final Activity mainActivity;

    public GetAllHappenings(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Boolean retVal;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mainActivity != null) {
                    Toast.makeText(mainActivity, "Downloading happenings", Toast.LENGTH_LONG).show();
                }
            }
        });
        SocketConnect sC = new SocketConnect();
        sC.getAllHappenings();
        AsyncTask aT = sC.execute();

        try {
            final String message;
            retVal = (Boolean) aT.get();

            if(retVal){

                message = "Happening added Successfully!";
            }
            else{
                message = "Happenings has run away...";
            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mainActivity != null) {
                        Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}
