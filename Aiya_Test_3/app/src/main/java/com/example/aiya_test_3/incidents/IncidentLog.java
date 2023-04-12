package com.example.aiya_test_3.incidents;

public class IncidentLog {
    /*DESCRIPTION
     * The purpose of the IncidentLog is to record all status changes to an incident.
     * Eg: Incident creation, Incident resolution or updates (by NEA).
     *
     * This class applies the singleton (creational) design pattern by creating only on instance
     * of the IncidentLog locally. The reason for this pattern is to ensure that the statuses of
     * incidents are changed in a linear fashion.
     *
     * (Yet to be implemented) In the android lifecycle, onDestroy() should upload the incident log
     * to the database for documentation.
     *
     * METHODS:
     * INFO: records down a status change (upload, update or resolution etc) in the incidents.
     * returnIncidents: return the entire log (should be called onDestroy of the android lifecycle)
     * */

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

    public String returnIncidents(){
        return log;
    }
}
