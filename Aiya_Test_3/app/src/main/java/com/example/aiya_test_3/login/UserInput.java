package com.example.aiya_test_3.login;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public abstract class UserInput {
    /*DESCRIPTION
    * This class takes in inputs from the login page and checks if the user entered an acceptable value.
    * It does so by using a static method which can be accessed by every class.
    *
    * This class also helps to validate the UserInput by cross checking the input Strings with the database.
    * The user login is successful if it matches the database.
    *
    * */

    // STATIC METHODS //

    public static boolean verify(String email, String password, Context context){

        /*DESCRIPTION
         * This method checks if the user inputs are acceptable.
         *
         * INPUTS:
         * String email from EditText widget email in the activity_login.xml
         * String password from EditText widget password in the activity_login.xml
         *
         * OUTPUT:
         * boolean true: acceptable input
         * boolean false: unacceptable input (not an email address)
         * */

        // Check if email is valid, if not make toast to inform user
        if (!email.contains("@")){
            Log.d("USER INPUT", "invalid email");
            Toast.makeText(context, "Walao, put a real email leh", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is valid, if not make toast to inform user
        else if (password.length() < 6){
            Log.d("USER INPUT", "password too short");
            Toast.makeText(context, "Please enter a password of at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        // passed all test cases.
        return true;
    }

    public static boolean inputequals(String input1, String input2){
        /*DESCRIPTION
         * This method checks if the user password and confirm_password entered in the
         * sign up page matches.
         *
         * INPUTS:
         * String password from EditText widget sign up activity
         * String confirm_password from EditText widget sign up activity
         *
         * OUTPUT:
         * boolean true: password matches
         * boolean false: password does not match
         * */
        Log.d("USER INPUT", "checking password and confirm_password");
        return input1.equals(input2); // returns either true or false.
    }

    public static boolean uniqueemail(String email){
        // Check if account with email already exists//
        // Implementation will be explored if the app uses a local database//
        // Currently Firebase does the check on its end and does not require any implementation//
        return true;
    }

    abstract public String CheckAllUserInputs();
}

