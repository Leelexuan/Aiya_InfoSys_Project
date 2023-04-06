package com.example.aiya_test_3.incidents.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Activity_Incident_Details_Input extends AppCompatActivity {
    private LayoutInflater inflater; // To instantiate a layout (use this to have more than 1 layout for every activity)
    private LinearLayout appbarContainer,inputdetailsContainer; // Linear layout means it is either horizontal or vertical, holds the respective name item
    private View app_bar,input_detail;

    // Firebase
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    FirebaseStorage storageDatabaseRef;
    StorageReference storageRef;

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

        // Todo : Figure out the options for dropdown
        String[] options = {"Trees", "Pot Holes", "Dead Animals"}; // This are the options for the dropdown option
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options); // Add options to spinner

        Spinner HazardTypeDropDownMenu = input_detail.findViewById(R.id.hazardTypeSpinner);
        HazardTypeDropDownMenu.setAdapter(optionAdapter); // set the options into the drop down menu
        HazardTypeDropDownMenu.setSelection(0); // Set the first value as the default value

        // Note currently when you choose an item in drop down box, it doesn't do anything other than logcat
        HazardTypeDropDownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Hazard Drop Down Menu","You have selected: " + HazardTypeDropDownMenu.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Never do anything, go back to default
            }
        });

        // This is for the radio button on the hazard severity
        RadioGroup radioGroup = input_detail.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            //Todo UI: Input the radio button icons with visibility change
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Perform an action based on the selected radio button
                if (checkedId == R.id.radio_button_important) {
                    Log.d("BUTTON","Hello This is Important");// Option "Important" is selected
                } else if (checkedId == R.id.radio_button_warning) {
                    Log.d("BUTTON","Hello This is Warning");// Option "Warning" is selected
                }
                else if (checkedId == R.id.radio_button_mild) {
                    Log.d("BUTTON","Hello This is Mild"); // Option "Mild" is selected
                }
                else if (checkedId == R.id.radio_button_good) {
                    Log.d("BUTTON","Hello This is Good"); // Option "Good" is selected
                }
            }
        });


        Button submitPicture = input_detail.findViewById(R.id.uploadPhotoBtn);
        ImageView submittedPicture = input_detail.findViewById(R.id.uploadedPhotoImage);
        submitPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Todo PhotoUploading: Let user choose between gallery or camera app

                // Todo PhotoUploading: Gallery: Open Gallery and get url (Lesson 4)
                // Todo PhotoUploading: Camera: Store Image in known folder after image is taken (Lesson 4)

                /* Uncomment this to open camera app

                // Implicit Intent (Lesson 2)
                Intent openCameraAppIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(openCameraAppIntent);
                Log.d("Submit Button", "User clicked submit picture");
                */

                submittedPicture.setVisibility(View.VISIBLE);

                // Todo PhotoUploading: Get actual photo that user picked and display to input_detail.findViewById(R.id.uploadedPhotoImage) (Lesson 4)

            }
        });

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        final String node = "Hazard_Details";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

        // All the different inputs from the user are being initiated here
        // Todo Design Pattern: To rewrite using template/builder pattern (Lesson 3)
        Button submitHazard = input_detail.findViewById(R.id.submitHazardBtn);
        EditText HazardName_Input = input_detail.findViewById(R.id.editText_HazardName);
        EditText HazardAddress_Input = input_detail.findViewById(R.id.editText_PostalAddress);
        EditText HazardDescription_Input = input_detail.findViewById(R.id.editText_HazardDescriptionMultiLine);

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
                incidentLog.INFO("|" + String.format("%-12s",date) + "|" + hazardName + "|" + hazardAddress +"|");

                // record the incident documentation into logcat.
                Log.d("Incident Log", incidentLog.recordLog());


                // Create a HashMap with the header as keys and input as values
                HashMap<String, String> Send_database_details = new HashMap<>();
                Send_database_details.put("HazardName_Input", HazardName_Input.getText().toString());
                Send_database_details.put("HazardAddress_Input", HazardAddress_Input.getText().toString());
                Send_database_details.put("HazardDescription_Input", HazardDescription_Input.getText().toString());
                Send_database_details.put("HazardType_Input", HazardTypeDropDownMenu.getSelectedItem().toString());

                // Send the HashMap to Firebase
                DatabaseReference nNodeRefPush = nNodeRef.push();
                nNodeRefPush.setValue(Send_database_details);

                //Todo Database: Send image to firebase STORAGE (Lesson 5)
                //Todo Database: Link firebase STORAGE image url to firebase REAL TIME DATABASE (Lesson 5)

                /* Do the above here

                */
            }
        });

    }
}