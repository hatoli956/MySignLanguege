package com.example.mysignlanguege.screens;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.screens.MainActivity;
import com.example.mysignlanguege.screens.UpdatePassword;

public  class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Start a thread to delay for 3 seconds and then move to MainActivity
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000); // Delay for 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Start MainActivity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close SplashActivity so it doesn't remain in backstack
                }
            }
        };

        thread.start();
    }
}