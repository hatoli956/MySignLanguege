package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends BaseActivity {

    EditText etEmailReset;
    Button btnSendCode;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etEmailReset = findViewById(R.id.etEmailReset);
        btnSendCode = findViewById(R.id.btnSendCode);

        // Set click listener for the button
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailReset.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send password reset email
                sendPasswordResetEmail(email);
            }
        });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                            Toast.makeText(ResetPassword.this, "Reset email sent successfully!", Toast.LENGTH_SHORT).show();

                            // Redirect to Login activity after sending the reset email
                            Intent intent = new Intent(ResetPassword.this, Login.class);
                            startActivity(intent);
                            finish(); // Finish the ResetPassword activity so the user can't go back to it
                        } else {
                            Log.e("TAG", "Error sending reset email", task.getException());
                            Toast.makeText(ResetPassword.this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
