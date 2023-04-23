package com.example.aiya_test_3.login;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/* DESCRIPTION
This abstract class is used as a template to for the template design pattern.
LogInUserInput and SignUpUserInput class will extend this abstract class with their own specific implementation of abstract methods.
This is done as user input has to be checked at different areas but what they check for may differ.
Therefore we use a template design that can be reused in the future if we add more features that require checking user input.

This is also a form of delegation as the tasks of checking user input is delegated to objects who extend
this abstract class rather than doing the check in the activity itself.
 */
public abstract class UserInputAbstract {

    //This abstract method will be implemented depending if the purpose of the input check is for login, signup, or future purposes.
    public abstract boolean accountInputCheck(String email, String password1, String password2, Context context);

    public abstract String IncidentUserInputCheck(String HazardName, String HazardAddress, LatLng HazardAddress_LatLng, String HazardDescription_Input, String HazardType_Input, String HazardImage_Input);

    // Check if password is valid.
    protected boolean checkValidPassword(String password){
        if (password.length() < 6){
            return false;
        }
        return true;
    }

    // Check if email is valid.
    protected boolean checkValidEmail(String email){
        if (email.contains("@")){
            return true;
        }
        return false;
    }

    // Check if password and confirm password is the same (mainly used in sign up).
    protected boolean checkSamePassword(String password1, String password2){
        return password1.equals(password2);
    }

    //Check if email is a unique email, not used by a currently registered user.
    protected boolean checkUniqueEmail(String email){
        //Firebase already implements this. This method will be used in the event that we have a separate database to store user information.
        //For now, this is used as proof of concept.
        return true;
    }

    protected boolean CheckHazardNameInput(String HazardName){
        return HazardName.equals("");
    }
    protected boolean CheckHazardAddress(String HazardAddress){
        return HazardAddress.equals("");
    }
    protected boolean CheckHazardAddress_LatLng(LatLng HazardAddress_LatLng){
        return HazardAddress_LatLng == null;
    }
    protected boolean CheckHazardDescription_Input(String HazardDescription_Input){
        return HazardDescription_Input.equals("");
    }
    protected boolean CheckHazardType_Input(String HazardType_Input){
        return HazardType_Input.equals("");
    }
    protected boolean CheckHazardImage_Input(String HazardImage_Input){
        return HazardImage_Input.equals("");
    }

}
