package com.example.aiya_test_3.login;

import androidx.annotation.NonNull;

import com.example.aiya_test_3.login.Activities.Activity_SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserInput {
    /*DESCRIPTION
    * This class takes in inputs from the login page and checks if the user entered an acceptable value.
    * It does so by using a static method which can be accessed by every class.
    *
    * This class also helps to validate the UserInput by cross checking the input Strings with the database.
    * The user login is successful if it matches the database.
    *
    * */


    // STATIC METHODS //

    public static boolean verify(String email, String password){
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

        if(!email.contains("@") || password.length()==0){return false;} // check if input email have @ symbol
        return true; // valid email and password
    }


    public static boolean passwordequals(String password, String confirm_password){
        //Check if passwords inputs are of the same value//
        if (password.equals(confirm_password)){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean uniqueemail(String email){
        // Check if account with email already exists//

        // TODO Check database for account with same linked email
        return true;

    }



}
