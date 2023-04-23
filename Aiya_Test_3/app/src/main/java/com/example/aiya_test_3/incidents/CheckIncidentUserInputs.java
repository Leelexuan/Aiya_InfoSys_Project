package com.example.aiya_test_3.incidents;

import android.content.Context;

import com.example.aiya_test_3.login.UserInputAbstract;
import com.google.android.gms.maps.model.LatLng;

public class CheckIncidentUserInputs extends UserInputAbstract {

    @Override
    public String IncidentUserInputCheck(String HazardName, String HazardAddress, LatLng HazardAddress_LatLng, String HazardDescription_Input, String HazardType_Input, String HazardImage_Input) {
        if (this.CheckHazardNameInput(HazardName)) {
            return "HazardName";
        }
        if (this.CheckHazardAddress(HazardAddress)) {
            return "HazardAddress";
        }
        if (this.CheckHazardAddress_LatLng(HazardAddress_LatLng)) {
            return "HazardAddress_LatLng";
        }
        if (this.CheckHazardDescription_Input(HazardDescription_Input)) {
            return "HazardDescription_Input";
        }
        if (this.CheckHazardType_Input(HazardType_Input)) {
            return "HazardType_Input";
        }
        if (this.CheckHazardImage_Input(HazardImage_Input)) {
            return "HazardImage_Input";
        }
        return ""; // Empty string indicates all inputs are valid
    }

    @Override
    public boolean accountInputCheck(String email, String password1, String password2, Context context) {
        return false;
    }

}
