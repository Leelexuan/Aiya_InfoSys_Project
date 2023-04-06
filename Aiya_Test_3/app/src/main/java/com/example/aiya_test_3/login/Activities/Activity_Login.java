package com.example.aiya_test_3.login.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.Activities.Activity_Incidents;
import com.example.aiya_test_3.login.UserInput;

public class Activity_Login extends AppCompatActivity {
    // Declaring necessary widgets //
    private EditText password, email;
    private Button loginButton;

    // Declare necessary classes //
    private UserInput userInput = new UserInput(); // this class is used to verify and validate inputs.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set main layout //
        setContentView(R.layout.activity_login);

        // reference widgets in the layout //
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // when the user clicks the login button //
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log the attempt to logCat
                Log.d("Login Button", "User attempted to login");

                // Get text values from the EditText widgets
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                // DELEGATION STRATEGY //
                // VERIFY USER INPUT -> IF VERIFIED -> VALIDATE WITH DATABASE //

                // VERIFICATION: Check if user input is acceptable
                boolean verified = userInput.verify(emailText, passwordText); // verified = true if is legal, false if illegal.

                if(verified){
                    // VALIDATION: Cross check user input with database //
                    boolean validated = userInput.validate(emailText, passwordText);

                    if(validated) { //
                        Log.d("USER LOGIN", "Login Successful. Proceeding to Activity_Incident."); // log successful login

                        // explicit intent to go back to Activity_Incident //
                        Intent intent = new Intent(Activity_Login.this, Activity_Incidents.class);
                        Toast.makeText(Activity_Login.this, "Successful Login", Toast.LENGTH_SHORT).show(); // notify user successful login //
                        startActivity(intent);
                    } else {
                        // Failed Login Attempt //
                        Log.d("USER LOGIN", "User is not validated - failed login"); // log failed attempt to login
                        Toast.makeText(Activity_Login.this, "Your email and password wrong sia, please sign up if you have not!", Toast.LENGTH_SHORT).show();
                    }

                } else { // NOT VERIFIED //
                    // log unacceptable input //
                    Log.d("USER LOGIN", "User input an unacceptable value."); // log unverified attemp
                    // display Toast message //
                    Toast.makeText(Activity_Login.this, "Walao, put a real email leh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}