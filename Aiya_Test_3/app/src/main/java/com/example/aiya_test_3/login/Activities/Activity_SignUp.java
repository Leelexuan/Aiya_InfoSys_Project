package com.example.aiya_test_3.login.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aiya_test_3.R;
import com.example.aiya_test_3.login.UserInput;

public class Activity_SignUp extends AppCompatActivity {
    // Declaring necessary widgets //
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private Button signup;

    // Declaring necessary classes //
    private UserInput userInput; // instantiate UserInput class for verification and validation

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set main layout //
        setContentView(R.layout.activity_signup);


                        // Check if no account with same email already created
                        boolean checkemail = signupuserInput.uniqueemail(usernameText);

                        if (checkemail){ // entered email is not in database
                            Log.d("USER SIGNUP", "verified unique email for sign up");
                            usersignup(usernameText, confirm_passwordText); // sign up users.
                        }
                    }
                    else{
                        Log.d("USER SIGNUP","username and password fail verification");
                        Toast.makeText(Activity_SignUp.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d("USER SIGNUP","sign up passwords do not match");
                    Toast.makeText(Activity_SignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
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
