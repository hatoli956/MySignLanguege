package com.example.mysignlanguege.screens;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestDetailsActivity extends AppCompatActivity {

    private TextView tvFullName, tvReason;
    private Button btnApprove, btnReject;

    private String userId;
    private String fullName;
    private String reason;

    private DatabaseReference usersRef;
    private DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        tvFullName = findViewById(R.id.tvFullName);
        tvReason = findViewById(R.id.tvReason);
        btnApprove = findViewById(R.id.btnApprove);
        btnReject = findViewById(R.id.btnReject);

        userId = getIntent().getStringExtra("userId");
        fullName = getIntent().getStringExtra("fullName");
        reason = getIntent().getStringExtra("reason");

        tvFullName.setText("שם: " + fullName);
        tvReason.setText("סיבה: " + reason);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        requestsRef = FirebaseDatabase.getInstance().getReference("employer_requests");

        btnApprove.setOnClickListener(v -> approveRequest());
        btnReject.setOnClickListener(v -> rejectRequest());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }

    private void approveRequest() {
        usersRef.child(userId).child("employer").setValue(true);
        removeRequest("המשתמש אושר כמעסיק");
    }

    private void rejectRequest() {
        removeRequest("הבקשה נדחתה");
    }

    private void removeRequest(String message) {
        requestsRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().removeValue();
                        }
                        Toast.makeText(RequestDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RequestDetailsActivity.this, "שגיאה בביצוע הפעולה", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
