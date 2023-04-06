package com.example.aiya_test_3.incidents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.aiya_test_3.R;

public class Activity_LoadingScreenStartUp extends AppCompatActivity {

    // Todo: Implement Map  (Lesson 3) [Darren and Michaels]
    // Todo Design Pattern: Implement superclass for incident (observable and visitable) with subclass being the hazard type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent go_to_forum_page = new Intent(Activity_LoadingScreenStartUp.this, Activity_Incidents.class);
                startActivity(go_to_forum_page);
            }
        }, 2000);   // Wait for 2 seconds (Once we connect to database, we can make it actually take amount of time required to upload the data
    }
}