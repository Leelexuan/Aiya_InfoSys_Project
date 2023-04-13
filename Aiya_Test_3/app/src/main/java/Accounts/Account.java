package Accounts;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// template design pattern to build an Account from the inputs.
public abstract class Account {

    final String accountNode = "Accounts";


    abstract void update(String date, String hazardName, String message);

    abstract void createAccountInDatabase(String uid);

    public String getUid() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            return uid;
        }
    return "error";
    }

}