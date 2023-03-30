package com.example.aiya_test_3.login;

import androidx.annotation.Nullable;

public class SignupFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmError;
    private boolean isDataValid;

    //two different constructors to indirectly set values
    SignupFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer confirmError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.confirmError = confirmError;
        this.isDataValid = false;
    }

    //essentially only used when isDataValid=true
    SignupFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.confirmError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getConfirmError() {
        return confirmError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
