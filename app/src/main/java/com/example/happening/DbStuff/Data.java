package com.example.happening.DbStuff;

import com.example.happening.Happening;

import java.util.ArrayList;

public class Data {
    ArrayList<Happening> allHappeningsList;
    ArrayList<Happening> myHappeningsList;
    ArrayList<Happening> toDbHappeningsList;

    private static final Data ourInstance = new Data();

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
    }
}
