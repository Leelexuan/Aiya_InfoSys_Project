package com.example.aiya_test_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Submitted_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_details);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent go_back_forum_page = new Intent(Submitted_Details.this, Forum.class);
                startActivity(go_back_forum_page);
            }
        }, 2000);   // Wait for 2 seconds (Once we connect to database, we can make it actually take amount of time required to upload the data
    }
}