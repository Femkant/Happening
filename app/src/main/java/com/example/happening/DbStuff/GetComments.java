package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.happening.Comment;
import com.example.happening.CommentListAdapter;
import com.example.happening.Happening;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Gets comments from database
 */
public class GetComments implements Runnable {
    private final Activity mainActivity;
    private final Happening happening;
    private CommentListAdapter commentListAdapter;

    public GetComments(Activity mainActivity, Happening happening, CommentListAdapter commentListAdapter) {
        this.mainActivity = mainActivity;
        this.happening = happening;
        this.commentListAdapter = commentListAdapter;
    }

    @Override
    public void run() {
        final ReturnValue retVal;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mainActivity != null) {
                    Toast.makeText(mainActivity, "Downloading comments", Toast.LENGTH_LONG).show();
                }
            }
        });
        SocketConnect sC = new SocketConnect();
        sC.getComments(happening);
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
                            ArrayList<Comment> listToUpdate = Data.getInstance().getUpdateCommentList();
                            listToUpdate.clear();
                            for (Comment c: Data.getInstance().getComments()){
                                listToUpdate.add(c);
                            }
                            commentListAdapter.notifyDataSetChanged();
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
