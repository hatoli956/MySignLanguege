package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class Login extends AppCompatActivity {

    // UI Elements
    EditText etEmail, etPassword;
    Button btnLog, btnForgotPassword;
    String email, pass;

    private static final String TAG = "LoginActivity";

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();

        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();



    }

    private void initViews() {
        // Initialize views
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLog = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        // Set click listeners
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Reset Password page
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });
    }

    private void handleLogin() {
        // Get user input
        email = etEmail.getText().toString().trim();
        pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        authenticationService.signIn(email, pass, new AuthenticationService.AuthCallback<String>() {
            /// Callback method called when the operation is completed
            /// @param uid the user ID of the user that is logged in
            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User logged in successfully");
                /// get the user data from the database
                databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User user) {

                        Log.d(TAG, "onCompleted: User data retrieved successfully");
                        SharedPreferencesUtil.saveUser(Login.this, user);
                        if (user.isAdmin()) {
                            // Admin credentials match, redirect to AdminPage
                            Intent go = new Intent(getApplicationContext(), AdminPage.class);
                            startActivity(go);
                        } else {
                            // Regular user, redirect to AfterLogin activity
                            Intent go = new Intent(getApplicationContext(), AfterLogin.class);
                            startActivity(go);

                        }
                    }
                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                        /// Show error message to user
                        etPassword.setError("Invalid email or password");
                        etPassword.requestFocus();
                        /// Sign out the user if failed to retrieve user data
                        /// This is to prevent the user from being logged in again
                        authenticationService.signOut();

                        }
                    });

            }

            @Override
            public void onFailed(Exception e) {

            }

        });
    }
}

