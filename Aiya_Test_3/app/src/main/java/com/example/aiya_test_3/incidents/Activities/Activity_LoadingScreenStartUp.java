package com.example.aiya_test_3.incidents.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.firebaseCardSource;
import com.example.aiya_test_3.login.Activities.Activity_Login;

import java.io.Serializable;

public class Activity_LoadingScreenStartUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent go_to_login_page = new Intent(Activity_LoadingScreenStartUp.this, Activity_Login.class);
                startActivity(go_to_login_page);
            }
        }, 2000);   // Wait for 2 seconds (Once we connect to database, we can make it actually take amount of time required to upload the data
    }
}