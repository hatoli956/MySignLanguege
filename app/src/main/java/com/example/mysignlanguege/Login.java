package com.example.mysignlanguege;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    // Define Admin credentials
    String AdminEmail = "nadavrok123@gmail.com";
    String AdminPassword = "272053";

    // UI Elements
    EditText etEmail, etPassword;
    Button btnLog, btnForgotPassword;
    String email, pass;

    // Firebase Auth instance
    private FirebaseAuth mAuth;

    // SharedPreferences to store login data
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();

        // Retrieve saved email and password if available
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        email = sharedpreferences.getString("email", "");
        pass = sharedpreferences.getString("password", "");
        etEmail.setText(email);
        etPassword.setText(pass);
    }

    private void initViews() {
        // Initialize views
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLog = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

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

        // Sign in with FirebaseAuth
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in successful
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Save email and password in SharedPreferences
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("email", email);  // Store email
                            editor.putString("password", pass);  // Store password
                            editor.apply();  // Commit changes

                            // Check if the logged-in user is an admin
                            if (email.equals(AdminEmail) && pass.equals(AdminPassword)) {
                                Intent go = new Intent(getApplicationContext(), AdminPage.class);
                                startActivity(go);
                                finish(); // Ensure user can't go back to login screen
                            } else {
                                // Navigate to the main activity for regular users
                                Intent go = new Intent(getApplicationContext(), AfterLogin.class);
                                startActivity(go);
                                finish(); // Ensure user can't go back to login screen
                            }
                        } else {
                            // Sign-in failed
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
