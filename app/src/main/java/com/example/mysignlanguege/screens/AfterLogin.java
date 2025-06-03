package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class AfterLogin extends BaseActivity {

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        // Initialize authentication service
        authenticationService = AuthenticationService.getInstance();
        
        // Check if user is authenticated
        if (!authenticationService.isUserSignedIn() || !SharedPreferencesUtil.isUserLoggedIn(this)) {
            // If not authenticated, redirect to login
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkAuthenticationBeforeAction() {
        if (!authenticationService.isUserSignedIn() || !SharedPreferencesUtil.isUserLoggedIn(this)) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
            finish();
            return;
        }
    }

    public void goToBusinessList(View view) {
        Intent intent = new Intent(this, BusinessJobApplication.class);
        startActivity(intent);
    }


    public void goShowBuisness(View view) {
        checkAuthenticationBeforeAction();
        Intent go = new Intent(getApplicationContext(), ShowBusinessForUser.class);
        startActivity(go);
    }

    public void goToUserAppliedJobs(View view) {
        Intent intent = new Intent(this, UserAppliedJobs.class);
        startActivity(intent);
    }

    public void goIntrested(View view) {
        checkAuthenticationBeforeAction();
        Intent go = new Intent(getApplicationContext(), InterestedBusinessesActivity.class);
        startActivity(go);
    }

    public void UpdateUser(View view) {
        checkAuthenticationBeforeAction();
        Intent go = new Intent(getApplicationContext(), UpdateUserDetails.class);
        startActivity(go);
    }
}