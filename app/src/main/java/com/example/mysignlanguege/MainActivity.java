package com.example.mysignlanguege;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Button loginButton, registerButton, adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Your layout with DrawerLayout and Toolbar

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        loginButton = findViewById(R.id.button);
        registerButton = findViewById(R.id.button2);
        adminButton = findViewById(R.id.button3);

        // Setup toolbar and drawer toggle
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Setup buttons
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Login.class));
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Register.class));
        });

        adminButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminPage.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();  // Refresh toolbar menu when returning from login/logout
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main) {
            Toast.makeText(this, "Main Activity", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_login) {
            startActivity(new Intent(this, Login.class));
        } else if (id == R.id.menu_logout) {
            setUserLoggedIn(false);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
