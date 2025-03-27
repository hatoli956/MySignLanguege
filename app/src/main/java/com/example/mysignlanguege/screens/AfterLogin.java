package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.R;

public class AfterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void goShowBuisness(View view) {

        Intent go = new Intent(getApplicationContext(), ShowBusinessForUser.class);
        startActivity(go);
    }
    public void goIntrested(View view) {

        Intent go = new Intent(getApplicationContext(), InterestedBusinessesActivity.class);
        startActivity(go);
    }
    public void UpdateUser(View view) {

        Intent go = new Intent(getApplicationContext(), UpdateUserDetails.class);
        startActivity(go);
    }
}