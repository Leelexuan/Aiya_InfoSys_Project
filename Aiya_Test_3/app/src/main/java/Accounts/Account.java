package Accounts;

import java.util.ArrayList;

// template design pattern to build an Account from the inputs.
public abstract class Account {
    abstract String comment(String comment);
}

// each account inherits from Account and should implement the observer interface.
class normalAccount extends Account implements Observer{
    private ArrayList<Subject> incidents;

    @Override
    String comment(String comment){return "Test";}

    @Override
    public void update(String message) {
    }

    // handles what happens to the normal account when we register the user.
    public void upvote(){
        // TODO register user to the subject (incident/hazard)
    }
}

class verifiedAccount extends Account implements Observer{
    // this subclass is meant for official (government) accounts
    @Override
    String comment(String comment){return "Test";}

    @Override
    public void update(String message) {
        // TODO should display a toast message about the incident that they are subscribed to here.
    }


}