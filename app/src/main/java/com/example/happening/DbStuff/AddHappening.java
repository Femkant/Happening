package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.happening.Happening;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to run thread to add happenning to DB
 */
public class AddHappening implements Runnable {
    private Happening happening;
    private final Activity mainActivity;
    private AtomicBoolean requestSent;

    /**
     * Constructor Add happening
     * @param happening to add
     * @param activity activity for toast
     */
    public AddHappening(Happening happening, Activity activity, AtomicBoolean requestSent){
        this.happening = happening;
        this.mainActivity = activity;
        this.requestSent = requestSent;
    }

    @Override
    public void run() {
        final ReturnValue retVal;
        SocketConnect sC = new SocketConnect();
        sC.addHappening(happening);
        AsyncTask aT = sC.execute();

        try {
            retVal = (ReturnValue) aT.get();
            String retValMessage = "";
            switch(retVal){
                case SUCCESS:
                    retValMessage = "Happening added Successfully!";
                    break;

                case NO_CONN_TO_DB:
                    retValMessage = "ERROR in database connenction.";
                    break;

                case GENERAL_FAILURE:
                    retValMessage = "ERROR some place.. some where.";
                    break;

                case NO_CONN_TO_SERVER:
                    retValMessage = "Connection to server = super DEAD!";
                    break;

                default:

            }
            final String message = retValMessage;
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
        finally {
            requestSent.set(false);
        }
    }
}
