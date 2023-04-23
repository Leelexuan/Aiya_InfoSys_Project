package com.example.aiya_test_3.incidents;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TestDatabaseGet {

    DatabaseReference nRootDatabaseRef;
    DatabaseReference nNodeRef;
    Query query;

    public TestDatabaseGet(){
        final String node = "Incident Objects";
        nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        nNodeRef = nRootDatabaseRef.child(node);
        query = nNodeRef.orderByChild("hazardAddress_Lat").equalTo(1.3644202);
        Log.d("database value","test");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("database value",snapshot.getKey());

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    String IncidentID = snapshot.getKey();
                    IncidentObject value = dataSnapshot.getValue(IncidentObject.class); // Notice here you get the class Incident Object from firebase
                    System.out.println(value);

                    if (value != null && value instanceof IncidentObject) {
                        // This is required for path
                    }
                }
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
}


