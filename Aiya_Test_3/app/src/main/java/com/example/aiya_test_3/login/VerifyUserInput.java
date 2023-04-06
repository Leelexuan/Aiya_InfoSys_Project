package com.example.aiya_test_3.login;

public class VerifyUserInput {
    /*DESCRIPTION
    * This class takes in inputs from the login page and checks if the user entered a legal value.
    * It does so by using a static method which can be accessed by every class.
    *
    * INPUTS to constructor:
    * String email - this string must be from the EditText widget with id email from activity_login.xml
    * String password - this string must be from the EditText widget with id password from activity_login.xml
    * */

    // ATTRIBUTES //
    private String email;
    private String password;

    // CONSTRUCTOR //
    VerifyUserInput(String email, String password){
        this.email = email;
        this.password = password;
    }

    // STATIC METHOD //
    public static boolean verify(){
        // check email //
        // must have @ symbol //


        // check password //
        // must include at least 6 characters and have both numbers and password //

        return true;
    }
}
