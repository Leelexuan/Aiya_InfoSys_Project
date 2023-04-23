package com.example.aiya_test_3.incidents.Activities;

import static com.example.aiya_test_3.BuildConfig.MAPS_API_KEY;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.CheckIncidentUserInputs;
import com.example.aiya_test_3.incidents.IncidentLog;
import com.example.aiya_test_3.incidents.IncidentObject;
import com.example.aiya_test_3.incidents.Submitted_Details;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Activity_Incident_Details_Input extends AppCompatActivity {

    /*DESCRIPTION
     * This is the activity class for the details input page.
     * On this page, users will be able to key in data and upload hazards
     *
     *  There are a total of 6 items to input and they are all checked using the CheckIncidentUserInputs class for validity
     * */

    private LayoutInflater inflater; // To instantiate a layout (use this to have more than 1 layout for every activity)
    private LinearLayout appbarContainer,inputdetailsContainer; // Linear layout means it is either horizontal or vertical, holds the respective name item
    private View app_bar,input_detail;

    String[] issueList;
    // Firebase
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRefInputDetails, nNodeRefIssueList;
    FirebaseStorage storageDatabaseRef;
    StorageReference storageRef;

    String imageFileNameInStorage;

    EditText HazardName_Input;
    LatLng HazardAddress_LatLng = null;
    Spinner HazardTypeDropDownMenu;

    // Incident Log (Singleton)
    private IncidentLog incidentLog = IncidentLog.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

        // main layout for this page
        setContentView(R.layout.activity_detail_input); // activity_forum layout does not have content, only containers

        // Bring in contents to fill up the containers, we do this by using inflater
        // Inflater is to bring another layout into this layout
        inflater = LayoutInflater.from(this);

        app_bar = inflater.inflate(R.layout.app_bar_detail_input, null);
        input_detail = inflater.inflate(R.layout.input_details_items, null);

        // Add the app_bar view to the MainActivity's layout
        appbarContainer = findViewById(R.id.appbar);
        appbarContainer.addView(app_bar, 0);

        // Add the input detail view to the Mainactivity's layout
        inputdetailsContainer = findViewById(R.id.inputdetailsContainer);
        inputdetailsContainer.addView(input_detail);

        Button submitPicture = input_detail.findViewById(R.id.uploadPhotoBtn);
        HazardTypeDropDownMenu = input_detail.findViewById(R.id.hazardTypeSpinner);
        RadioGroup SeverityRadioGroup = input_detail.findViewById(R.id.radio_group);

        // Get the list of issues from Database
        final String issueNode = "Issues";
        nNodeRefIssueList = nRootDatabaseRef.child(issueNode);

        // onDataChange is async, so means that it is getting this data while the app is also building

        handleBackgroundTask retrieveIssues = new handleBackgroundTask();
        retrieveIssues.start(nNodeRefIssueList); // Delegation strategy -> An object to handle the background task of retrieving incidents. -> The object uses Template design.

        HazardTypeDropDownMenu.setSelection(0); // Set the first value as the default value
        // Set Drop Down on clicked listener for logging purposes
        HazardTypeDropDownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Hazard Drop Down Menu", "You have selected: " + HazardTypeDropDownMenu.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Never do anything, go back to default
            }
        });

        SeverityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            // Set On check changed listener for logging purposes
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Perform an action based on the selected radio button
                if (checkedId == R.id.red_radio_button) {
                    Log.d("BUTTON", "Hello This is Important");// Option "Important" is selected
                } else if (checkedId == R.id.orange_radio_button) {
                    Log.d("BUTTON", "Hello This is Warning");// Option "Warning" is selected
                } else if (checkedId == R.id.yellow_radio_button) {
                    Log.d("BUTTON", "Hello This is Mild"); // Option "Mild" is selected
                } else if (checkedId == R.id.green_radio_button) {
                    Log.d("BUTTON", "Hello This is Good"); // Option "Good" is selected
                }
            }
        });



        //name of photo to be uploaded
        imageFileNameInStorage = "";

        String filename = getString(R.string.StorageImagePath) + System.currentTimeMillis() + getString(R.string.ImageFileType_JPG);

        ActivityResultLauncher<Intent> photo_app_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri photoUri = result.getData().getData();
                        StorageReference imageRef = storageRef.child(filename); // Upload photos to Firebase Storage
                        imageRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                                downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageFileNameInStorage = filename;
                                    }
                                });
                            }
                        });

                        //display user input in an imageView
                        ImageView uploaded_photo = input_detail.findViewById(R.id.uploadedPhotoImage);
                        uploaded_photo.setImageURI(photoUri);
                        uploaded_photo.setVisibility(View.VISIBLE);
                        Log.d("putFile","User uploaded photo to database");
                        }
                    });

        // When user click the submit button, launch the photo gallery
        submitPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Intent for gallery app
                Intent getPicture = new Intent(Intent.ACTION_PICK);
                getPicture.setType("image/*");

                //Calling launcher
                photo_app_launcher.launch(getPicture);
                Log.d("getPicture", "User chose app");

            }
        });

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        final String hazardDetailsNode = "Hazard_Details";
        nNodeRefInputDetails = nRootDatabaseRef.child(hazardDetailsNode);

        // All the different inputs from the user are being initiated here
        Button submitHazard = input_detail.findViewById(R.id.submitHazardBtn);
        EditText HazardName_Input = input_detail.findViewById(R.id.editText_HazardName);
        EditText HazardAddress_Input = input_detail.findViewById(R.id.editText_PostalAddress);
        EditText HazardDescription_Input = input_detail.findViewById(R.id.editText_HazardDescriptionMultiLine);

        // Initialise places
        String MAPS_API = MAPS_API_KEY;
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), MAPS_API);
        }

        ActivityResultLauncher<Intent> launcher;
        // Initialize the ActivityResultLauncher
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Handle the result in the callback method
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Place place = Autocomplete.getPlaceFromIntent(data);
                        Log.d("Place",place.getAddress());
                        Log.d("Place LatLng", Objects.requireNonNull(place.getLatLng()).toString());
                        String AddressString = place.getName() + " ( " + place.getAddress() + " ) ";
                        HazardAddress_Input.setText(AddressString);
                        HazardAddress_LatLng = place.getLatLng();
                    }
                });

        // Set click listener for HazardAddress_Input EditText to open up places client
        HazardAddress_Input.setOnClickListener(v -> {
            List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, field)
                    .build(Activity_Incident_Details_Input.this);
            launcher.launch(intent); // Use the launcher to start the activity
        });

        // Set click listener when user submit
        submitHazard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("Submit Button", "User clicked submit details");

                String HazardName = HazardName_Input.getText().toString();
                String HazardAddress = HazardAddress_Input.getText().toString();
                String HazardDescription =  HazardDescription_Input.getText().toString();
                String HazardType =  HazardTypeDropDownMenu.getSelectedItem().toString();

                LogInformation(HazardName,HazardAddress);
                // Check that user has input details and that the string is not empty
                CheckIncidentUserInputs checker = new CheckIncidentUserInputs(HazardName,HazardAddress,HazardAddress_LatLng,HazardDescription,HazardType,imageFileNameInStorage);
                String checked = checker.CheckAllUserInputs();

                if(checked.equals("")){
                    Double HazardAddress_Lat = HazardAddress_LatLng.latitude;
                    Double HazardAddress_Long = HazardAddress_LatLng.longitude;

                    IncidentObject NewIncident = new IncidentObject(HazardName,HazardAddress,HazardAddress_Lat, HazardAddress_Long,HazardDescription,HazardType,imageFileNameInStorage);
                    NewIncident.saveIncidentToDatabase();

                    Intent go_to_submit_page = new Intent(Activity_Incident_Details_Input.this, Submitted_Details.class);
                    startActivity(go_to_submit_page);
                }
                else {
                    Toast.makeText(Activity_Incident_Details_Input.this, "Please input " + checked, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class handleBackgroundTask extends BackgroundTasks<DatabaseReference, ArrayList<String>>{
        @Override
        public ArrayList<String> task(DatabaseReference dbInput) {
            ArrayList<String> issues = new ArrayList<String>();
            dbInput.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int size = (int) snapshot.getChildrenCount();
                    int index = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Object value = dataSnapshot.getValue();
                        if (value != null && value instanceof String) {
                            issues.add(dataSnapshot.getValue(String.class));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            return issues;
        }

        @Override
        public void done(ArrayList<String> input) {
            if(input != null){
                String[] options = input.toArray(new String[0]); // These are the options for the dropdown option
                ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(Activity_Incident_Details_Input.this, android.R.layout.simple_spinner_item, options); // Add options to spinner
                HazardTypeDropDownMenu.setAdapter(optionAdapter); // set the options into the drop down menu
            }
        }
    }

    private void LogInformation(String HazardName, String HazardAddress){
        // obtain Hazard Name and pad to the right 15 spaces
        String formattedHazardName = String.format("%-15s", HazardName);
        String formattedHazardAddress = String.format("%-15s", HazardAddress);

        // getting the current date
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // documenting the incident into the incident log.
        incidentLog.INFO("|" + String.format("%-12s", date) + "|" + formattedHazardName + "|" + formattedHazardAddress + "|");

        // record the incident documentation into logcat.
        Log.d("Incident Log", incidentLog.returnIncidents());
    }
}