package Accounts;

import com.example.aiya_test_3.incidents.IncidentLog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class officialAccount extends Account implements Observer{

    // polymorphism - same method name but do different things.
    @Override
    public void update(String date, String hazardName, String message) {
        // 1. remove incident from user subscribed list.
        // 2. add message to updateMessages in database
        // 3. log update on IncidentLog

        IncidentLog incidentLog = IncidentLog.getInstance(); // get instance of incident log (singleton)
        // format string
        String updateMessage = "|" + String.format("%-12s", date) + "|" + hazardName + "|" + message + "|";

        // send message to this verified account (this is to account for other NEA/Official accounts to be notified of the update).
        // TODO add message to updateMessageList
        // eg: updateMessages.add(updateMessage)



        // log updates to incident Log.
        incidentLog.INFO(updateMessage);
    }

    @Override
    public void createAccountInDatabase(String uid) {
        // Creates official account in the realtime database with the Uid of the user account as key
        DatabaseReference nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();;
        DatabaseReference accountRef = nRootDatabaseRef.child(accountNode);

        HashMap<String, Object> newaccount = new HashMap<>();
        newaccount.put(uid, new HashMap<>());

        accountRef.updateChildren(newaccount);

        DatabaseReference newaccountref = accountRef.child(uid);


        HashMap<String, Object> AccountDetails = new HashMap<>();

        AccountDetails.put("Official", true);

        newaccountref.setValue(AccountDetails);

    }
}