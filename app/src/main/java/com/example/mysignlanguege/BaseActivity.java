package com.example.mysignlanguege;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
    }

    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem loginItem = menu.findItem(R.id.menu_login);
        MenuItem logoutItem = menu.findItem(R.id.menu_logout);

        boolean isLoggedIn = checkUserLoginStatus();

        loginItem.setVisible(!isLoggedIn);
        logoutItem.setVisible(isLoggedIn);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.menu_login) {
            startActivity(new Intent(this, Login.class));
            return true;
        } else if (id == R.id.menu_logout) {
            Toast.makeText(this, "התנתקת בהצלחה", Toast.LENGTH_SHORT).show();
            // Update login status on logout
            setUserLoggedIn(false);

            // Sign out from authentication (if applicable)
            // authenticationService.signOut(); // if you have this accessible here

            // Go back to MainActivity and clear activity stack
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected boolean checkUserLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }

    protected void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", loggedIn).apply();
    }
}
