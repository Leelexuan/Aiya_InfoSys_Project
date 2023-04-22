package com.example.aiya_test_3.incidents.Activities; // Don't forgot to change this if you shift files out

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

// For Firebase
import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.CardAdapter;
import com.example.aiya_test_3.incidents.firebaseCardSource;
import com.example.aiya_test_3.login.Activities.Activity_Logout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;


public class Activity_Incidents extends AppCompatActivity implements SearchView.OnQueryTextListener, OnMapReadyCallback, OnMapsSdkInitializedCallback {

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
    RecyclerView CardContainer;

    // Firebase
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    CardAdapter adapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(1.3521, 103.8198);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private CameraPosition cameraPosition;
    private static final String TAG = Activity_Incidents.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) { // First thing that runs when you open activity
        super.onCreate(savedInstanceState);

        // Inform the maps to use Latest renderer
        // This is so that we can use the cloud-based map styling instead of local styling
        // This is better as local styling will require the user to "update" the app every time we want the map style to change
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Initialising Firebase Data
        // This allows us to get the firebase data as quickly as possible by making it the first thing to be loaded
        cardDataSource = new firebaseCardSource(null);

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
        CardContainer = findViewById(R.id.revisedCardContainer);
        cardContent = cardView.findViewById(R.id.card_content);
        cardTextView = cardContent.findViewById(R.id.hazardDescription);

        adapter = new CardAdapter(this, cardDataSource); // Here we initialize the recycler view adapter with the card data
        CardContainer.setAdapter( adapter ); // Then we set the adapter
        CardContainer.setLayoutManager( new LinearLayoutManager(this));

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

    private Runnable checkOnClickCard = new Runnable() {
        @Override
        public void run() {

            if(adapter.isCardClicked()){
                LatLng LastClickedLocation = adapter.getClickedCardPosition();
                moveMap(LastClickedLocation);
                adapter.setCardClicked(false);
            }
            handler.postDelayed(checkOnClickCard, 0);
        }
    };

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
                Log.d("Refresh","Stop Refresh");
                handler.removeCallbacks(refreshRunnable);
                if(adapter.isCardClicked()){
                    LatLng LastClickedLocation = adapter.getClickedCardPosition();
                    moveMap(LastClickedLocation);
                    handler.postDelayed(checkOnClickCard, 0);
                    adapter.setCardClicked(false);
                }
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

        if(adapter.isCardClicked()){
            LatLng LastClickedLocation = adapter.getClickedCardPosition();
            moveMap(LastClickedLocation);
            adapter.setCardClicked(false);
        }

        handler.postDelayed(checkOnClickCard, 0);
        handler.postDelayed(refreshRunnable, 0);
    }

    // Stop auto-refresh when the Activity stops
    @Override
    protected void onStop() {
        super.onStop();
        // Remove any pending callbacks to stop the auto-refresh
        handler.removeCallbacks(checkOnClickCard);
        handler.removeCallbacks(refreshRunnable);
    }

    // Update data in the adapter
    private void refreshData() {
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
        cardDataSource = new firebaseCardSource(query);
        handler.postDelayed(refreshRunnable, 0);
        refreshCardContainerAdapter();
        return true;
    }

    // We use onQueryTextChange to detect when player has cancelled their search query
    @Override
    public boolean onQueryTextChange(String query) {
        if (searchView.getQuery().length() == 0) {
            cardDataSource = new firebaseCardSource(null);
            handler.postDelayed(refreshRunnable, 0);
            refreshCardContainerAdapter();
        }
        return false;
    }

    private void refreshCardContainerAdapter(){
        adapter = new CardAdapter(this, cardDataSource); // Here we initialize the recycler view adapter with the card data
        CardContainer.setAdapter(null); // reset the adapter to null
        CardContainer.setLayoutManager(null);
        CardContainer.setAdapter(adapter); // set the adapter again with the revised list
        CardContainer.setLayoutManager(new LinearLayoutManager(this));
    }

    // Methods for map usage
    // when the map is ready
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Notify the log that the mpa is ready
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        Log.d("Map Created", "Created");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
        // Checking in logcat whether the user is using the latest ver of renderer
        // This affects the map styling
        // Phone Requirements:
        // Android 5.0 (API level 21) or later (Our app is built for API 30)
        // 2 GB or more storage
        // Using Google Play Service Ver 21.39.14 or later

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
    private void addNewMarkers(){

        // Adding the markers into the map
        for(int index = 0; index < cardDataSource.numberOfIncident(); index++){

            // Add bitmap marker
            String HazardType = cardDataSource.getHazardType(index);

            Log.d("Hazard Type: ",HazardType);
            BitmapDrawable Hazard_icon;
            Hazard_icon = getCorrespondingIcon(HazardType);
            Bitmap hazard_iconBitmap = Hazard_icon.getBitmap();
            Bitmap resized_hazard_iconBitmap = Bitmap.createScaledBitmap(hazard_iconBitmap, 200, 200, false);

            LatLng name = cardDataSource.getHazardLatLng(index);
            Log.d("Marker Created", "Created");
            mMap.addMarker(
                    new MarkerOptions()
                            .position(name)
                            .title(cardDataSource.getHazardName(index))
                            .snippet(cardDataSource.getHazardDescription(index))
                            .icon(BitmapDescriptorFactory.fromBitmap(resized_hazard_iconBitmap)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(name, 15));
        }
    }

    private BitmapDrawable getCorrespondingIcon(String HazardType){

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

        return Hazard_icon;
    }

    public void moveMap(LatLng LastClickedLocation){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LastClickedLocation, 15));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    }