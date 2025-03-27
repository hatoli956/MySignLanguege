package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView fName, lName, email, phone, password;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Initialize the views
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);

        // Get the user ID passed from ShowUsers activity
        String userId = getIntent().getStringExtra("USER_ID");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Fetch the user details from Firebase
        fetchUserDetails(userId);
    }

    private void fetchUserDetails(String userId) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    fName.setText(user.getfName());
                    lName.setText(user.getlName());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhone());
                    password.setText(user.getPassword());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserDetailsActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoBack(View view) {
        Intent go = new Intent(getApplicationContext(), AdminPage.class);
        startActivity(go);
    }
}
