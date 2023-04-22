package com.example.aiya_test_3.login;

import android.content.Context;
public abstract class UserInputAbstract {


    //This abstract method will be implemented depending if the purpose of the input check is for login, signup, or future purposes.
    public abstract boolean accountInputCheck(String email, String password1, String password2, Context context);


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


    abstract public String CheckAllUserInputs();

}
