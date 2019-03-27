package com.example.happening.DbStuff;

import android.app.Activity;
import android.widget.Button;

import com.example.happening.AttendersListAdapter;
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
    public void addHappeningToDB(final Happening happening, Activity activity, AtomicBoolean requestSent){
        Thread th = new Thread(new AddHappening(happening, activity, requestSent));

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

    /**
     * Add comment to DB
     * @param comment
     * @param activity
     * @param commentListAdapter
     * @param requestSent
     */
    public void addComment(Comment comment, Activity activity,CommentListAdapter commentListAdapter, AtomicBoolean requestSent){
        Thread th = new Thread(new AddComment(comment,activity,commentListAdapter,requestSent));

        th.start();
    }

    /**
     * get comments from DB
     * @param happening
     * @param activity
     * @param commentListAdapter
     */
    public void getComments(Happening happening, Activity activity, CommentListAdapter commentListAdapter){
        Thread th = new Thread(new GetComments(activity,happening,commentListAdapter ));

        th.start();
    }

    /**
     * Get attenders from DB
     * @param happening
     * @param activity
     * @param commentListAdapter
     */
    public void getAttenders(Happening happening, Activity activity, AttendersListAdapter attendersListAdapter){
        Thread th = new Thread(new GetAttenders(activity,happening,attendersListAdapter ));

        th.start();
    }
}
