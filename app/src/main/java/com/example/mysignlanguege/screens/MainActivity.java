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
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        if (AuthenticationService.getInstance().isUserSignedIn()) {
            User user = SharedPreferencesUtil.getUser(this);
            if (user.isAdmin()) {
                Intent go = new Intent(getApplicationContext(), AdminPage.class);
                startActivity(go);
            } else {
                Intent go = new Intent(getApplicationContext(), AfterLogin.class);
                startActivity(go);
            }
            finish();
            return;
        }


    }

    public void goReg(View view) {

        Intent go = new Intent(getApplicationContext(), Register.class);
        startActivity(go);
    }
    public void GOadmin(View view) {

        Intent go = new Intent(getApplicationContext(), AdminPage.class);
        startActivity(go);
    }


    public void goLogin(View view) {
        Intent go = new Intent(getApplicationContext(), Login.class);
        startActivity(go);
    }

    public void goAddItem(View view) {
        Intent go = new Intent(getApplicationContext(), AddBusiness.class);
        startActivity(go);
    }

    public void ShowUsers(View view) {
        Intent go = new Intent(getApplicationContext(), ShowUsers.class);
        startActivity(go);
    }

}
