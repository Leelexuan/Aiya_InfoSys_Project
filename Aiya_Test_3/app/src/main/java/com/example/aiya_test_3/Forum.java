package com.example.aiya_test_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// For Firebase
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Forum extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    private SearchView searchView;    // Component for search bar
    private LayoutInflater inflater; // To instantiate a layout (use this to have more than 1 layout for every activity)
    private LinearLayout cardContainer,appbarContainer; // Linear layout means it is either horizontal or vertical, holds the respective name item
    private TextView cardTextView; // For viewing text
    private View cardView, app_bar;
    private ConstraintLayout cardContent;
    private List<String> cardData;

    // Firebase
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // First thing that runs when you open activity
        super.onCreate(savedInstanceState);

        // Todo Design Pattern: Rewrite entire forum code using recycler view (low priority)(Lesson 4)
        // main layout for this page
        setContentView(R.layout.activity_forum); // activity_forum layout does not have content, only containers

        // Bring in contents to fill up the containers, we do this by using inflater
        // Inflater is to bring another layout into this layout
        inflater = LayoutInflater.from(this);
        app_bar = inflater.inflate(R.layout.app_bar_forum, null); // Here we bring in app_bar_forum

        // Within appbar, there is an image button, we want to address it so we instantiate it
        ImageButton imageButton = app_bar.findViewById(R.id.input_detail_button);
        imageButton.setOnClickListener(new View.OnClickListener() { // This tells the to start listening out for clicks
            public void onClick(View v) { // This tells the button what to do when clicked

                // When we click the button, we want it to open the open the detail page
                // We use intent to do that
                Intent openDetail_input = new Intent(Forum.this,Detail_Input.class);
                startActivity(openDetail_input);
                Log.d("Input Details Button", "User click to input details");
            }
        });

        // Once everything is instantiated, add the app_bar layout to the container meant for app bar to the MainActivity's layout
        appbarContainer = findViewById(R.id.appbar);
        appbarContainer.addView(app_bar, 0);

        // We want to generate in the cards for the forum pages automatically
        cardContainer = findViewById(R.id.cardContainer);

        int tempCardHeightVariable = 0;
        int num_of_cards = 100; // Here, we hardcode the number of cards
        cardData = new ArrayList<>(num_of_cards);

        for (int i = 0; i < num_of_cards /*input some data here to tell it how many to create i guess*/ ; i++) {

            // Inflate the card_view.xml layout
            cardView = inflater.inflate(R.layout.card_view, null);

            //Todo Database: Retrieve data from database and input into cards (Lesson 5)

            // Set any component or attributes that you want for the card in cardContainer
            cardContent = cardView.findViewById(R.id.card_content);
            cardTextView = cardContent.findViewById(R.id.hazardDescription);
            cardData.add(i + ".Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus eget arcu dapibus,\n" +
                    "vulputate felis sed, malesuada dolor. Nunc et massa viverra, tincidunt tellus ac, porttitor sapien.\n" +
                    "Phasellus et risus sagittis, aliquam magna vel, vestibulum libero. Sed quis nibh ipsum. Quisque tincidunt\n" +
                    "tristique quam, sed dapibus urna condimentum nec. Morbi fringilla velit at nisi vestibulum, at bibendum odio\n" +
                    "ultrices. Ut a lorem in metus maximus porta. Quisque non suscipit lacus. Nam non sapien convallis, tincidunt\n" +
                    "neque eget, fermentum magna. Donec nec diam finibus, accumsan leo at, interdum elit. Donec sed iaculis nibh, vel condimentum mi.\"\n");
            cardTextView.setText(cardData.get(i)); // Add text card data into the card Textview

            ObjectAnimator animator = ObjectAnimator.ofInt(cardContent, "maxHeight", 100);
            animator.setDuration(200);

            if (tempCardHeightVariable == 0){
                    tempCardHeightVariable = cardView.getMeasuredHeight(); // Get initial height of the card and store it in a "getter" variable
            }

            int vHeight = tempCardHeightVariable; // instantiate card height

            View.OnClickListener dropdownCardListener = new View.OnClickListener() { // This onClickListener is for the drop down mechanism
                boolean expanded = false; // a flag to check if it is expanded

                @Override
                public void onClick(View v) { // This is the logic for the drop down

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

            // This here is to start the card minimized
            animator.setIntValues(vHeight, 100);
            animator.start();

            // Add the CardView to the container ViewGroup
            cardContainer.addView(cardView);

            // Set the card view with the above drop down listener
            cardView.setOnClickListener(dropdownCardListener);
        }

        // This is to instantiate search bar
        searchView = app_bar.findViewById(R.id.SearchBar);
        searchView.setOnQueryTextListener(this);

    }

    // Everything beyond this line is for search bar functionality
    // Todo Design Pattern: Ryan say he will use multi-threading to make this faster (Lesson 3)
    // Todo Database: Figure out how to search database and refresh forum page (Lesson 5)

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
            cardTextView = cardContent.findViewById(R.id.hazardDescription);
            cardData.add("Card " + i);
            cardTextView.setText(cardData.get(i));

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