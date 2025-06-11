package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds
    private ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the logo ImageView
        logoImageView = findViewById(R.id.splashLogo);

        // Set initial visibility to invisible
        logoImageView.setAlpha(0f);

        // Start the fade-in animation
        startLogoFadeIn();

        // Start a new thread to wait
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Start MainActivity on UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close SplashActivity
                    }
                });
            }
        }).start();
    }

    private void startLogoFadeIn() {
        logoImageView.animate()
                .alpha(1f)
                .setDuration(2000)
                .setStartDelay(500)
                .start();
    }
}
