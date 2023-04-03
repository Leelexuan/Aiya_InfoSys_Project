package Accounts;

// consider using templater and builder pattern also can.
public abstract class Account {
    // TODO implement visitor: upvote/downvoting incidents - TBC
    // TODO implement observer: "subscribe" to incidents - for reported incidents/following incidents.
    // Attributes
    private Integer userID;
    public Integer getUserID() {
        return userID;
    }
    // TODO 3: normal methods:
}

// TODO Normal Account extends Account
class NormalAccount extends Account{

}

// TODO Verified/Official Accounts for NEA, etc extends Account
// Able to Resolve Incidents - add updates to incidents.
class VerifiedAccount extends Account{

}