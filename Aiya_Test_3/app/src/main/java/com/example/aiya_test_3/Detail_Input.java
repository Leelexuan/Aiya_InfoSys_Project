package com.example.aiya_test_3;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Detail_Input extends AppCompatActivity {
    private LayoutInflater inflater; // To instantiate a layout (use this to have more than 1 layout for every activity)
    private LinearLayout appbarContainer,inputdetailsContainer; // Linear layout means it is either horizontal or vertical, holds the respective name item
    private View app_bar,input_detail;

    // Firebase
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;

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

        String[] options = {"Trees", "Pot Holes", "Dead Animals"}; // This are the options for the dropdown option
        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options); // Add options to spinner

        Spinner hazardDropDownMenu = input_detail.findViewById(R.id.hazardTypeSpinner);
        hazardDropDownMenu.setAdapter(optionAdapter); // set the options into the drop down menu
        hazardDropDownMenu.setSelection(0); // Set the first value as the default value

        // Note currently when you choose an item in drop down box, it doesn't do anything other than logcat
        hazardDropDownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Hazard Drop Down Menu","You have selected: " + hazardDropDownMenu.getSelectedItem());// Option "Important" is selected
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // This is for the radio button on the hazard severity
        RadioGroup radioGroup = input_detail.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            //Todo: Input the radio button icons with visibility change
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
        submitPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Todo: Let user open either camera or gallery and store it

                Intent openCameraAppIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(openCameraAppIntent);
                Log.d("Submit Button", "User clicked submit picture");

                // Todo: Get actual photo that user picked and display here
                ImageView uploadedPhoto = findViewById(R.id.uploadedPhotoImage);
                uploadedPhoto.setVisibility(View.VISIBLE);

            }
        });

        //Todo: Send information recorded here into database
        Button submitHazard = input_detail.findViewById(R.id.submitHazardBtn);

        EditText HazardName_Input = input_detail.findViewById(R.id.editText_HazardName);
        EditText HazardAddress_Input = input_detail.findViewById(R.id.editText_PostalAddress);
        EditText HazardDescription_Input = input_detail.findViewById(R.id.editText_HazardDescriptionMultiLine);

        // Firebase
        final String node = "Hazard_Details";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);

        submitHazard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent go_to_submit_page = new Intent(Detail_Input.this, Submitted_Details.class);
                startActivity(go_to_submit_page);
                Log.d("Submit Button", "User clicked submit details");

                //Todo: Update and send information to database
                Log.d("HazardName_Input: ", String.valueOf(HazardName_Input.getText()));
                Log.d("HazardAddress_Input: ", String.valueOf(HazardAddress_Input.getText()));
                Log.d("HazardDescription_Input: ", String.valueOf(HazardDescription_Input.getText()));

                // Create a HashMap with log message keys and values
                HashMap<String, String> Send_database_details = new HashMap<>();
                Send_database_details.put("HazardName_Input", HazardName_Input.getText().toString());
                Send_database_details.put("HazardAddress_Input", HazardAddress_Input.getText().toString());
                Send_database_details.put("HazardDescription_Input", HazardDescription_Input.getText().toString());

                // Send the HashMap to Firebase
                DatabaseReference nNodeRefPush = nNodeRef.push();
                nNodeRefPush.setValue(Send_database_details);
            }
        });

    }
}