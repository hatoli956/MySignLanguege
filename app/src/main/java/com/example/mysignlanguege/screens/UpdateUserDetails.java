package com.example.mysignlanguege.screens;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class UpdateUserDetails extends BaseActivity implements View.OnClickListener {

    EditText etfName, etlName, etPhone, etEmail;
    Button btnUpdate;
    String fName, lName, phone, email;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;
    private static final String TAG = "UpdateUserDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_details);

        initViews();

        // Initialize the services
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        // Load the existing user details from SharedPreferences or Database
        loadUserDetails();
    }

    private void initViews() {
        etfName = findViewById(R.id.etfName);
        etlName = findViewById(R.id.etlName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
    }

    private void loadUserDetails() {
        // Retrieve the saved user from SharedPreferences
        User user = SharedPreferencesUtil.getUser(this);

        // If user data exists, display it
        if (user != null) {
            etfName.setText(user.getfName());
            etlName.setText(user.getlName());
            etPhone.setText(user.getPhone());
            etEmail.setText(user.getEmail());
        } else {
            // Show a message if the user is not found in SharedPreferences
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        // Get the updated values from the EditText fields
        fName = etfName.getText().toString();
        lName = etlName.getText().toString();
        phone = etPhone.getText().toString();
        email = etEmail.getText().toString();

        // Validate the input before proceeding
        boolean isValid = validateInput();

        if (isValid) {
            updateUserDetails(fName, lName, phone, email);
        }
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Simple validation for fields
        if (fName.length() < 2) {
            etfName.setError("First name is too short");
            isValid = false;
        }
        if (lName.length() < 2) {
            etlName.setError("Last name is too short");
            isValid = false;
        }
        if (phone.length() < 9 || phone.length() > 10) {
            etPhone.setError("Phone number is not valid");
            isValid = false;
        }
        if (!email.contains("@")) {
            etEmail.setError("Email is not valid");
            isValid = false;
        }

        return isValid;
    }

    private void updateUserDetails(String fName, String lName, String phone, String email) {
        // Get the current user's ID from Authentication service
        String userId = authenticationService.getCurrentUserId();
        if (userId != null) {
            // Create a new user object with the updated details
            User updatedUser = new User();
            updatedUser.setId(userId);
            updatedUser.setfName(fName);
            updatedUser.setlName(lName);
            updatedUser.setPhone(phone);
            updatedUser.setEmail(email);

            // Call the updateUser method of the Database service to update the user in the database
            databaseService.updateUserDetails(updatedUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    // Save the updated user data to SharedPreferences
                    SharedPreferencesUtil.saveUser(UpdateUserDetails.this, updatedUser);
                    // Inform the user and navigate back or refresh the activity
                    Toast.makeText(UpdateUserDetails.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(UpdateUserDetails.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
