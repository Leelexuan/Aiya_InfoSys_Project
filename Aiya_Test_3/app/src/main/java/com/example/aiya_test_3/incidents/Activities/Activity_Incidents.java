package com.example.aiya_test_3.incidents.Activities; // Don't forgot to change this if you shift files out

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.CardAdapter;
import com.example.aiya_test_3.incidents.firebaseCardSource;
import com.example.aiya_test_3.login.Activities.Activity_Logout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;


public class Activity_Incidents extends AppCompatActivity implements SearchView.OnQueryTextListener, OnMapReadyCallback, OnMapsSdkInitializedCallback {

    // Instantiation of attributes and views //
    private SearchView searchView;    // Component for search bar
    private LayoutInflater inflater; // To instantiate a layout (use this to have more than 1 layout for every activity)
    private LinearLayout cardContainer,appbarContainer; // Linear layout means it is either horizontal or vertical, holds the respective name item
    private TextView cardTextView; // textView
    private View cardView, app_bar,overlay;
    private ConstraintLayout cardContent;
    private List<String> cardData;

    private GoogleMap mMap;

    private firebaseCardSource cardDataSource;

    boolean initialDataReady = false;
    RecyclerView revisedCardContainer;

    // Firebase
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // First thing that runs when you open activity
        super.onCreate(savedInstanceState);

        /*DESCRIPTION
         * This is the activity class for the incident forum page.
         * On this page, at the top there is an app bar with the following features
         * - An ImageButton to Login
         * - An ImageButton to Add Incidents
         * - An ImageButton to Refresh Page (Not Implemented)
         * - A search bar that searches all the recycler view cards
         *
         * Right below the app bar is a custom map powered by google maps. The maps have the following feature
         * - Custom self-made hazard icons displayed on the map at the location of the hazard
         *
         * Finally, below that, we have the forum of hazards displayed using a recycler view with the following features
         * - Expanding and minimizing of card to show or hide hazards
         * - Card shows an image of the hazard pulled from a firebase storage
         * - Card shows the hazard title, location and details pulled from a firebase real time database
         * - Upvote button to attract attention (Not implemented)
         * */

