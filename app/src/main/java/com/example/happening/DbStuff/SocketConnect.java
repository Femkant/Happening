package com.example.happening.DbStuff;

import android.os.AsyncTask;
import android.util.Log;

import com.example.happening.Comment;
import com.example.happening.Happening;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * For connecting to server and send DB info
 */
public class SocketConnect extends AsyncTask<Void, Void, ReturnValue>{

    public static String HOST ="";
    private final int PORT = 6969;
    private Socket socket;
    private ObjectOutputStream oOS;
    private ObjectInputStream oIS;
    private Happening happening;
    private String command;
    private ReturnValue retVal = ReturnValue.GENERAL_FAILURE;
    private GetHappeningsRequest getHReq;
    private GetAttendRequest attendRequest;
    private final String TAG = "SocketConnect";
    private Comment comment;

    @Override
    protected ReturnValue doInBackground(Void... voids) {

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(HOST, PORT),10000);
            oOS = new ObjectOutputStream(socket.getOutputStream());
            oIS = new ObjectInputStream(socket.getInputStream());
            Log.d(TAG, "SocketConnect: "+ socket.isConnected());
        }
        catch(IOException ioe){
            Log.d(TAG, "SocketConnect: "+ioe.getMessage());
        }

        try {
            if(socket.isConnected()) {
                switch (command) {

                    case "addHappeningToDb":
                        addHappeningInt();

                        break;

                    case "getHappenings":
                        getHappeningsInt();
                        break;

                    case "deleteAttend":
                        deleteAttendInt();
                        break;

                    case "addAttend":
                        addAttendInt();
                        break;

                    case "addComment":
                        addCommentsInt();
                        Log.d(TAG, "doInBackground: ********************************************");
                        break;

                    case "getComments":
                        getCommentsInt();
                        break;
                        
                    default:
                }
            }
            else{
                retVal = ReturnValue.NO_CONN_TO_SERVER;
                Log.d("AddHappening", "doInBackground: Socket not connected");
            }
        }
        catch (Exception e){
            retVal = ReturnValue.GENERAL_FAILURE;
            Log.d(TAG, "doInBackground: "+e.getMessage());
        }
        finally {
            try {
                if(oOS != null)
                    oOS.close();
                if(oIS != null)
                    oIS.close();
                if(socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }

    /**
     * Add happening to database
     * @param happening to be added
     */
    public void addHappening(Happening happening){
            this.happening = happening;
            command = "addHappeningToDb";
    }

    /**
     * Writes to server
     * @return true if successful
     * @throws IOException
     */
    private void addHappeningInt() throws IOException, ClassNotFoundException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(happening);
        oOS.flush();
        retVal = (ReturnValue) oIS.readObject();
    }

    /**
     * Get happenings parameters
     * @param getHReq
     */
    public void getHappenings( GetHappeningsRequest getHReq){
        command = "getHappenings";
        this.getHReq = getHReq;

    }

    /**
     * Get happenings communication with server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void getHappeningsInt() throws IOException,ClassNotFoundException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(getHReq);
        oOS.flush();
        ArrayList<ArrayList<Happening>> list;
        list = (ArrayList<ArrayList<Happening>>)oIS.readObject();
        retVal = (ReturnValue) oIS.readObject();

        if(retVal == ReturnValue.SUCCESS){
            Data.getInstance().acquireWrite(this.toString());
            Data.getInstance().setAllHappeningsList(list.get(0));
            Data.getInstance().setMyHappeningsList(list.get(1));
            Data.getInstance().releaseWrite(this.toString());
        }
    }

    /**
     * Delete attend parameters
     * @param attendRequest
     */
    public void deleteAttend(GetAttendRequest attendRequest){
        command = "deleteAttend";
        this.attendRequest = attendRequest;
    }

    /**
     * Delete attend communication with server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void deleteAttendInt() throws IOException,ClassNotFoundException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(attendRequest);
        oOS.flush();
        retVal = (ReturnValue) oIS.readObject();
    }

    /**
     * Add attend parameters
     * @param attendRequest
     */
    public void addAttend(GetAttendRequest attendRequest){
        command = "addAttend";
        this.attendRequest = attendRequest;
    }

    /**
     * Add attend communication with server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addAttendInt() throws IOException,ClassNotFoundException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(attendRequest);
        oOS.flush();
        retVal = (ReturnValue) oIS.readObject();
    }

    /**
     * Get comments parameters
     * @param happening
     */
    public void getComments(Happening happening){
        this.happening = happening;
        command = "getComments";
    }

    /**
     * Get comments communication with server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void getCommentsInt() throws IOException,ClassNotFoundException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(happening);
        oOS.flush();
        ArrayList<Comment> comments = (ArrayList<Comment>)oIS.readObject();
        retVal = (ReturnValue) oIS.readObject();
        if(retVal == ReturnValue.SUCCESS){
            Data.getInstance().acquireWrite(this.toString());
            Data.getInstance().setComments(comments);
            Data.getInstance().releaseWrite(this.toString());
        }
    }

    /**
     * Add comment parameters
     * @param comment
     */
    public void addComment(Comment comment){
        command = "addComment";
        this.comment = comment;
    }

    private void addCommentsInt() throws IOException,ClassNotFoundException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(comment);
        oOS.flush();
        retVal = (ReturnValue) oIS.readObject();
    }
}
