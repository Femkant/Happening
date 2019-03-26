package com.example.happening.DbStuff;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;

import com.example.happening.CommonTools;
import com.example.happening.Happening;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Deletes attend in DB
 */
public class AddAttend implements Runnable{

    private GetAttendRequest attendRequest;
    private final Activity mainActivity;
    private AtomicBoolean attendRequestInProgress;
    private Button attendButton;
    private Happening happening;

    public AddAttend(Activity mainActivity, GetAttendRequest attendRequest, AtomicBoolean attendRequestInProgress, Button attendButton, Happening happening) {
        this.attendRequest = attendRequest;
        this.mainActivity = mainActivity;
        this.attendRequestInProgress = attendRequestInProgress;
        this.attendButton = attendButton;
        this.happening = happening;
    }

    @Override
    public void run() {

        final ReturnValue retVal;
        SocketConnect sC = new SocketConnect();
        sC.addAttend(attendRequest);
        AsyncTask aT = sC.execute();

        try {
            retVal = (ReturnValue) aT.get();
            String retValMessage = "";
            switch(retVal){
                case SUCCESS:
                    retValMessage = "Attend added Successfully!";
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

                    if(retVal == ReturnValue.SUCCESS){

                        attendButton.setText("Unattend");
                        attendButton.setTextColor(Color.parseColor("#800000"));

                        happening.setAttending(true);

                        Data.getInstance().acquireWrite(this.toString());
                        Data.getInstance().getAllHappeningsList().remove(happening);
                        Data.getInstance().getMyHappeningsList().add(happening);
                        Collections.sort(Data.getInstance().getMyHappeningsList(), CommonTools.getCompByDate());
                        Data.getInstance().releaseWrite(this.toString());

                    }
                    Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        finally {
            attendRequestInProgress.set(false);
        }

    }
}
