package com.example.aiya_test_3.login;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class LogInUserInput extends UserInputAbstract{


    @Override
    public boolean accountInputCheck(String email, String password1, String password2, Context context) {
        // Check if email is valid
        if (!this.checkValidEmail(email)){
            Log.d("LOG IN", "Invalid email attempt");
            Toast.makeText(context,"Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if password is valid
        if (!this.checkValidPassword(password1)){
            Log.d("LOG IN", "Invalid password attempt");
            Toast.makeText(context,"Please enter valid password", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If checks pass, return true
        return true;
    }


    // This is not used in Log In, so we will return null
    @Override
    public String CheckAllUserInputs() {
        return null;
    }
}
