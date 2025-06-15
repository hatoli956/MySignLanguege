package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;

public class EmployerPage extends BaseActivity {

    private Toolbar toolbar;
    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_employer_page);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup main layout and window insets
        mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Action for "Add Business" button
    public void goShowBusiness(View view) {
        startActivity(new Intent(this, AddBusiness.class));
    }

    // Action for "Add Job" button
    public void goAddJob(View view) {
        startActivity(new Intent(this, AddJob.class));
    }

    // Action for "Update My Businesses" button
    public void goIntrested(View view) {
        startActivity(new Intent(this, MyBusinessesList.class));
    }

    // Action for "Personal Area" button
    public void UpdateUser(View view) {
        startActivity(new Intent(this, UpdateUserDetails.class));
    }

    // Action for "My Businesses List" button
    public void goShowJobsForEmployer(View view) {
        startActivity(new Intent(this, ShowJobsForEmployer.class));
    }
}
