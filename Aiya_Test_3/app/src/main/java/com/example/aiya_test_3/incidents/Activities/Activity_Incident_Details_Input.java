package com.example.aiya_test_3.incidents.Activities;

import androidx.annotation.NonNull;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

// For Firebase
import com.example.aiya_test_3.R;
import com.example.aiya_test_3.incidents.IncidentLog;
import com.example.aiya_test_3.incidents.Submitted_Details;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Activity_Incident_Details_Input extends AppCompatActivity {
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
    LatLng HazardAddress_LatLng;

    // Incident Log (Singleton)
    private IncidentLog incidentLog = IncidentLog.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final String issueNode = "Issues";
        nNodeRefIssueList = nRootDatabaseRef.child(issueNode);

        // Get the list of issues
        // So onDataChange is async, so means that it is getting this data while the app is also building
        // So we need to have another issueList and drop down that is null while this is still getting the data
        nNodeRefIssueList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();
                issueList = new String[size];
                int index = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Object value = dataSnapshot.getValue();
                    if (value != null && value instanceof String) {
                        issueList[index++] = dataSnapshot.getValue(String.class);
                    }
                }

                String[] options = issueList; // This are the options for the dropdown option
                ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(Activity_Incident_Details_Input.this, android.R.layout.simple_spinner_item, options); // Add options to spinner

                Spinner HazardTypeDropDownMenu = input_detail.findViewById(R.id.hazardTypeSpinner);
                HazardTypeDropDownMenu.setAdapter(optionAdapter); // set the options into the drop down menu
                HazardTypeDropDownMenu.setSelection(0); // Set the first value as the default value

                // Note currently when you choose an item in drop down box, it doesn't do anything other than logcat
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // From here, it is just a repeat of whatever is inside onDataChange but with null values
        //=======================================================================================
        ArrayAdapter<String> optionAdapter = null;
        if (issueList == null) {
            issueList = new String[0];
            String[] options = issueList; // This are the options for the dropdown option
            optionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);

        }

        Spinner HazardTypeDropDownMenu = input_detail.findViewById(R.id.hazardTypeSpinner);
        HazardTypeDropDownMenu.setAdapter(optionAdapter); // set the options into the drop down menu
        HazardTypeDropDownMenu.setSelection(0); // Set the first value as the default value

        // Note currently when you choose an item in drop down box, it doesn't do anything other than logcat
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
        //=============================== END onDataChange Duplicate w Null ====================

        // This is for the radio button on the hazard severity
        RadioGroup radioGroup = input_detail.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            //Todo UI: Input the radio button icons with visibility change
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Perform an action based on the selected radio button
                if (checkedId == R.id.radio_button_important) {
                    Log.d("BUTTON", "Hello This is Important");// Option "Important" is selected
                } else if (checkedId == R.id.radio_button_warning) {
                    Log.d("BUTTON", "Hello This is Warning");// Option "Warning" is selected
                } else if (checkedId == R.id.radio_button_mild) {
                    Log.d("BUTTON", "Hello This is Mild"); // Option "Mild" is selected
                } else if (checkedId == R.id.radio_button_good) {
                    Log.d("BUTTON", "Hello This is Good"); // Option "Good" is selected
                }
            }
        });

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

        //name of photo to be uploaded
        String filename = "/User Uploaded Photos Test/" + System.currentTimeMillis() + ".jpg";

        ActivityResultLauncher<Intent> photo_app_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri photoUri = result.getData().getData();
                        StorageReference imageRef = storageRef.child(filename);
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


                        //display user input
                        ImageView uploaded_photo = input_detail.findViewById(R.id.uploadedPhotoImage);
                        uploaded_photo.setImageURI(photoUri);
                        uploaded_photo.setVisibility(View.VISIBLE);
                        Log.d("putFile","User uploaded photo to database");
                        }
                    }

        );


        Button submitPicture = input_detail.findViewById(R.id.uploadPhotoBtn);
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

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

        // All the different inputs from the user are being initiated here
        // Todo Design Pattern: To rewrite using template/builder pattern (Lesson 3)
        Button submitHazard = input_detail.findViewById(R.id.submitHazardBtn);
        EditText HazardName_Input = input_detail.findViewById(R.id.editText_HazardName);
        EditText HazardAddress_Input = input_detail.findViewById(R.id.editText_PostalAddress);
        EditText HazardDescription_Input = input_detail.findViewById(R.id.editText_HazardDescriptionMultiLine);

        // Initialise places
        String MAPS_API = "AIzaSyDnIVT6BNvi2ANiKaoYhmteP3WaSGjbuOI";
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
                        Log.d("Place LatLng",place.getLatLng().toString());
                        HazardAddress_Input.setText(place.getAddress());
                        HazardAddress_LatLng = place.getLatLng();
                    }
                });

        // Set click listener for HazardAddress_Input EditText
        HazardAddress_Input.setOnClickListener(v -> {
            List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, field)
                    .build(Activity_Incident_Details_Input.this);
            launcher.launch(intent); // Use the launcher to start the activity
        });



        submitHazard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent go_to_submit_page = new Intent(Activity_Incident_Details_Input.this, Submitted_Details.class);
                startActivity(go_to_submit_page);
                Log.d("Submit Button", "User clicked submit details");

                // obtain Hazard Name and pad to the right 15 spaces
                String hazardName = String.format("%-15s", HazardName_Input.getText().toString());
                String hazardAddress = String.format("%-15s", HazardAddress_Input.getText().toString());

                // getting the current date
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // documenting the incident into the incident log.
                incidentLog.INFO("|" + String.format("%-12s", date) + "|" + hazardName + "|" + hazardAddress + "|");

                // record the incident documentation into logcat.
                Log.d("Incident Log", incidentLog.recordLog());


                // Create a HashMap with the header as keys and input as values
                HashMap<String, Object> Send_database_details = new HashMap<>();
                Send_database_details.put("HazardName_Input", HazardName_Input.getText().toString());
                Send_database_details.put("HazardAddress_Input", HazardAddress_Input.getText().toString());
                Send_database_details.put("HazardAddress_Lat", HazardAddress_LatLng.latitude);
                Send_database_details.put("HazardAddress_Long", HazardAddress_LatLng.longitude);
                Send_database_details.put("HazardDescription_Input", HazardDescription_Input.getText().toString());
                Send_database_details.put("HazardType_Input", HazardTypeDropDownMenu.getSelectedItem().toString());

                //Todo Database: Link firebase STORAGE image url to firebase REAL TIME DATABASE (Lesson 5)
                Send_database_details.put("HazardImage_Input", imageFileNameInStorage);

                // Send the HashMap to Firebase
                DatabaseReference nNodeRefPush = nNodeRefInputDetails.push();
                nNodeRefPush.setValue(Send_database_details);

            }
        });

    }
}