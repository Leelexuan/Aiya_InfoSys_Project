package com.example.aiya_test_3.login.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.Activities.Activity_Incidents;
import com.example.aiya_test_3.incidents.Activities.Activity_LoadingScreenStartUp;
import com.example.aiya_test_3.login.UserInput;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Login extends AppCompatActivity {
    // Declaring necessary widgets //
    private EditText password, email;
    private Button loginButton;

    private TextView signuplink;

    // Declare necessary classes //
    private UserInput userInput = new UserInput(); // this class is used to verify and validate inputs.

    // Connect to Firebase
    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set main layout //
        setContentView(R.layout.activity_login);

        // reference widgets in the layout //
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signuplink = findViewById(R.id.no_account);

        auth = FirebaseAuth.getInstance();

        signuplink.setOnClickListener(new View.OnClickListener() {
            // switches to sign up activity when user clicks on text//
            @Override
            public void onClick(View view) {
                //Log switch from log in activity to sign up activity//
                Log.d("Sign up switch", "User redirected to sign up");
                startActivity(new Intent(Activity_Login.this, Activity_SignUp.class));
                finish();
            }
        });

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
                    // VALIDATION: Cross check user input with database, if details match log user in//

                    loginUser(emailText, passwordText);

                } else { // NOT VERIFIED //
                    // log unacceptable input //
                    Log.d("USER LOGIN", "User input an unacceptable value."); // log unverified attemp
                    // display Toast message //
                    Toast.makeText(Activity_Login.this, "Walao, put a real email leh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String password){
        Log.d("USER LOGIN", "Button Pressed");
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("USER LOGIN", "Login Successful. Proceeding to Activity_Incident.");
                Toast.makeText(Activity_Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_Login.this, Activity_LoadingScreenStartUp.class));
                finish();
            }
        });
    }
}