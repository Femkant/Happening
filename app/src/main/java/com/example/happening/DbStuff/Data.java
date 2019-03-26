package com.example.happening.DbStuff;

import android.util.Log;

import com.example.happening.Comment;
import com.example.happening.Happening;

import java.util.ArrayList;

public class Data {
    private ArrayList<Happening> allHappeningsList = new ArrayList<Happening>();
    private ArrayList<Happening> myHappeningsList = new ArrayList<Happening>();
    private ArrayList<Happening> updateHappeningList = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Comment> updateCommentList = new ArrayList<>();
    private boolean updateMyHappenings;
    private final String TAG = "DataSingleton";
    public final long BUTTON_DELAY_MS = 500;
    //For lock between threads
    private int readCount = 0;
    private boolean writeLock = false;
    private int writerWaiting = 0;
    //******** Lock*****************

    private static final Data ourInstance = new Data();

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
    }

    public ArrayList<Happening> getAllHappeningsList() {
        return allHappeningsList;
    }

    public void setAllHappeningsList(ArrayList<Happening> allHappeningsList) {
        this.allHappeningsList = allHappeningsList;
    }

    public ArrayList<Happening> getMyHappeningsList() {
        return myHappeningsList;
    }

    public void setMyHappeningsList(ArrayList<Happening> myHappeningsList) {
        this.myHappeningsList = myHappeningsList;
    }

    public ArrayList<Happening> getUpdateHappeningList() {
        return updateHappeningList;
    }

    public void setUpdateHappeningList(ArrayList<Happening> updateList, boolean updateMyHappenings) {
        this.updateHappeningList = updateList;
        this.updateMyHappenings = updateMyHappenings;
    }

    public boolean getUpdateMyHappenings(){
        return updateMyHappenings;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Comment> getUpdateCommentList() {
        return updateCommentList;
    }

    public void setUpdateCommentList(ArrayList<Comment> updateCommentList) {
        this.updateCommentList = updateCommentList;
    }

    //*********************** LOCK ***********************************

    /**
     * Aquire Read access
     */
    public synchronized void acquireRead(String id){
        while (writeLock && readCount<1 || writerWaiting>0){
            try {
                Log.d(TAG, "acquireRead: (wait)"+id);
                wait();
            }
            catch (InterruptedException e){

            }
        }
        writeLock =true;
        Log.d(TAG, "acquireRead: "+id);
        readCount++;
    }

    /**
     * Aquire write access
     */
    public synchronized void acquireWrite(String id){
        writerWaiting++;
        while (writeLock){
            try
            {
                Log.d(TAG, "acquireWrite: (wait)"+id);
                wait();
            }
            catch (InterruptedException e) { }
        }
        writerWaiting--;
        Log.d(TAG, "acquireWrite: "+id);
        writeLock = true;
    }

    /**
     * Release read
     */
    public synchronized void releaseRead(String id){

        if(readCount<2){
            writeLock = false;
        }
        readCount--;
        Log.d(TAG, "releaseRead: "+id);
        notifyAll();
    }

    /**
     * release write
     */
    public synchronized void releaseWrite(String id){
        writeLock = false;
        Log.d(TAG, "releaseWrite: "+id);
        notifyAll();
    }
}
