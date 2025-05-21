package com.example.mysignlanguege;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class AfterLogin extends BaseActivity {

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login);
        
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

    public void goShowBuisness(View view) {
        checkAuthenticationBeforeAction();
        Intent go = new Intent(getApplicationContext(), ShowBusinessForUser.class);
        startActivity(go);
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