package com.example.aiya_test_3.login.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.Activities.Activity_Incidents;
import com.example.aiya_test_3.login.UserInput;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_SignUp extends AppCompatActivity {
    // Declaring necessary widgets//
    private EditText username, password, confirm_password;

    private Button sign_up;


    //Declare necessary class//
    private UserInput signupuserInput = new UserInput(); // class to perform checks on user input

    //Firebase authentication//
    private FirebaseAuth auth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set main layout //
        setContentView(R.layout.activity_signup);

        // reference widgets in layout//
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        sign_up = findViewById(R.id.signup);

        // reference Firebase auth//
        auth = FirebaseAuth.getInstance();


        // when user clicks on sign up button//
        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Log attempt in logCat //
                Log.d("Signup Button", "User attempted to sign up");

                // Get text values from EditText widgets //
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String confirm_passwordText = confirm_password.getText().toString();

                // Verify inputs
                boolean verified = signupuserInput.verify(usernameText, confirm_passwordText, Activity_SignUp.this);
                // Check if passwords are equal
                boolean passwordequals = signupuserInput.passwordequals(passwordText, confirm_passwordText);
                // Check if no account with same email already created (assuming we have our own local database)
                boolean checkemail = signupuserInput.uniqueemail(usernameText);

                // check acceptable inputs
                if (verified){
                    Log.d("USER SIGNUP", "email and password verified");
                    // check passwords
                    if (passwordequals){
                        Log.d("USER SIGNUP","sign up passwords are equal");
                        // check email in database
                        if (checkemail){
                            Log.d("USER SIGNUP", "verified unique email for sign up");
                            usersignup(usernameText, confirm_passwordText);
                        }
                    } else{ // password and confirmed_password typed are not equal
                        Log.d("USER SIGNUP","sign up passwords do not match");
                        Toast.makeText(Activity_SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else { // email or password is not acceptable.
                    Log.d("USER SIGNUP","email and password fail verification");
                    Toast.makeText(Activity_SignUp.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to sign up user account to Firebase
    private void usersignup(String email, String password) {

        //create user account in Firebase

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Activity_SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Activity_SignUp.this, "User sign up successful", Toast.LENGTH_SHORT).show();
                    Log.d("USER SIGNUP", "Sign up successful");
                    startActivity(new Intent(Activity_SignUp.this, Activity_Incidents.class));
                    finish();
                }
                else {
                    Toast.makeText(Activity_SignUp.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                    Log.d("USER SIGNUP", "Sign up failed");
                }
            }
        });
    }

}


