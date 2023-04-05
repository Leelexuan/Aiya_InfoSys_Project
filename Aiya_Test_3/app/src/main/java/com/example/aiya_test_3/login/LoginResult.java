package com.example.aiya_test_3.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */

class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    //set by way of constructor, by login activity
    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}