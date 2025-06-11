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

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;

public class EmployerPage extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // מצב Edge-to-Edge

        setContentView(R.layout.activity_employer_page);

        // צבע של Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // טיפול ב-Insets רק על container (לא על toolbar!)
        View container = findViewById(R.id.container);
        ViewCompat.setOnApplyWindowInsetsListener(container, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // פעולה עבור כפתור "הוסף עסק"
    public void goShowBusiness(View view) {
        startActivity(new Intent(this, AddBusiness.class));
    }

    // פעולה עבור כפתור "הוסף עבודה"
    public void goAddJob(View view) {
        startActivity(new Intent(this, AddJob.class));
    }

    // פעולה עבור כפתור "עדכון העסקים שלי"
    public void goIntrested(View view) {
        startActivity(new Intent(this, MyBusinessesList.class));
    }

    // פעולה עבור כפתור "לאיזור האישי"
    public void UpdateUser(View view) {
        startActivity(new Intent(this, UpdateUserDetails.class));
    }

    // פעולה עבור כפתור "רשימת העסקים שלי"
    public void goShowJobsForEmployer(View view) {
        startActivity(new Intent(this, ShowJobsForEmployer.class));
    }
}
