package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.happening.Happening;
import com.example.happening.HappeningListAdapter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class to run thread to get all happenning from DB
 */
public class GetHappenings implements Runnable {
    private final Activity mainActivity;
    private final HappeningListAdapter adapter;
    private final GetHappeningsRequest getHReq;
    private AtomicBoolean getRequest;

    public GetHappenings(Activity mainActivity, HappeningListAdapter adapter, GetHappeningsRequest getHReq, AtomicBoolean getRequest) {
        this.mainActivity = mainActivity;
        this.adapter = adapter;
        this.getHReq = getHReq;
        this.getRequest = getRequest;
    }

    @Override
    public void run() {
        final ReturnValue retVal;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mainActivity != null) {
                    Toast.makeText(mainActivity, "Downloading happenings", Toast.LENGTH_LONG).show();
                }
            }
        });
        SocketConnect sC = new SocketConnect();
        sC.getHappenings(getHReq);
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
                    ArrayList<Happening> listNew;

                    if (retVal == ReturnValue.SUCCESS) {
                        Data.getInstance().acquireWrite(this.toString());
                        ArrayList<Happening> list = Data.getInstance().getUpdateHappeningList();

                        if (Data.getInstance().getUpdateMyHappenings()) {
                            listNew = Data.getInstance().getMyHappeningsList();
                        } else {
                            listNew = Data.getInstance().getAllHappeningsList();
                        }
                        list.clear();
                        for (Happening h : listNew) {
                            list.add(h);
                        }
                        Data.getInstance().releaseWrite(this.toString());
                        adapter.notifyDataSetChanged();
                    }
                    getRequest.set(false);
                    Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            getRequest.set(false);
        }


    }
}
