package com.example.aiya_test_3.login;


import androidx.annotation.NonNull;

import com.example.aiya_test_3.login.Activities.Activity_SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserInput {

    /*DESCRIPTION
    * This abstract class implements the template design to verify and validate user inputs. This
    * abstract class also utilizes static inner classes to delegate work.
    *
    * verify: ensure user inputs are acceptable.
    *
    * validate: ensure user inputs corresponds to the database (mainly used for sign ups).
    * */

    final boolean prepareInput(UserInput userInput){
        if(userInput.verify()) { // check acceptable input
            return validate(); // check if input matches database.
        }
        return false;
    }

    public abstract boolean verify();
        /*DESCRIPTION
         * This method checks if the user inputs are acceptable.
         *
         * INPUTS:
         * String email from EditText widget email
         * String password from EditText widget password
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


