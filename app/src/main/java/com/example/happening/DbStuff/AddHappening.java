package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.happening.Happening;

import java.util.concurrent.ExecutionException;

/**
 * Class to run thread to add happenning to DB
 */
public class AddHappening implements Runnable {
    private Happening happening;
    private final Activity mainActivity;

    /**
     * Constructor Add happening
     * @param happening to add
     * @param activity activity for toast
     */
    public AddHappening(Happening happening, Activity activity){
        this.happening = happening;
        this.mainActivity = activity;
    }

    @Override
    public void run() {
        Boolean retVal;

        SocketConnect sC = new SocketConnect();
        sC.addHappening(happening);
        AsyncTask aT = sC.execute();

        try {
            final String message;
            retVal = (Boolean) aT.get();

            if(retVal){
                message = "Happening added Successfully!";
            }
            else{
                message = "Happening got lost in translation...";
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
