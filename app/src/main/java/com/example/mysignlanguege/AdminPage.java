package com.example.mysignlanguege;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }


    public void AddBusiness(View view) {
            Intent go = new Intent(getApplicationContext(), AddBusiness.class);
            startActivity(go);
    }
    public void MainPage(View view) {
        Intent go = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(go);

    }
    public void goAllBusiness(View view) {
        Intent go = new Intent(getApplicationContext(), AllBusiness.class);
        startActivity(go);
    }
    public void SearchUser(View view) {
        Intent go = new Intent(getApplicationContext(), ShowUsers.class);
        startActivity(go);
    }

}