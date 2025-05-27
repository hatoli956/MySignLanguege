package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

public class Register extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText etFName, etLName, etPhone, etEmail, etPassword;
    String fName, lName, phone, email, password, city;
    Button btnReg;
    Spinner spCity;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }

    private void initViews() {
        etFName = findViewById(R.id.etfName);
        etLName = findViewById(R.id.etlName);
        etPhone = findViewById(R.id.etPhone);
        spCity = findViewById(R.id.SpCity);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);
        spCity.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        // Trim input values
        fName = etFName.getText().toString().trim();
        lName = etLName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString();

        boolean isValid = true;

        if (fName.length() < 2) {
            etFName.setError("שם פרטי קצר מדי");
            isValid = false;
        }
        if (lName.length() < 2) {
            etLName.setError("שם משפחה קצר מדי");
            isValid = false;
        }
        if (phone.length() < 9 || phone.length() > 10) {
            etPhone.setError("מספר הטלפון לא תקין");
            isValid = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("כתובת האימייל לא תקינה");
            isValid = false;
        }
        if (password.length() < 6) {
            etPassword.setError("הסיסמה קצרה מדי");
            isValid = false;
        } else if (password.length() > 20) {
            etPassword.setError("הסיסמה ארוכה מדי");
            isValid = false;
        }

        if (isValid) {
            registerUser(email, password, fName, lName, phone);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        city = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void registerUser(String email, String password, String fName, String lName, String phone) {
        Log.d(TAG, "registerUser: Registering user...");

        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User registered successfully");

                User user = new User();
                user.setId(uid);
                user.setEmail(email);
                user.setfName(fName);
                user.setlName(lName);
                user.setPhone(phone);
                user.setAdmin(false);
                user.setEmployer(false); // Change to true if this user is registering as an Employer

                databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        Log.d(TAG, "onCompleted: User data saved successfully");
                        SharedPreferencesUtil.saveUser(Register.this, user);

                        Intent mainIntent = new Intent(Register.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to save user to database", e);
                        Toast.makeText(Register.this, "שגיאה ביצירת המשתמש", Toast.LENGTH_SHORT).show();
                        authenticationService.signOut();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Registration failed", e);
                Toast.makeText(Register.this, "שגיאה בהרשמה", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
