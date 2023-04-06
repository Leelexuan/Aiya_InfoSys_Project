package com.example.aiya_test_3.login.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiya_test_3.R;

public class Activity_Login extends AppCompatActivity {
    // Declaring necessary widgets //
    private EditText password, email;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set main layout //
        setContentView(R.layout.activity_login);

        // reference widgets in the layout //
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // when the user clicks the button //
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log the attempt to logCat
                Log.d("Login Button", "User attempted to login");

                // Using Delegation Strategy for clarity //

                // check if the user input a valid email and password - Verification
                // TODO - implement a class to check for valid email

                // check if the user's email and password corresponds to the database - Validation
                // TODO - implement a class to obtain data from database and cross check with the user input.

                // Validated User - Proceed back to Incident Activity
                // TODO - explicit intent to go to incident activity

                // Not Validated User (no account found in database) - Prompt sign up.
                // TODO - a Toast message to prompt user to sign up
            }
        });
    }
}
