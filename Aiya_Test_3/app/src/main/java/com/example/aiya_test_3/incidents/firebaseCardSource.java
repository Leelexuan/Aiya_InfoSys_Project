package com.example.aiya_test_3.incidents;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class firebaseCardSource implements cardDataSource{

    public static final String FIREBASE_TESTING = "FirebaseTesting";
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    int size;
    long numberOfIncident;
    List<String> hazardDescriptionList,hazardAddressList,hazardNameList,hazardImageList, hazardTypeList;
    List<Double> hazardLatList,hazardLngList;
    TextView t;
    boolean initialDataReadyFlag = false;

    public firebaseCardSource(TextView t){

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        final String node = "Hazard_Details";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);

        this.t = t;
        nNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countListItems(snapshot);
                repopulateList(snapshot);
                initialDataReadyFlag = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countListItems(DataSnapshot snapshot){
        size = (int) snapshot.getChildrenCount();
        t.setText( String.valueOf(size));
        Log.d(FIREBASE_TESTING, "constructor size " + size);
    }

    private void repopulateList(DataSnapshot snapshot){
        hazardDescriptionList = new ArrayList<>();
        hazardNameList = new ArrayList<>();
        hazardTypeList = new ArrayList<>();
        hazardAddressList = new ArrayList<>();
        hazardLatList = new ArrayList<>();
        hazardLngList = new ArrayList<>();
        hazardImageList = new ArrayList<>();
        for(DataSnapshot dataSnapshot: snapshot.getChildren()){

            numberOfIncident = snapshot.getChildrenCount();

            Object value = dataSnapshot.getValue();
            if (value != null && value instanceof HashMap) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) value;
                // Access the values in the HashMap using their keys
                String HazardName_Key = (String) hashMap.get("HazardName_Input");
                String HazardDescription_Key = (String) hashMap.get("HazardDescription_Input");
                String HazardType_Key = (String) hashMap.get("HazardType_Input");
                String HazardAddress_Key = (String) hashMap.get("HazardAddress_Input");
                Double HazardLat_key = (Double) hashMap.get("HazardAddress_Lat");
                Double HazardLng_key = (Double) hashMap.get("HazardAddress_Long");
                String HazardImage_Key = (String) hashMap.get("HazardImage_Input");
                hazardNameList.add(HazardName_Key);
                hazardDescriptionList.add(HazardDescription_Key);
                hazardTypeList.add(HazardType_Key);
                hazardAddressList.add(HazardAddress_Key);
                hazardImageList.add(HazardImage_Key);
                hazardLatList.add(HazardLat_key);
                hazardLngList.add(HazardLng_key);
                Log.d(FIREBASE_TESTING, hazardDescriptionList.get(0));
            }
        }
    }


    public boolean isInitialDataReadyFlag() {
        return initialDataReadyFlag;
    }

    public long numberOfIncident(){ return numberOfIncident; }

    public void addWord(String s){
        nNodeRef.push().setValue(s);
        Log.d(FIREBASE_TESTING, "add word size " + size);
    }

    public String getHazardName(int i){
        return hazardNameList.get(i);
    }
    public String getHazardDescription(int i){
        return hazardDescriptionList.get(i);
    }
    public String getHazardType(int i) { return hazardTypeList.get(i); }
    public String getHazardAddress(int i){
        return hazardAddressList.get(i);
    }

    public LatLng getHazardLatLng(int i) {
        LatLng HazardLatLng = new LatLng(hazardLatList.get(i),hazardLngList.get(i));
        return HazardLatLng;
    }

    public String getHazardImage(int i) { return hazardImageList.get(i); }

    public void removeWord(int i){

        String word = hazardDescriptionList.get(i);

        nNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if( dataSnapshot.getValue().toString().equals(word) ){
                        Log.d(FIREBASE_TESTING, "word:" + word + dataSnapshot.getValue().toString());
                        nNodeRef.child(dataSnapshot.getKey()).removeValue();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getSize(){
        return size;
    }

}
