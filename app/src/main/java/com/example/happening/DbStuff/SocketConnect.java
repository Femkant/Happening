package com.example.happening.DbStuff;

import android.os.AsyncTask;
import android.util.Log;
import com.example.happening.Happening;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static android.content.ContentValues.TAG;

/**
 * For connecting to server and send DB info
 */
public class SocketConnect extends AsyncTask<Void, Void, Boolean>{

    public static String HOST ="";
    private final int PORT = 6969;
    private Socket socket;
    private ObjectOutputStream oOS;
    private ObjectInputStream oIS;
    private Happening happening;
    private String command;

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean retVal = false;

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
                    case "addEventToDb":

                        if(addhappeningInt())
                            //Added sucessfully
                            retVal = true;
                        break;
                    case "addUserToDb":
                        break;

                }
            }
            else{
                Log.d("AddHappening", "doInBackground: Socket not connected");
            }
        }
        catch (Exception e){
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
            command = "addEventToDb";
    }

    /**
     * Writes to server
     * @return true if successful
     * @throws IOException
     */
    private boolean addhappeningInt() throws IOException{
        oOS.writeObject(command);
        oOS.flush();
        oOS.writeObject(happening);
        oOS.flush();
        return true;
    }
}
