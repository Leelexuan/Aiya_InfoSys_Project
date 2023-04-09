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



    }
}
