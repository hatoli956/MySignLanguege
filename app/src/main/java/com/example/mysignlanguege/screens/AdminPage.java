package com.example.mysignlanguege;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.screens.BaseActivity;
import com.example.mysignlanguege.screens.MainActivity;

public class AdminPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }


    public void AddBusiness(View view) {
            Intent go = new Intent(getApplicationContext(), com.example.mysignlanguege.AddBusiness.class);
            startActivity(go);
    }
    public void MainPage(View view) {
        Intent go = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(go);

    }
    public void goAllBusiness(View view) {
        Intent go = new Intent(getApplicationContext(), com.example.mysignlanguege.AllBusiness.class);
        startActivity(go);
    }
    public void SearchUser(View view) {
        Intent go = new Intent(getApplicationContext(), com.example.mysignlanguege.ShowUsers.class);
        startActivity(go);
    }

}