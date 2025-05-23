package com.example.mysignlanguege;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.screens.BaseActivity;
import com.example.mysignlanguege.screens.ResetPassword;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class Login extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private static final String ADMIN_EMAIL = "nadavroki@gmail.com";

    private EditText etEmail, etPassword;
    private Button btnLogin, btnForgotPassword;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    public static boolean isAdmin = false;
    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        User savedUser = SharedPreferencesUtil.getUser(Login.this);
        if (savedUser != null) {
            etEmail.setText(savedUser.getEmail());
            etPassword.setText(savedUser.getPassword());
        }

        btnLogin.setOnClickListener(v -> handleLogin());

        btnForgotPassword.setOnClickListener(v -> {
            Intent resetIntent = new Intent(Login.this, ResetPassword.class);
            startActivity(resetIntent);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "אנא הזן אימייל וסיסמה", Toast.LENGTH_SHORT).show();
            return;
        }

        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "התחברות הצליחה, UID: " + uid);

                databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User u) {
                        user = u;

                        // Save user to SharedPreferences
                        SharedPreferencesUtil.saveUser(Login.this, user);

                        // ✅ Save login session and user email
                        setUserLoggedIn(true);
                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("userEmail", email)
                                .apply();

                        // Redirect based on admin status
                        if (email.equals(ADMIN_EMAIL)) {
                            isAdmin = true;
                            startActivity(new Intent(Login.this, com.example.mysignlanguege.AdminPage.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            isAdmin = false;
                            startActivity(new Intent(Login.this, com.example.mysignlanguege.AfterLogin.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }

                        finish();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "שגיאה בקבלת נתוני משתמש", e);
                        Toast.makeText(Login.this, "שגיאה בטעינת פרטי המשתמש", Toast.LENGTH_SHORT).show();
                        authenticationService.signOut();
                        SharedPreferencesUtil.signOutUser(Login.this);
                    }
                });
            }

            public void GoBack(View view) {
                Intent go = new Intent(getApplicationContext(), com.example.mysignlanguege.ShowBusinessForUser.class);
                startActivity(go);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "התחברות נכשלה", e);
                Toast.makeText(Login.this, "אימייל או סיסמה שגויים", Toast.LENGTH_SHORT).show();
                etPassword.setError("נסה שוב");
                etPassword.requestFocus();
            }
        });
    }
}
