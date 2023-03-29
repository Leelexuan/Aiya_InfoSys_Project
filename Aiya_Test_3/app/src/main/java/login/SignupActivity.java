package login;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.attemptnumerodos.R;
import com.example.attemptnumerodos.databinding.ActivitySignupBinding;

//don't forget that every time you start a new activity
//please update the manifest with that activity declaration
//don't forget to extend AppCompat Activity
public class SignupActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    //note that the below class is automatically generated
    //by the formatting of the xml file name
    //since the associated layout is activity_signup
    //the name should be Activity Signup Binding
    private ActivitySignupBinding binding;
    private LayoutInflater inflater;

    private View app_bar;

    private LinearLayout appbarContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        //setContentView should accept the R.layout.login_activity
        setContentView(R.layout.activity_signup);
        inflater = LayoutInflater.from(this);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        //note that all binding.[id] are generated automatically

        app_bar = inflater.inflate(R.layout.app_bar, null);

        appbarContainer = findViewById(R.id.appbar);
        appbarContainer.addView(app_bar, 0);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final EditText confirmEditText = binding.confirmPassword;
        //no implementation of this yet
        final Button loginButton = binding.signup;
        //final ProgressBar loadingProgressBar = binding.loading;
        //TODO: we don't have a loading bar for signup yet!
//        final TextView makeAccountText = (TextView) findViewById(R.id.no_account);

        //TODO: Note that most of the below code are currently unchanged from loginActivity. See Login Activity for comments.


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
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
//                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
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

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });


    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful signup experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//        //start the login
//        makeAccountText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
