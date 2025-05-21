package com.example.mysignlanguege;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class Login extends BaseActivity {

    private static final String TAG = "LoginActivity";

    // מזהי אדמין - לפי אימייל
    private static final String ADMIN_EMAIL = "nadavroki@gmail.com";

    // רכיבי UI
    private EditText etEmail, etPassword;
    private Button btnLogin, btnForgotPassword;

    // שירותים
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    // מצבים
    public static boolean isAdmin = false;
    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        initViews();

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // טעינת נתוני משתמש מזיכרון (אם יש)
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

                        SharedPreferencesUtil.saveUser(Login.this, user);

                        // *** Set login flag to true here ***
                        setUserLoggedIn(true);

                        if (email.equals(ADMIN_EMAIL)) {
                            isAdmin = true;
                            Intent adminIntent = new Intent(Login.this, AdminPage.class);
                            adminIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(adminIntent);
                        } else {
                            isAdmin = false;
                            Intent userIntent = new Intent(Login.this, AfterLogin.class);
                            userIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(userIntent);
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
