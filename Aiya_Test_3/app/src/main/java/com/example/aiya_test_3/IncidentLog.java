package com.example.aiya_test_3;

public class IncidentLog {
    // this class implements the singleton (creational) design pattern
    // purpose: tracking of incidents to ensure accountability.

    private static IncidentLog instance = null;
    private String log = "";

    // private constructor
    private IncidentLog() {
    }

    public static IncidentLog getInstance() {
        // check if there are any instances of logfile
        if (instance == null) {
            // create an instance of incident log file
            instance = new IncidentLog();
            // table label formatting
            // |Date   |Activity Name |Location | but each segment has 15 spaces.
            instance.INFO(String.format("%-12s","|Date") + "|" + String.format("%-15s","Activity Name") + "|" + String.format("%-15s","Location") + "|");
        }
        return instance;
    }

    // record log
    public void INFO(String msg) {
        log += "\n" + msg; // log activities down.
    }

    public String recordLog(){
        return log;
    }
}
