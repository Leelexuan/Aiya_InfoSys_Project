package com.example.aiya_test_3.login;

/**
 * Class exposing authenticated user details to the UI.
 */

//right now, all of this only displays the username to the user
    //future use: use with UpdateUItoUser when they are logged in
class LoggedInUserView {
    private String displayName;
    //...TODO: other data fields that may be accessible to the UI
    //e.g. user icons/avatars...but uuuuuuh, that's optional

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}