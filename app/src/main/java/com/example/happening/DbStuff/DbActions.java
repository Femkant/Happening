package com.example.happening.DbStuff;

import android.app.Activity;
import android.widget.Button;

import com.example.happening.Comment;
import com.example.happening.CommentListAdapter;
import com.example.happening.Happening;
import com.example.happening.HappeningListAdapter;
import com.example.happening.ShowHappening;

import java.util.concurrent.atomic.AtomicBoolean;

public class DbActions {
    private static final DbActions ourInstance = new DbActions();

    public static DbActions getInstance() {
        return ourInstance;
    }

    private DbActions() {
    }

    /**
     * Adds happening to DB
     * @param happening Happening
     * @param activity Activity
     */
    public void addHappeningToDB(final Happening happening, Activity activity){
        Thread th = new Thread(new AddHappening(happening, activity));

        th.start();
    }

    /**
     * Gets happenings from DB between dates set in GetHappeningsRequest
     * @param activity Activity
     * @param adapter HappeningListAdapter
     * @param getHReq GetHappeningsRequest
     */
    public void getHappeningsFromDB(Activity activity, HappeningListAdapter adapter, GetHappeningsRequest getHReq, AtomicBoolean getRequest){
        Thread th = new Thread(new GetHappenings(activity, adapter, getHReq, getRequest));

        th.start();
    }

    /**
     * Adds attend to DB
     * @param activity
     * @param attendRequest
     */
    public void addAttend(Activity activity, GetAttendRequest attendRequest,AtomicBoolean attendRequestInprogress, Button attendButton,Happening happening){
        Thread th = new Thread(new AddAttend(activity,attendRequest, attendRequestInprogress, attendButton, happening));

        th.start();
    }

    /**
     * Deletes attend from DB
     * @param activity
     * @param attendRequest
     */
    public void deleteAttend(Activity activity, GetAttendRequest attendRequest, AtomicBoolean attendRequestInprogress, Button attendButton,Happening happening){
        Thread th = new Thread(new DeleteAttend(activity,attendRequest,attendRequestInprogress, attendButton, happening));

        th.start();
    }

    public void addComment(Comment comment, Activity activity,CommentListAdapter commentListAdapter){
        Thread th = new Thread(new AddComment(comment,activity,commentListAdapter));

        th.start();
    }

    public void getComments(Happening happening, Activity activity, AtomicBoolean getCommentRequest){
        Thread th = new Thread(new GetComments(activity,happening,getCommentRequest ));

        th.start();
    }
}
