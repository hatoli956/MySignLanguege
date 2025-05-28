package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddJob extends BaseActivity {

    private Spinner businessSpinner;
    private EditText inputJobTitle, inputSalary, inputEmail, inputEmployerName;
    private Button btnSaveJob;

    private List<Business> businessList = new ArrayList<>();
    private String selectedBusinessId = "";
    private String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        businessSpinner = findViewById(R.id.spinner_business);
        inputJobTitle = findViewById(R.id.input_job_title);
        inputSalary = findViewById(R.id.input_salary);
        inputEmail = findViewById(R.id.input_email);
        inputEmployerName = findViewById(R.id.input_employer_name);
        btnSaveJob = findViewById(R.id.btn_save_job);

        loadUserBusinesses();

        btnSaveJob.setOnClickListener(v -> saveJob());
    }

    private void loadUserBusinesses() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(this, "UID מחובר: " + currentUserId, Toast.LENGTH_LONG).show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("businesss");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> businessNames = new ArrayList<>();
                businessList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String ownerId = ds.child("ownerId").getValue(String.class);
                    Toast.makeText(AddJob.this, "ownerId שנמצא: " + ownerId, Toast.LENGTH_SHORT).show();

                    if (ownerId != null && ownerId.trim().equals(currentUserId.trim())) {
                        Business business = ds.getValue(Business.class);
                        if (business != null) {
                            businessList.add(business);
                            businessNames.add(business.getName());
                        }
                    }
                }

                Toast.makeText(AddJob.this, "מספר עסקים: " + businessList.size(), Toast.LENGTH_LONG).show();

                if (businessList.isEmpty()) return;

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddJob.this, android.R.layout.simple_spinner_item, businessNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                businessSpinner.setAdapter(adapter);

                businessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Business selectedBusiness = businessList.get(position);
                        selectedBusinessId = selectedBusiness.getId();
                        selectedCategory = selectedBusiness.getCategory();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddJob.this, "שגיאה בטעינת עסקים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveJob() {
        String jobTitle = inputJobTitle.getText().toString().trim();
        String salary = inputSalary.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String employerName = inputEmployerName.getText().toString().trim();

        if (selectedBusinessId.isEmpty() || jobTitle.isEmpty()) {
            Toast.makeText(this, "יש למלא את כל הפרטים", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");
        String jobId = jobsRef.push().getKey();

        Job job = new Job(selectedBusinessId, jobId, jobTitle, selectedCategory, salary, email, employerName);
        jobsRef.child(jobId).setValue(job)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddJob.this, "המשרה נשמרה", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddJob.this, EmployerPage.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddJob.this, "שגיאה בשמירה", Toast.LENGTH_SHORT).show());
    }
}
