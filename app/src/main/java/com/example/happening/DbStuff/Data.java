package com.example.happening.DbStuff;

import android.util.Log;

import com.example.happening.Comment;
import com.example.happening.Happening;

import java.util.ArrayList;

/**
 * Singleton for sharing data between classes and threads
 */
public class Data {
    private ArrayList<Happening> allHappeningsList = new ArrayList<Happening>();
    private ArrayList<Happening> myHappeningsList = new ArrayList<Happening>();
    private ArrayList<Happening> updateHappeningList = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Comment> updateCommentList = new ArrayList<>();
    private ArrayList<String> attendersList = new ArrayList<>();
    private ArrayList<String> updateAttendersList = new ArrayList<>();
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

    /**
     * get list for unattending happenings
     * @return list of unattending happenings
     */
    public ArrayList<Happening> getAllHappeningsList() {
        return allHappeningsList;
    }

    /**
     * Set list for unattending happenings
     * @param allHappeningsList happenings to store
     */
    public void setAllHappeningsList(ArrayList<Happening> allHappeningsList) {
        this.allHappeningsList = allHappeningsList;
    }

    /**
     * get list for unattending happenings
     * @return list of attending happenings
     */
    public ArrayList<Happening> getMyHappeningsList() {
        return myHappeningsList;
    }

    /**
     * Set list for attending happenings
     * @param myHappeningsList Happenings to store
     */
    public void setMyHappeningsList(ArrayList<Happening> myHappeningsList) {
        this.myHappeningsList = myHappeningsList;
    }

    /**
     * List to update for bg task
     * @return list of happenings
     */
    public ArrayList<Happening> getUpdateHappeningList() {
        return updateHappeningList;
    }

    /**
     * Set list for update for bg task
     * @param updateList happenings
     * @param updateMyHappenings is attending happening if true
     */
    public void setUpdateHappeningList(ArrayList<Happening> updateList, boolean updateMyHappenings) {
        this.updateHappeningList = updateList;
        this.updateMyHappenings = updateMyHappenings;
    }

    /**
     * Inicates if it is attending happenings that should be updated
     * @return boolean true if attending happenings that should be updated
     */
    public boolean getUpdateMyHappenings(){
        return updateMyHappenings;
    }

    /**
     * Get list of comments
     * @return list of stored comments
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Sets comments
     * @param comments  comments
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Comment list to update
     * @return  comments
     */
    public ArrayList<Comment> getUpdateCommentList() {
        return updateCommentList;
    }

    /**
     * Sets comments
     * @param updateCommentList comments
     */
    public void setUpdateCommentList(ArrayList<Comment> updateCommentList) {
        this.updateCommentList = updateCommentList;
    }

    /**
     * Attending users list
     * @return
     */
    public ArrayList<String> getAttendersList() {
        return attendersList;
    }

    /**
     * Sets list of attending users
     * @param attendersList ArrayList<String> attendersList
     */
    public void setAttendersList(ArrayList<String> attendersList) {
        this.attendersList = attendersList;
    }

    /**
     * List of attenders to be updated
     * @return list of attenders
     */
    public ArrayList<String> getUpdateAttendersList() {
        return updateAttendersList;
    }

    /**
     * Set comment list to be updated
     * @param updateAttendersList list to be updated
     */
    public void setUpdateAttendersList(ArrayList<String> updateAttendersList) {
        this.updateAttendersList = updateAttendersList;
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
