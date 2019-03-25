package com.example.happening;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ReadWrite {
    private final String TAG = "File reader/writer";
    public boolean writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("ip.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
        return false;
    }

    public String readFromFile(Context context) {

        String retVal = "";

        try {
            InputStream inputStream = context.openFileInput("ip.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                retVal = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return retVal;
    }

    public ArrayList<Happening> readHappenings(Context context, String name) {
        ArrayList<Happening> retVal = new ArrayList<>();
        ObjectInputStream oIS = null;
        try {
            oIS = new ObjectInputStream(context.openFileInput(name));

            if ( oIS != null ) {
                retVal = (ArrayList<Happening>)oIS.readObject();
            }
        }
        catch(ClassNotFoundException e){
            Log.e(TAG, "File not found: " + e.toString());
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }finally {
            if(oIS!= null){
                try {
                    oIS.close();
                }
                catch (IOException e){
                    Log.e(TAG, "Can not close iOS: " + e.toString());
                }
            }
        }

        return retVal;
    }

    /**
     * Writes happening list to file
     * @param context
     * @param name
     * @param list
     * @return
     */
    public boolean writeHappenings(Context context, String name, ArrayList<Happening> list) {

        boolean retVal = false;
        ObjectOutputStream oOS = null;
        try{
            oOS = new ObjectOutputStream(context.openFileOutput(name,Context.MODE_PRIVATE));
            oOS.writeObject(list);
            retVal = true;

        }
        catch (FileNotFoundException e){
            Log.e(TAG, "File not found: " + e.toString());
        }
        catch (IOException e){
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        finally {
            if (oOS !=null){
                try{
                    oOS.close();
                }
                catch (IOException e){
                    Log.e(TAG, "Can not close oOS: " + e.toString());
                }
            }
        }

        return retVal;
    }


}
