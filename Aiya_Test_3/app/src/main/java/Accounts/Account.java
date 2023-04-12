package Accounts;

import com.example.aiya_test_3.incidents.IncidentLog;

import java.util.ArrayList;

// template design pattern to build an Account from the inputs.
public abstract class Account {
    abstract void comment(String comment);
}

// each account inherits from Account and should implement the observer interface.
class normalAccount extends Account implements Observer{
    private ArrayList<Subject> incidents;

    @Override
    void comment(String comment){

    }

    @Override
    public void update(String message) {
        // TODO should display a toast message about the incident that they are subscribed to here.
    }

    // handles what happens to the normal account when we register the user.
    public void upvote(Subject incident){
        // TODO register user to the subject (incident/hazard)
        incidents.add(incident);
    }

    normalAccount(ArrayList<Subject> incidents){
        // instantiate incidents that this account is subscribed to - TODO get incidents from DATABASE
        this.incidents = incidents;
    }
}

class verifiedAccount extends Account{
    // this subclass is meant for official (government) accounts
    @Override
    void comment(String comment){
        // expecting to comment (should add a VERIFIED MESSAGE in front to distinguish the verified users.
    }

    // polymorphism - same method name but do different things.
    public void update(String date, String hazardName, String message) {
        IncidentLog incidentLog = IncidentLog.getInstance();
        String updateMessage = "|" + String.format("%-12s", date) + "|" + hazardName + "|" + message + "|";
        incidentLog.INFO(updateMessage);

        //TODO DATABASE send message to user account.
    }
}