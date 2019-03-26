package com.example.happening.DbStuff;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.happening.Comment;
import com.example.happening.CommentListAdapter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddComment implements Runnable {
    private Comment comment;
    private final Activity mainActivity;
    final private String TAG = this.toString();
    private CommentListAdapter commentListAdapter;
    private AtomicBoolean requestSent;

    /**
     * Constructor Add happening
     * @param comment to add
     * @param activity activity for toast
     */
    public AddComment(Comment comment, Activity activity, CommentListAdapter commentListAdapter, AtomicBoolean requestSent){
        this.comment = comment;
        this.mainActivity = activity;
        this.commentListAdapter = commentListAdapter;
        this.requestSent = requestSent;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        final ReturnValue retVal;
        SocketConnect sC = new SocketConnect();
        sC.addComment(comment);
        AsyncTask aT = sC.execute();

        try {
            retVal = (ReturnValue) aT.get();
            String retValMessage = "";
            switch(retVal){
                case SUCCESS:
                    retValMessage = "Comment added Successfully!";
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
                        Data.getInstance().getComments().add(comment);
                        Data.getInstance().getUpdateCommentList().add(comment);
                        Data.getInstance().releaseWrite(this.toString());
                        commentListAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(mainActivity, message, Toast.LENGTH_LONG).show();

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
