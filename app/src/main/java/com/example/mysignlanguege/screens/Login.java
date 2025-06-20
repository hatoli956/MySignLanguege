package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class Login extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private static final String ADMIN_EMAIL = "nadavroki@gmail.com";

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ImageButton btnGoBack;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    public static boolean isAdmin = false;
    public static boolean isEmployer = false;

    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();

        btnGoBack = findViewById(R.id.btnGoBack);

        btnGoBack.setOnClickListener(v -> {
            onBackPressed();
        });


        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        User savedUser = SharedPreferencesUtil.getUser(Login.this);
        if (savedUser != null) {
            etEmail.setText(savedUser.getEmail());
            etPassword.setText(savedUser.getPassword());
        }

        btnLogin.setOnClickListener(v -> handleLogin());


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

                        // Set isAdmin based on the fetched user object
                        isAdmin = user.isAdmin();
                        isEmployer=user.isEmployer();

                        // Save user to SharedPreferences
                        SharedPreferencesUtil.saveUser(Login.this, user);

                        setUserLoggedIn(true);
                        getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("userEmail", email)
                                .apply();

                        // Redirect based on admin status
                        if (isAdmin) {
                            startActivity(new Intent(Login.this, AdminPage.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else if (isEmployer) {
                            startActivity(new Intent(Login.this, EmployerPage.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            startActivity(new Intent(Login.this, AfterLogin.class)
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



            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "התחברות נכשלה", e);
                Toast.makeText(Login.this, "אימייל או סיסמה שגויים", Toast.LENGTH_SHORT).show();
                etPassword.setError("נסה שוב");
                etPassword.requestFocus();
            }


        });
    }}
