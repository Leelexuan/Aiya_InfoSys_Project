package com.example.aiya_test_3.login;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class SignUpUserInput extends UserInputAbstract{
    @Override
    public boolean accountInputCheck(String email, String password1, String password2, Context context) {
        // Check if email is valid
        if (!this.checkValidEmail(email)){
            Log.d("SIGN UP", "Invalid email attempt");
            Toast.makeText(context,"Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is valid
        if (!this.checkValidPassword(password1)){
            Log.d("SIGN UP", "Invalid password attempt");
            Toast.makeText(context,"Please enter valid password of more than 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password and confirm password inputs match
        if (!this.checkSamePassword(password1, password2)){
            Log.d("SIGN UP", "Passwords do not match");
            Toast.makeText(context,"Passwords do not match, please check", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if account with email already exists in database
        // This is done by Firebase already, we put this method here in the event that we have our own database that stores user information
        // Proof of concept
        if (!this.checkUniqueEmail(email)){
            Log.d("SIGN UP", "Email used already tagged to registered account");
            Toast.makeText(context,"Account with email already exists, try logging in instead", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If all checks pass, return true
        return true;
    }


    // This method is declared in SignUpUserInput class and will be used to check if account to be created will be an Official account or not.
    // In this case, we imagine that such an account with be an NEA account
    // Proof of concept
    public boolean checkOfficialEmail(String email){
        if (email.contains("@nea.gov.sg")){
            return true;
        }
        return false;

    }

    // This is not used in Sign Up, so we will return null
    @Override
    public String IncidentUserInputCheck(String HazardName, String HazardAddress, LatLng HazardAddress_LatLng, String HazardDescription_Input, String HazardType_Input, String HazardImage_Input) {
        return null;
    }
}
