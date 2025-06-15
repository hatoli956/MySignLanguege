package com.example.mysignlanguege.screens;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.EmployerRequest;
import com.example.mysignlanguege.models.User;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestBecomeEmployer extends AppCompatActivity {

    private Button btnSendRequest;
    private EditText reasonEditText;
    private DatabaseReference databaseRef;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_become_employer);

        btnSendRequest = findViewById(R.id.btnSendRequest);
        reasonEditText = findViewById(R.id.reasonEditText); // חיבור לשדה הסיבה
        databaseRef = FirebaseDatabase.getInstance().getReference("employer_requests");

        currentUser = SharedPreferencesUtil.getUser(this);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
    }

    private void sendRequest() {
        if (currentUser == null) {
            Toast.makeText(this, "שגיאה: אין משתמש מחובר", Toast.LENGTH_SHORT).show();
            return;
        }

        String reason = reasonEditText.getText().toString().trim();
        if (reason.isEmpty()) {
            Toast.makeText(this, "נא להסביר מדוע אתה רוצה להפוך למעסיק", Toast.LENGTH_SHORT).show();
            return;
        }

        String requestId = databaseRef.push().getKey();

        EmployerRequest request = new EmployerRequest(
                currentUser.getId(),
                currentUser.getfName() + " " + currentUser.getlName(),
                reason
        );


        databaseRef.child(requestId).setValue(request);
        Toast.makeText(this, "הבקשה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
        finish();
    }


}
