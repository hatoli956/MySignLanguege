package com.example.mysignlanguege;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends BaseActivity {

    private EditText fName, lName, email, phone, password;
    private Button btnSave;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        btnSave = findViewById(R.id.btnSave);

        userId = getIntent().getStringExtra("USER_ID");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        fetchUserDetails();

        btnSave.setOnClickListener(v -> saveUserDetails());
    }

    private void fetchUserDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    fName.setText(user.getfName());
                    lName.setText(user.getlName());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhone());
                    password.setText(user.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDetailsActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserDetails() {
        String firstName = fName.getText().toString().trim();
        String lastName = lName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPhone) ||
                TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User(userId, firstName, lastName, userEmail, userPhone, userPassword);

        databaseReference.setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserDetailsActivity.this, "השינויים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserDetailsActivity.this, "שגיאה בשמירת השינויים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
