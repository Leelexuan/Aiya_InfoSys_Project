package com.example.aiya_test_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {
    private ImageButton imageButton7;
    private SearchView searchView;
    private LayoutInflater inflater;
    private LinearLayout cardContainer,appbarContainer;
    private TextView textView;
    private View cardView, app_bar;
    private ConstraintLayout cardContent;

    private List<String> cardData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflater = LayoutInflater.from(this);

        app_bar = inflater.inflate(R.layout.app_bar, null);

        ImageButton imageButton = app_bar.findViewById(R.id.login_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the Supabutton");
            }
        });

        // Add the app_bar view to the MainActivity's layout
        appbarContainer = findViewById(R.id.appbar);
        appbarContainer.addView(app_bar, 0);

        // Make Card View Automatically
        cardContainer = findViewById(R.id.cardContainer);

        int tempTester = 0;
        int num_of_cards = 100;
        cardData = new ArrayList<>(num_of_cards);

        for (int i = 0; i < num_of_cards /*input some data here to tell it how many to create i guess*/ ; i++) {
            // Inflate the card_view.xml layout file
            cardView = inflater.inflate(R.layout.card_view, null);

            // Set any content or attributes that you want for the CardView
            cardContent = cardView.findViewById(R.id.card_content);
            textView = cardContent.findViewById(R.id.textView);
            cardData.add("Card " + i);
            textView.setText(cardData.get(i));

            ObjectAnimator animator = ObjectAnimator.ofInt(cardContent, "maxHeight", 100);
            animator.setDuration(200);
            if (tempTester == 0){
                    tempTester = cardView.getMeasuredHeight();
            }

            int vHeight = tempTester;

            View.OnClickListener dropdownCardListener = new View.OnClickListener() {
                boolean expanded = false;

                @Override
                public void onClick(View v) {

                    if (expanded == false) {
                        animator.setIntValues(vHeight, vHeight+500);
                        expanded = true;
                    } else {
                        animator.setIntValues(vHeight+500, 100);
                        expanded = false;
                    }
                    animator.start();
                }

            };
            // Perform initial expansion
            animator.setIntValues(vHeight, 100);
            animator.start();

            // Add the CardView to the container ViewGroup
            cardContainer.addView(cardView);

            cardView.setOnClickListener(dropdownCardListener);
        }

        searchView = app_bar.findViewById(R.id.SearchBar);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<String> filteredData = new ArrayList<>();
        for (String data : cardData) {
            if (data.toLowerCase().contains(newText.toLowerCase())) {
                filteredData.add(data);
            }
        }

        // Update the CardViews based on the filtered data
        updateCardViews(filteredData);

        return true;
    }

    private void updateCardViews(List<String> data) {
        // Remove all existing CardViews from the container
        cardContainer.removeAllViews();

        // Create new CardViews based on the filtered data
        // Make Card View Automatically

        inflater = LayoutInflater.from(this);
        cardContainer = findViewById(R.id.cardContainer);

        int tempTester = 0;
        int num_of_cards = data.size();

        for (int i = 0; i < num_of_cards /*input some data here to tell it how many to create i guess*/ ; i++) {
            // Inflate the card_view.xml layout file
            cardView = inflater.inflate(R.layout.card_view, null);

            // Set any content or attributes that you want for the CardView
            cardContent = cardView.findViewById(R.id.card_content);
            textView = cardContent.findViewById(R.id.textView);
            cardData.add("Card " + i);
            textView.setText(cardData.get(i));

            ObjectAnimator animator = ObjectAnimator.ofInt(cardContent, "maxHeight", 100);
            animator.setDuration(200);
            if (tempTester == 0){
                tempTester = cardView.getMeasuredHeight();
            }

            int vHeight = tempTester;

            View.OnClickListener dropdownCardListener = new View.OnClickListener() {
                boolean expanded = false;

                @Override
                public void onClick(View v) {

                    if (expanded == false) {
                        animator.setIntValues(vHeight, vHeight+500);
                        expanded = true;
                    } else {
                        animator.setIntValues(vHeight+500, 100);
                        expanded = false;
                    }
                    animator.start();
                }

            };
            // Perform initial expansion
            animator.setIntValues(vHeight, 100);
            animator.start();

            // Add the CardView to the container ViewGroup
            cardContainer.addView(cardView);

            cardView.setOnClickListener(dropdownCardListener);
        }
    }
    }