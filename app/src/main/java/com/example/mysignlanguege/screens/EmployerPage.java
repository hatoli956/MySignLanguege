package com.example.mysignlanguege.screens;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.screens.AddBusiness;
import com.example.mysignlanguege.screens.UpdatePassword;
import com.example.mysignlanguege.screens.UpdateUserDetails;

public class EmployerPage extends BaseActivity {
    private Toolbar toolbar;
    private Button btnGoStore2, btnGoMyCart, btnGoPersonalArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employer_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // קישור לרכיבי ה-XML
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // חשוב להשתמש בזה ל-toolbar מסוג AppCompat

        btnGoStore2 = findViewById(R.id.btnAddBusiness);
        btnGoMyCart = findViewById(R.id.btnMyBusiness);
        btnGoPersonalArea = findViewById(R.id.btnGoPersonalArea);

        // אפשר גם להשתמש ב-onClick XML (כפי שעשית), או כאן:
            /*
            btnGoStore2.setOnClickListener(v -> goShowBuisness(v));
            btnGoMyCart.setOnClickListener(v -> goIntrested(v));
            btnGoPersonalArea.setOnClickListener(v -> UpdateUser(v));
            */
    }

    // פעולה עבור כפתור "הוסף עסק"
    public void goShowBusiness(View view) {
        Intent intent = new Intent(this, AddBusiness.class);
        startActivity(intent);
    }

    // פעולה עבור כפתור "רשימה של העסקים שלי"
    public void goIntrested(View view) {
        Intent intent = new Intent(this, MyBusinessesList.class);
        startActivity(intent);
    }

    // פעולה עבור כפתור "לאיזור האישי"
    public void UpdateUser(View view) {
        Intent intent = new Intent(this, UpdateUserDetails.class);
        startActivity(intent);
    }
    public void goAddJob(View view) {
        Intent intent = new Intent(this, AddJob.class);
        startActivity(intent);
    }
    public void goShowJobsForEmployer(View view) {
        Intent intent = new Intent(this, ShowJobsForEmployer.class);
        startActivity(intent);
    }

}