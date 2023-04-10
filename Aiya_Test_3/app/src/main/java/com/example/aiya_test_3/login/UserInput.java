package com.example.aiya_test_3.login;

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
         * String email from EditText widget
         * String password from EditText widget

         *
         * OUTPUT:
         * boolean true: acceptable input
         * boolean false: unacceptable input (not an email address)
         * */

        if(!email.contains("@") || password.length()<=5){return false;} // check if input email have @ symbol and password length > 5.
        return true; // valid email and password
    }


    public static class UserLogin extends UserInput{
        // this class is to verify and validate during the UserLogin activity.

    public static boolean passwordequals(String password, String confirm_password){
        /*DESCRIPTION
         * This method checks if the user inputs are acceptable.
         *
         * INPUTS:
         * String password from EditText widget
         * String confirm_password from EditText widget
         *
         * OUTPUT:
         * boolean true: password and confirm_password matches
         * boolean false: password and confirm_password does not match.
         * */
        return (password.equals(confirm_password));
    }

    public static boolean uniqueemail(String email){
        /*DESCRIPTION
         * This method checks if the input email can be found in the database.
         *
         * INPUTS:
         * String email from EditText widget
         *
         * OUTPUT:
         * boolean true: email is not in the database
         * boolean false: email is found in the database.
         * */

        // NOTE //
        // Currently, firebase checks for a unique email automatically. However, we left this
        // method in to account that the company may not be using firebase. Hence, we may still need
        // to implement this method based on the selected database.

        return true;

    }
}


