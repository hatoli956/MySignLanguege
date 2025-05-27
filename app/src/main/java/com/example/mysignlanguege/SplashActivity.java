package com.example.mysignlanguege;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.screens.MainActivity;

public class SplashActivity extends AppCompatActivity {

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
