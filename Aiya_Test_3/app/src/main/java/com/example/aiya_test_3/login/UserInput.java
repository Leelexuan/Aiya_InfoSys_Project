package com.example.aiya_test_3.login;

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

    public static boolean validate(String email, String password){
        /*DESCRIPTION
         * This method checks if the user inputs matches the database values.
         *
         * INPUTS:
         * String email from EditText widget email in the activity_login.xml
         * String password from EditText widget password in the activity_login.xml
         *
         * OUTPUT:
         * boolean true: matches with the database
         * boolean false: does not match with database - failed login
         * */


        // TODO DATABASE: get email and password from database
        return true;
    }
}
