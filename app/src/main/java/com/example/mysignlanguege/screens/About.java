package com.example.mysignlanguege.screens;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;

public class About extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));


    }

    public void goBack(View view) {
        finish();
    }
}