package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        setUserLoggedIn(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button2);

        // Setup toolbar
        setSupportActionBar(toolbar);

        // ✅ Hide login button if already logged in
        if (checkUserLoginStatus()) {
            loginButton.setVisibility(View.GONE);
            // Optional: hide register button too
            // registerButton.setVisibility(View.GONE);
        }

        // Set click listeners
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Login.class));
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Register.class));
        });
    }

    public void goShowJobsForEmployer(View view) {
        Intent intent = new Intent(this, JobsList.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();  // Refresh toolbar menu
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_login) {
            startActivity(new Intent(this, Login.class));
        } else if (id == R.id.menu_logout) {
            setUserLoggedIn(false);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            recreate();
        } else if (id == R.id.menu_user_details) {
            startActivity(new Intent(this, UpdateUserDetails.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void GoAbout(View view) {
        Intent go = new Intent(getApplicationContext(),About.class);
        startActivity(go);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}