package com.example.aiya_test_3.incidents;

import com.example.aiya_test_3.login.UserInput;
import com.google.android.gms.maps.model.LatLng;

public class CheckIncidentUserInputs extends UserInput {

    String HazardName;
    String HazardAddress;
    LatLng HazardAddress_LatLng;
    String HazardDescription_Input;
    String HazardType_Input;
    String HazardImage_Input;

    public CheckIncidentUserInputs(String HazardName, String HazardAddress, LatLng HazardAddress_LatLng, String HazardDescription_Input, String HazardType_Input, String HazardImage_Input) {
        this.HazardName = HazardName;
        this.HazardAddress = HazardAddress;
        this.HazardAddress_LatLng = HazardAddress_LatLng;
        this.HazardDescription_Input = HazardDescription_Input;
        this.HazardType_Input = HazardType_Input;
        this.HazardImage_Input = HazardImage_Input;
    }

    @Override
    public String CheckAllUserInputs(){
        if (CheckHazardNameInput()) {
            return "HazardName";
        }
        if (CheckHazardAddress()) {
            return "HazardAddress";
        }
        if (CheckHazardAddress_LatLng()) {
            return "HazardAddress_LatLng";
        }
        if (CheckHazardDescription_Input()) {
            return "HazardDescription_Input";
        }
        if (CheckHazardType_Input()) {
            return "HazardType_Input";
        }
        if (CheckHazardImage_Input()) {
            return "HazardImage_Input";
        }

        return ""; // Empty string indicates all inputs are valid
    }

    public boolean CheckHazardNameInput(){
        return HazardName.equals("");
    }
    public boolean CheckHazardAddress(){
        return HazardAddress.equals("");
    }
    public boolean CheckHazardAddress_LatLng(){
        return HazardAddress_LatLng == null;
    }
    public boolean CheckHazardDescription_Input(){
        return HazardDescription_Input.equals("");
    }
    public boolean CheckHazardType_Input(){
        return HazardType_Input.equals("");
    }
    public boolean CheckHazardImage_Input(){
        return HazardImage_Input.equals("");
    }
}
