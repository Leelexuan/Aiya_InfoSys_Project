package com.example.aiya_test_3.login.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aiya_test_3.R;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Logout extends AppCompatActivity {

    private Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set main layout //
        setContentView(R.layout.activity_logout);

        logout = findViewById(R.id.button);

        //Log user out and return to Login Page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Log.d("USER LOGOUT", "user has been logged out");
                Toast.makeText(Activity_Logout.this, "You have been logged out", Toast.LENGTH_SHORT).show();
                // Switch activity to login page
                startActivity(new Intent(Activity_Logout.this, Activity_Login.class));
                finish();
            }
        });
    }
}
