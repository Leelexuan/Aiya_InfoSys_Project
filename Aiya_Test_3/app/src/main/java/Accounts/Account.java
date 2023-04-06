package Accounts;

// template design pattern to build an Account from the inputs.
public abstract class Account {
    void prepareAccount() {
        String email = getEmail();
        String password = getPassword();
        // TODO: generate unique user id if have time.
        // TODO: add account to database.
    }

    abstract String getEmail();
    abstract String getPassword();

}

class normalAccount extends Account{
    @Override
    String getEmail() {
        return "Test";
    }

    @Override
    String getPassword() {
        return "Test";
    }
}

class verifiedAccount extends Account{
    // this subclass is meant for official (government) accounts
    @Override
    String getEmail() {
        return "Test";
    }

    @Override
    String getPassword() {
        return "Test";
    }
}