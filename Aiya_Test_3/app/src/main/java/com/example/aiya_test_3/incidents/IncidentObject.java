package com.example.aiya_test_3.incidents;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class IncidentObject {
    private String HazardID;
    private  String HazardName_Input;
    private  String HazardAddress_Input;
    private  LatLng HazardAddress_LatLng;
    private  Double HazardAddress_Lat;
    private  Double HazardAddress_Long;
    private  String HazardDescription_Input;
    private  String HazardType_Input;
    private  String HazardImage_Input;
    private  long upvotes;
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
        setUpvotes(0);
    }

    // This method saves all the data into database
    public void saveIncidentToDatabase() {
        nNodeRefInputDetails = nRootDatabaseRef.child(hazardDetailsNode);
        HashMap<String, IncidentObject> Send_database_details = new HashMap<>();

        Send_database_details.put("Incident", new IncidentObject(HazardName_Input, HazardAddress_Input, HazardAddress_Lat, HazardAddress_Long, HazardDescription_Input, HazardType_Input,  HazardImage_Input));
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

    public void setHazardImage_Input(String hazardImage_Input) {
        HazardImage_Input = hazardImage_Input;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

}

