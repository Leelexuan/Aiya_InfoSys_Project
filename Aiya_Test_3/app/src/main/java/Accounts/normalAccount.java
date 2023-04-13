package Accounts;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

// each account inherits from Account and should implement the observer interface.
public class normalAccount extends Account implements Observer{

    @Override
    public void update(String date, String hazardName, String message) {
        // TODO should display a toast message about the incident that they are subscribed to here.
    }

    @Override
    public void createAccountInDatabase(String uid) {
        DatabaseReference nRootDatabaseRef = FirebaseDatabase.getInstance().getReference();;
        DatabaseReference accountRef = nRootDatabaseRef.child(accountNode);

        HashMap<String, Object> newaccount = new HashMap<>();
        newaccount.put(uid, new HashMap<>());

        accountRef.updateChildren(newaccount);

        DatabaseReference newaccountref = accountRef.child(uid);

        HashMap<String, Object> AccountDetails = new HashMap<>();
        HashMap<String, Object> incidents = new HashMap<>();
        HashMap<String, Object> messages = new HashMap<>();

        incidents.put("default key", "none");
        messages.put("default key", "none");

        AccountDetails.put("Official", false);
        AccountDetails.put("Incidents", incidents);
        AccountDetails.put("Messages", messages);

        newaccountref.setValue(AccountDetails);

    }


    // handles what happens to the normal account when we register the user.
    public void upvote(Subject incident){
        // TODO register user to the subject (incident/hazard)
        // TODO get incident id from database
        //incidents.put("test", true);
    }

    public void getSubscribedIncidents(ArrayList<Subject> incidents){
        // instantiate incidents that this account is subscribed to - TODO get incidents from DATABASE

    }


}