        // Inform the maps to use Latest renderer
        // This is so that we can use the cloud-based map styling instead of local styling
        // This is better as local styling will require the user to "update" the app every time we want the map style to change
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);

        // Initialising Firebase Data
        // This allows us to get the firebase data as quickly as possible by making it the first thing to be loaded
        cardDataSource = new firebaseCardSource();

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
                Intent openDetail_input = new Intent(Activity_Incidents.this, Activity_Incident_Details_Input.class);
                startActivity(openDetail_input);
                Log.d("Input Details Button", "User click to input details");
            }
        });

        // Within app bar, there is a log out button, we want to address it so we instantiate it
        ImageButton logout_button = app_bar.findViewById(R.id.logout_button);

        logout_button.setOnClickListener(new View.OnClickListener() { //when button is clicked redirect to logout page
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Incidents.this, Activity_Logout.class));
                Log.d("Logout button", "User clicked logout redirect button");

            }
        });

        // Within app bar, there is a log in button (repurposed to refresh), we want to address it so we instantiate
        // TODO: Repurpose button to refresh

        // Once everything is instantiated, add the app_bar layout to the container meant for app bar to the MainActivity's layout
        appbarContainer = findViewById(R.id.appbar);
        appbarContainer.addView(app_bar, 0);

        // sets gmap
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // This is here to run on UI thread as we notice that there is an issue for google maps (not fatal)
        // Link to issue : https://issuetracker.google.com/issues/228091313
        // This is the apparent fix
        Activity_Incidents.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.getMapAsync(Activity_Incidents.this);
            }
        });

        // We want to generate in the cards for the forum pages automatically
        // We use recycler view to save on memory

        // Inflate the card_view.xml layout
        cardView = inflater.inflate(R.layout.card_view, null);
        revisedCardContainer = findViewById(R.id.revisedCardContainer);
        cardContent = cardView.findViewById(R.id.card_content);
        cardTextView = cardContent.findViewById(R.id.hazardDescription);

        adapter = new CardAdapter(this, cardDataSource); // Here we initialize the recycler view adapter with the card data
        revisedCardContainer.setAdapter( adapter ); // Then we set the adapter
        revisedCardContainer.setLayoutManager( new LinearLayoutManager(this));

        // Initialising the searchbar
        searchView = app_bar.findViewById(R.id.SearchBar);
        searchView.setOnQueryTextListener(this);
    }

    // The firebase obtain data via firebaseCardSource.onDataChange only AFTER the onCreate has been run.
    // It also takes a while to get the data.
    // So we need a auto-refresher to trigger a refresh data to fetch the data once it is ready.
    // This entire code block is for that
    // =======================================================================================
    private Handler handler = new Handler();
    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {

            // Call the refresh method here
            refreshData();

            if(cardDataSource.isInitialDataReadyFlag() == false){
            // Schedule the next refresh after a delay (in milliseconds)
                handler.postDelayed(this, 0);
            }
            else{

                // We want it to stop refreshing once we got the data
                cardDataSource.buildOriginalList();
                Log.d("Refresh","Stop Refresh");
                handler.removeCallbacks(refreshRunnable);
            }
        }
    };

    // Start auto-refresh when the Activity starts
    @Override
    protected void onStart() {
        super.onStart();

        // Call the refresh method here to initially load data
        refreshData();

        // Schedule the first refresh after a delay (in milliseconds)
        handler.postDelayed(refreshRunnable, 0);
    }

    // Stop auto-refresh when the Activity stops
    @Override
    protected void onStop() {
        super.onStop();
        // Remove any pending callbacks to stop the auto-refresh
        handler.removeCallbacks(refreshRunnable);
    }

    // Update data in the adapter
    public void refreshData() {
        // Notify adapter that data has changed
        adapter.notifyDataSetChanged();

        // Add the map markers
        addNewMarkers();
    }
    // =======================================================================================

    // Search Bar Method
    // How it works is that we remove the items that are not in the search query and then reload
    // the adapter and refresh to have the updated list
    // =======================================================================================
    @Override
    public boolean onQueryTextSubmit(String query) {
        cardDataSource.rebuild_list(query); // Rebuild and filter the list based on the
        adapter.getItemCount();
        revisedCardContainer.setAdapter(null);
        revisedCardContainer.setLayoutManager(null);
        revisedCardContainer.setAdapter(adapter);
        revisedCardContainer.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
        return true;
    }
    @Override
    public boolean onQueryTextChange(String query) {
        if (searchView.getQuery().length() == 0) {
            cardDataSource.rebuild_list(query);
            adapter.getItemCount();
            revisedCardContainer.setAdapter(null);
            revisedCardContainer.setLayoutManager(null);
            revisedCardContainer.setAdapter(adapter);
            revisedCardContainer.setLayoutManager(new LinearLayoutManager(this));
            adapter.notifyDataSetChanged();
        }
        return false;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add bitmap marker
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable(R.drawable.lamp_post);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        Log.d("Map Created", "Created");
    }

    @Override
    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    // Adding unique markers
    public void addNewMarkers(){

        for(int index = 0; index < cardDataSource.numberOfIncident(); index++){

            // Add bitmap marker
            String HazardType = cardDataSource.getHazardType(index);

            Log.d("Hazard Type: ",HazardType);
            BitmapDrawable Hazard_icon;

            switch (HazardType){
                case "Litter" : Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.litter_hazard); break;
                case "Fallen tree" : Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.fallen_tree); break;
                case "Pipe leakage" : Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.clogged_drain); break;
                case "Mosquito breeding danger" : Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.mosquito_everywhere); break;
                case "Rats in public area" : Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.rats_everywhere); break;
                case "Choked drain" : Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.no_water); break;
                default: Hazard_icon = (BitmapDrawable) getDrawable(R.drawable.lamp_post); break;
            }

            Bitmap bitmap = Hazard_icon.getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

            LatLng name = cardDataSource.getHazardLatLng(index);
            Log.d("Marker Created", "Created");
            mMap.addMarker(
                    new MarkerOptions()
                            .position(name)
                            .title(cardDataSource.getHazardName(index))
                            .snippet(cardDataSource.getHazardDescription(index))
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(name, 15));
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    }