package Accounts;

import com.example.aiya_test_3.incidents.IncidentLog;

import java.util.ArrayList;
import java.util.HashMap;

// template design pattern to build an Account from the inputs.
public abstract class Account {

    private boolean offical;

    abstract void update(String date, String hazardName, String message);

    abstract void createAccountInDatabase();
}

// each account inherits from Account and should implement the observer interface.
class normalAccount extends Account implements Observer{
    private HashMap<String, Boolean> incidents;

    @Override
    public void update(String date, String hazardName, String message) {
        // TODO should display a toast message about the incident that they are subscribed to here.
    }

    @Override
    public void createAccountInDatabase() {

    }


    // handles what happens to the normal account when we register the user.
    public void upvote(Subject incident){
        // TODO register user to the subject (incident/hazard)
        // TODO get incident id from database
        incidents.put("test", true);
    }

    normalAccount(ArrayList<Subject> incidents){
        // instantiate incidents that this account is subscribed to - TODO get incidents from DATABASE

    }


}

class verifiedAccount extends Account implements Observer{

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
    public void createAccountInDatabase() {

    }
}