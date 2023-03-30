package com.example.aiya_test_3.login;


import androidx.annotation.Nullable;

//where we push to the database, I guess
public class SignupResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    //set by way of constructor, by login activity
    SignupResult(@Nullable Integer error) {
        this.error = error;
    }

//    SignupResult(@Nullable LoggedInUserView success) {
//        this.success = success;
//    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
