package com.example.aiya_test_3.login;


import androidx.annotation.Nullable;

//where we push to the database, I guess
public class SignupResult {
//    @Nullable
//    private LoggedInUserView success;
    @Nullable
    private Integer error;
    private boolean success;

    //set by way of constructor, by login activity
    SignupResult(@Nullable Integer error) {
        this.error = error;
    }

    SignupResult(@Nullable Boolean success){this.success = success;}

//    SignupResult(@Nullable LoggedInUserView success) {
//        this.success = success;
//    }

//    @Nullable
//    LoggedInUserView getSuccess() {
//        return success;
//    }

    @Nullable
    Integer getError() {
        return error;
    }

    //if you want nullable you cannot use primitive
    //need to use wrapper
    //so cursed
    @Nullable
    Boolean getSuccess() {return success;}
}
