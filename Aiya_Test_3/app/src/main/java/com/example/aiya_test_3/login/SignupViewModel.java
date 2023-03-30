package com.example.aiya_test_3.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aiya_test_3.R;
import com.example.aiya_test_3.data.LoginRepository;
import com.example.aiya_test_3.data.Result;
import com.example.aiya_test_3.data.model.LoggedInUser;

//where the values are actually set aka set on topic, not on observable
public class SignupViewModel extends ViewModel {

    private MutableLiveData<SignupFormState> signupFormState = new MutableLiveData<>();
    private MutableLiveData<SignupResult> signupResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    SignupViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<SignupFormState> getSignupFormState() {
        return signupFormState;
    }

    LiveData<SignupResult> getSignupResult() {
        return signupResult;
    }

    public void signup(String username, String password, String confirm) {
        // can be launched in a separate asynchronous job
        // threads, lesgooo

        //TODO: try catch etc to push to database
        //if it failed return signup failed
        //placeholder if else parameter - use isConfirmValid etc
        if (!password.equals(confirm)) {
            //push to login database
            //insert further variables later
            signupResult.setValue(new SignupResult(R.string.signup_failed));
        }
        //TODO: write about what happens if it succeeds
    }

        //        Result<LoggedInUser> result = loginRepository.login(username, password);
        //
        //        if (result instanceof Result.Success) {
        //            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
        //            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        //        } else {
        //            loginResult.setValue(new LoginResult(R.string.login_failed));
        //        }
//    }

    //add to this code to adapt for signup

    //sets the form state errors
    //without needing to communicate with SignupActivity
    public void signupDataChanged(String username, String password, String confirm) {
        if (!isUserNameValid(username)) {
            //signupformstate should accept three arguments, usernameError, PasswordError, and confirmPasswordError
            //SignupFormState accepts integers because string ids are...integers...
            //that's why (getResource.)getString translates id to string
            signupFormState.setValue(new SignupFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            signupFormState.setValue(new SignupFormState(null, R.string.invalid_password, null));
        } else if (!isConfirmValid(password, confirm)){
            signupFormState.setValue(new SignupFormState(null, null, R.string.invalid_confirm));
        }
        else {
            //if all data isValid
            //setValue is generated by android studio
            signupFormState.setValue(new SignupFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    // Obviously we'll need to connect this to our online database
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmValid(String password, String confirm) {
        return password.equals(confirm);
    }
}
