package com.example.aiya_test_3.incidents;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class firebaseCardSource implements cardDataSource {

    public static final String FIREBASE_TESTING = "FirebaseTesting";
    DatabaseReference nRootDatabaseRef, mRootDatabaseRef;
    DatabaseReference nNodeRef, mNodeRef;
    Query recentPost, queryPost;
    FirebaseStorage storageDatabaseRef;
    StorageReference storageRef;

    int size;
    List<String> hazardDescriptionList,hazardAddressList,hazardNameList,hazardImageList, hazardTypeList;
    List<Double> hazardLatList,hazardLngList;
    List<String> OriginalHazardNameList;
    List<String> OriginalhazardDescriptionList;
    List<String> OriginalhazardTypeList;
    List<String> OriginalhazardAddressList;
    List<String> OriginalhazardImageList;
    List<Double> OriginalhazardLatList;
    List<Double> OriginalhazardLngList;

    List<IncidentObject> IncidentObjectsList;
    ChildEventListener post;
    ValueEventListener post2;
    String searcher;
    boolean initialDataReadyFlag = false;
    final AtomicInteger numberOfIncident = new AtomicInteger();

    public firebaseCardSource(){

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        final String node = "Incident Objects";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);
        recentPost = nNodeRef.limitToLast(5);

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

        hazardDescriptionList = new ArrayList<>();
        hazardNameList = new ArrayList<>();
        hazardTypeList = new ArrayList<>();
        hazardAddressList = new ArrayList<>();
        hazardLatList = new ArrayList<>();
        hazardLngList = new ArrayList<>();
        hazardImageList = new ArrayList<>();
        IncidentObjectsList = new ArrayList<>();

        post2 = recentPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countListItems(snapshot);

            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//
            }
        });

        post = recentPost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int count = numberOfIncident.incrementAndGet();
                Log.d("CheckPoint 1", "Started");
                repopulateList(snapshot);
                initialDataReadyFlag = true;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public firebaseCardSource(String searcher){

        // Firebase Real-Time Database (Only for scalar data type e.g string, int, float)
        final String node = "Incident Objects";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);
        this.searcher = searcher;
        //recentPost = nNodeRef.orderByChild(searcher+"/hazardName_Input").equalTo(searcher);
        recentPost = nNodeRef.orderByChild("Incident/upvotes").equalTo(searcher);

        // Firebase Storage (For images and all form of data, can think of it like google drive)
        storageDatabaseRef = FirebaseStorage.getInstance();
        storageRef = storageDatabaseRef.getReference();

        hazardDescriptionList = new ArrayList<>();
        hazardNameList = new ArrayList<>();
        hazardTypeList = new ArrayList<>();
        hazardAddressList = new ArrayList<>();
        hazardLatList = new ArrayList<>();
        hazardLngList = new ArrayList<>();
        hazardImageList = new ArrayList<>();
        IncidentObjectsList = new ArrayList<>();

        post2 = recentPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                countListItems(snapshot);

            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//
            }
        });

        post = recentPost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int count = numberOfIncident.incrementAndGet();
                Log.d("CheckPoint 1", "Started");
                repopulateList(snapshot);
                initialDataReadyFlag = true;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countListItems(DataSnapshot snapshot){
        size = (int) snapshot.getChildrenCount();
        Log.d(FIREBASE_TESTING, "constructor size " + size);
    }

    private void repopulateList(DataSnapshot snapshot){

        Log.d("CheckPoint 2", "Started");

        for(DataSnapshot dataSnapshot: snapshot.getChildren()){

            String IncidentID = snapshot.getKey();

            IncidentObject value = dataSnapshot.getValue(IncidentObject.class); // Notice here you get the class Incident Object from firebase
            System.out.println(value);

            if (value != null && value instanceof IncidentObject) {
                IncidentObjectsList.add(value); // Put the objects into a list for further reference
                IncidentObjectsList.get(numberOfIncident.intValue()-1).setHazardID(IncidentID); // get the objectID (This is a unique ID of the object randomly generated).
                // This is required for path

                // Access the values from the object using the getter
                String HazardName_Key = value.getHazardName_Input();
                String HazardDescription_Key = value.getHazardDescription_Input();
                String HazardType_Key = value.getHazardType_Input();
                String HazardAddress_Key = value.getHazardAddress_Input();
                Double HazardLat_key = value.getHazardAddress_Lat();
                Double HazardLng_key = value.getHazardAddress_Long();
                String HazardImage_Key = value.getHazardImage_Input();

                // Putting them into the respective list
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
    public long numberOfIncident(){ return numberOfIncident.longValue(); }
    public String getHazardName(int i){
        return hazardNameList.get(i);
    }

    @Override
    public void addWord(String s) {
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
    public int getSize(){
        return size;
    }

    public IncidentObject getIncidentObject(int i) {
        return IncidentObjectsList.get(i);
    }

    public IncidentObject getIncidentObjectbyName(String i) {

        for (IncidentObject EachIncident : IncidentObjectsList){

            if(EachIncident.getHazardName_Input().equals(i)){

                return EachIncident;

            }

        }

        return null;
    }
}

class getDataFromFirebase implements Runnable{

    @Override
    public void run(){
    }
}
