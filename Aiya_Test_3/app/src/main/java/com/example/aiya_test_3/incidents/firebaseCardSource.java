package com.example.aiya_test_3.incidents;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class firebaseCardSource implements cardDataSource{

    public static final String FIREBASE_TESTING = "FirebaseTesting";
    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    FirebaseStorage storageDatabaseRef;
    StorageReference storageRef;

    int size;
    long numberOfIncident;
    List<String> hazardDescriptionList,hazardAddressList,hazardNameList,hazardImageList, hazardTypeList;
    List<Double> hazardLatList,hazardLngList;
    List<String> OriginalHazardNameList;
    List<String> OriginalhazardDescriptionList;
    List<String> OriginalhazardTypeList;
    List<String> OriginalhazardAddressList;
    List<String> OriginalhazardImageList;
    List<Double> OriginalhazardLatList;
    List<Double> OriginalhazardLngList;
    TextView t;
    boolean initialDataReadyFlag = false;

    public firebaseCardSource(TextView t){

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        final String node = "Hazard_Details";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

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

    public void rebuild_list(String searcher){
        List<Integer> indexes = new ArrayList<>();

        hazardNameList.clear();
        hazardDescriptionList.clear();
        hazardTypeList.clear();
        hazardAddressList.clear();
        hazardImageList.clear();
        hazardLatList.clear();
        hazardLngList.clear();

        for (int i = 0; i < OriginalHazardNameList.size(); i ++) {
            hazardNameList.add(OriginalHazardNameList.get(i));
            hazardDescriptionList.add(OriginalhazardDescriptionList.get(i));
            hazardTypeList.add(OriginalhazardTypeList.get(i));
            hazardAddressList.add(OriginalhazardAddressList.get(i));
            hazardImageList.add(OriginalhazardImageList.get(i));
            hazardLatList.add(OriginalhazardLatList.get(i));
            hazardLngList.add(OriginalhazardLngList.get(i));
        }

        for(int i = 0; i < hazardNameList.size(); i++){
            if (hazardNameList.get(i).toLowerCase().contains(searcher.toLowerCase())){
                if(!indexes.contains(i))
                    indexes.add(i);
            }
        }

        // Retain specific elements
        for (int i = hazardNameList.size() - 1; i >= 0; i--) {
            if (!indexes.contains(i)) {
                hazardNameList.remove(i);
                hazardDescriptionList.remove(i);
                hazardTypeList.remove(i);
                hazardAddressList.remove(i);
                hazardImageList.remove(i);
                hazardLatList.remove(i);
                hazardLngList.remove(i);
            }
        }

        size = hazardNameList.size();
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

    public void buildOriginalList(){

        OriginalHazardNameList = new ArrayList<>();
        OriginalhazardDescriptionList = new ArrayList<>();
        OriginalhazardTypeList = new ArrayList<>();
        OriginalhazardAddressList = new ArrayList<>();
        OriginalhazardImageList = new ArrayList<>();
        OriginalhazardLatList = new ArrayList<>();
        OriginalhazardLngList = new ArrayList<>();

        for (int i = 0; i < hazardNameList.size(); i ++) {
            OriginalHazardNameList.add(hazardNameList.get(i));
            OriginalhazardDescriptionList.add(hazardDescriptionList.get(i));
            OriginalhazardTypeList.add(hazardTypeList.get(i));
            OriginalhazardAddressList.add(hazardAddressList.get(i));
            OriginalhazardImageList.add(hazardImageList.get(i));
            OriginalhazardLatList.add(hazardLatList.get(i));
            OriginalhazardLngList.add(hazardLngList.get(i));
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

    public StorageReference getHazardImage(int i) {
        StorageReference hazardPictureLocation =  storageRef.child(hazardImageList.get(i));
        return hazardPictureLocation;
    }

    public void removeWord(int i){

        String word = hazardDescriptionList.get(i);
        size -= 1;
        nNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if( dataSnapshot.getValue().toString().equals(word) ){
                        Log.d(FIREBASE_TESTING, "word:" + word + dataSnapshot.getValue().toString());
                        hazardNameList.remove(i);
                        hazardDescriptionList.remove(i);
                        hazardTypeList.remove(i);
                        hazardAddressList.remove(i);
                        hazardImageList.remove(i);
                        hazardLatList.remove(i);
                        hazardLngList.remove(i);
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
