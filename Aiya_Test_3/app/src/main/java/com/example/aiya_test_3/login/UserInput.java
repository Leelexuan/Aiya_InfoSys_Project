package com.example.aiya_test_3.login;

public abstract class UserInput {
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

    public abstract boolean validate();
        /*DESCRIPTION
         * This method checks if the user inputs matches the database values.
         *
         * INPUTS:
         * String email from EditText widget email
         * String password from EditText widget password
         *
         * OUTPUT:
         * boolean true: matches with the database
         * boolean false: does not match with database - failed login
         * */


    // FOR LOGIN PAGE //

    public static class UserLogin extends UserInput{
        // this class is to verify and validate during the UserLogin activity.

        private String email;
        private String password;

        @Override
        public boolean verify() {
            if(!email.contains("@") || password.length()==0){return false;} // check if input email have @ symbol, check if password is an empty string
            return true; // valid email and password
        }

        @Override
        public boolean validate() {
            return false;
        }

        // constructor //
        public UserLogin(String email, String password){
            this.email = email;
            this.password = password;
        }
    }

// FOR SIGN UP PAGE //

    public class UserSignup extends UserInput{
        // ATTRIBUTES //
        private String email;
        private String password;
        private String confirmPassword;

        @Override
        public boolean verify() {
            // verify email //
            if(!email.contains("@")){return false;} // check if input email have @ symbol

            // verify password //
            if(password.length() <= 5 || !password.matches(".*1234567890.*") || !password.matches(".*abcdefghijklmnopqrstuvwxyz.*"))return false;
            // must be >5 characters with both numbers and alphabets

            if(!password.matches(confirmPassword))return false; // check if password matches confirm password.

            return true; // valid email, password and confirm password
        }

        @Override
        public boolean validate() {
            return false;
        }

        // constructor //
        UserSignup(String email, String password, String confirmPassword){
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }
    }
}


