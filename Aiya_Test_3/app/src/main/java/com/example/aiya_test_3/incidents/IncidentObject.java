package com.example.aiya_test_3.incidents;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Accounts.Observer;
import Accounts.Subject;

public class IncidentObject implements Subject {

    private ArrayList<Observer> observers;
    private String HazardID;
    private String HazardName_Input;
    private String HazardAddress_Input;
    private LatLng HazardAddress_LatLng;
    private Double HazardAddress_Lat;
    private Double HazardAddress_Long;
    private String HazardDescription_Input;
    private String HazardType_Input;
    private String HazardImage_Input;
    private long upvotes;

    DatabaseReference nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();;
    DatabaseReference nNodeRefInputDetails;

    final String hazardDetailsNode = "Incident Objects";

    // Empty Constructor
    public IncidentObject(){}

    // Overloaded Constructor with all details we want
    public IncidentObject(String HazardName_Input, String HazardAddress_Input, Double HazardAddress_Lat, Double HazardAddress_Long, String HazardDescription_Input, String HazardType_Input, String HazardImage_Input) {

        this.HazardName_Input = HazardName_Input;
        this.HazardAddress_Input = HazardAddress_Input;
        this.HazardAddress_Lat = HazardAddress_Lat;
        this.HazardAddress_Long = HazardAddress_Long;
        this.HazardDescription_Input = HazardDescription_Input;
        this.HazardType_Input = HazardType_Input;
        this.HazardImage_Input = HazardImage_Input;
        // TODO assign observers to the list of observers from the firebase.
        setUpvotes(1);
    }

    // This method saves all the data into database
    public void saveIncidentToDatabase() {
        nNodeRefInputDetails = nRootDatabaseRef.child(hazardDetailsNode);
        HashMap<String, IncidentObject> Send_database_details = new HashMap<>();

        Send_database_details.put(HazardName_Input, new IncidentObject(HazardName_Input, HazardAddress_Input, HazardAddress_Lat, HazardAddress_Long, HazardDescription_Input, HazardType_Input,  HazardImage_Input));
        DatabaseReference nNodeRefPush = nNodeRefInputDetails.push();
        nNodeRefPush.setValue(Send_database_details);
    }


    // Getter and Setter Methods
    public String getHazardID() {
        return HazardID;
    }

    public void setHazardID(String hazardID) {
        HazardID = hazardID;
    }

    public String getHazardName_Input() {
        return HazardName_Input;
    }

    public void setHazardName_Input(String hazardName_Input) {
        HazardName_Input = hazardName_Input;
    }

    public String getHazardAddress_Input() {
        return HazardAddress_Input;
    }

    public void setHazardAddress_Input(String hazardAddress_Input) {
        HazardAddress_Input = hazardAddress_Input;
    }

    public LatLng getHazardAddress_LatLng() {
        return HazardAddress_LatLng;
    }

    public void setHazardAddress_LatLng(LatLng hazardAddress_LatLng) {
        HazardAddress_LatLng = hazardAddress_LatLng;
    }

    public Double getHazardAddress_Lat() {
        return HazardAddress_Lat;
    }

    public void setHazardAddress_Lat(Double hazardAddress_Lat) {
        HazardAddress_Lat = hazardAddress_Lat;
    }

    public Double getHazardAddress_Long() {
        return HazardAddress_Long;
    }

    public void setHazardAddress_Long(Double hazardAddress_Long) {
        HazardAddress_Long = hazardAddress_Long;
    }

    public String getHazardDescription_Input() {
        return HazardDescription_Input;
    }

    public void setHazardDescription_Input(String hazardDescription_Input) {
        HazardDescription_Input = hazardDescription_Input;
    }

    public String getHazardType_Input() {
        return HazardType_Input;
    }

    public void setHazardType_Input(String hazardType_Input) {
        HazardType_Input = hazardType_Input;
    }

    public String getHazardImage_Input() {
        return HazardImage_Input;
    }

    public ArrayList<Observer> getObservers() {return observers;}

    public void setHazardImage_Input(String hazardImage_Input) {
        HazardImage_Input = hazardImage_Input;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    @Override
    public void register(Observer o) { // this event should occur if the user upvotes an event.
        // TODO DATABASE: get the list of users subscribe to this incident and assign to observers.
        // eg: observer = getObservers(this) - get the list of users who subscribe to this incident.
        // TODO obtain observer user id.
        observers.add(o);
        // TODO DATABASE: update real time firebase.
    }

    @Override // this event should occur when the user removes their upvote from this incident.
    public void unregister(Observer o) {
        // eg: observer = getObservers(this) - get the list of users who subscribe to this incident.
        observers.remove(o);
        // TODO DATABASE: update real time firebase.
    }

    @Override
    public void notifyObserver(String message) {
        // loop through all observers and update them.
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        for (Observer user: observers){
            user.update(date, this.getHazardName_Input(), message); // this update method should send the message to the database
            // the next time the user logs into their account, they will have receive an update about the incident.
        }
    }
}

