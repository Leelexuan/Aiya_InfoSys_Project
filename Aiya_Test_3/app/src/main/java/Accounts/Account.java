package Accounts;

// template design pattern to build an Account from the inputs.
public abstract class Account {
    void prepareAccount() {
        getEmail();
        getName();
        getPassword();
    }

    abstract void getEmail();

    abstract void getName();

    abstract void getPassword();

}

class normalAccount extends Account{
    @Override
    void getEmail() {

    }

    @Override
    void getName() {

    }

    @Override
    void getPassword() {

    }
}