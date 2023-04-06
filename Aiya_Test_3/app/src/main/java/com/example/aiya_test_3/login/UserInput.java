package com.example.aiya_test_3.login;

public class UserInput {
    /*DESCRIPTION
    * This class takes in inputs from the login page and checks if the user entered a legal value.
    * It does so by using a static method which can be accessed by every class.
    *
    * INPUTS to constructor:
    * String email - this string must be from the EditText widget with id email from activity_login.xml
    * String password - this string must be from the EditText widget with id password from activity_login.xml
    * */


    // STATIC METHOD //
    public static boolean verify(String email, String password){
        // check email //
        // must have @ symbol //
        if(!email.contains("@")){return false;}

        // check password //
        if(!(password.length()<6) && !(password.matches(".*1234567890.*")) && !(password.matches(".*abcdefghijklmnopqrstuvwxyz.*"))){return false;}
        // must include at least 6 characters and have both numbers and password //

        return true; // valid email and password
    }

    public static boolean validate(String email, String password){
        // TODO DATABASE: get email and password from database
        return true;
    }
}
