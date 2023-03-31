package com.example.aiya_test_3.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.aiya_test_3.Forum;
import com.example.aiya_test_3.R;
//databinding issues: you sync your project -> Files -> sync project with gradle builder
import com.example.aiya_test_3.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private View app_bar;

    private LinearLayout appbarContainer;

    private LoginViewModel loginViewModel;

    //ActivityLoginBinding is autogenerated by the project from activity_login
    //really, it's a way to load in activity_login.xml by inflater
    private ActivityLoginBinding binding;

    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        //setContentView should accept the R.layout.activity_login
        setContentView(binding.getRoot());
        inflater = LayoutInflater.from(this);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        app_bar = inflater.inflate(R.layout.app_bar, null);

        //Implement app_bar container
        // Once everything is instantiated, add the app_bar layout to the container meant for app bar to the MainActivity's layout
        appbarContainer = findViewById(R.id.appbar);
        appbarContainer.addView(app_bar, 0);

        //address by binding.[id]
        //note that this is just disguised R.layout.activity_login.[id of widget]
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final TextView makeAccountText = (TextView) findViewById(R.id.no_account);

        //essentially this loginformstate is an observer
        //and the loginViewModel is the topic
        //at any point in time, the loginformstate can be unsubscribed
        // (e.g. activity gets destroyed due to rotation)
        //and loginViewModel should still hold the data for the next subscriber
        //this saves us from needing to re query our online database
        //aka it's an "offline" cache (for some meaning of offline)
        //https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54 for more info
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {

            //updates every time a new character is typed into the input-able fields
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                //username and password length is at least valid, enable login button
                loginButton.setEnabled(loginFormState.isDataValid());
                //whoops, no username found, show error text message
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                //password doesn't match database, show error text message
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                //login button is pressed, now we can show off our loading bar!
                loadingProgressBar.setVisibility(View.GONE);

                //login failed because of wrong username etc
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }

                //user is successfully logged in, update ui with names etc
                // see somewhere down below for implementation of updateUiWithUser
                //see loggedInUserView to edit datafields that could be shown to the user when it is updated
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            //again, change loginViewModel, modify the topic content
            //instead of directly updating the activity ui (rotation is the bane of activities)
            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        //observers, more observers
        //name should be self explanatory
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            //editor info is built in
            //see google for more info
            //however, this is basically if username and password is finished typed in (e.g. press login button)
            // just login, do the check, update viewmodel,
            // then ui will observe the result of the viewmodel
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        //on click of the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        // the user pressed "New to Aiya? Sign up here!"
        // start the signup page aka "activity"
        makeAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    //user is logged in!
    //you update username, user icon, etc with the user's unique profile
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience aka integrate with Darren work
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, Forum.class);
        startActivity(intent);
    }

    //whoops. failed login.
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}