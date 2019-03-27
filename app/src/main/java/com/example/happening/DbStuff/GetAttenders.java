package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.happening.AttendersListAdapter;
import com.example.happening.Comment;
import com.example.happening.CommentListAdapter;
import com.example.happening.Happening;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GetAttenders implements Runnable {
    private final Activity mainActivity;
    private final Happening happening;
    private AttendersListAdapter attendersListAdapter;

    public GetAttenders(Activity mainActivity, Happening happening, AttendersListAdapter attendersListAdapter) {
        this.mainActivity = mainActivity;
        this.happening = happening;
        this.attendersListAdapter = attendersListAdapter;
    }

    @Override
    public void run() {
        final ReturnValue retVal;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mainActivity != null) {
                    Toast.makeText(mainActivity, "Downloading attenders", Toast.LENGTH_LONG).show();
                }
            }
        });
        SocketConnect sC = new SocketConnect();
        sC.getAttenders(happening);
        AsyncTask aT = sC.execute();
        try {
            String retValMessage = "Something is very wrong";
            retVal = (ReturnValue) aT.get();

            switch(retVal){
                case SUCCESS:
                    retValMessage = "Download complete!";
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
                        Data.getInstance().acquireWrite(this.toString());
                        ArrayList<String> listToUpdate = Data.getInstance().getUpdateAttendersList();
                        listToUpdate.clear();
                        for (String s: Data.getInstance().getAttendersList()){
                            listToUpdate.add(s);
                        }
                        attendersListAdapter.notifyDataSetChanged();
                        Data.getInstance().releaseWrite(this.toString());
                    }
                    Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}
