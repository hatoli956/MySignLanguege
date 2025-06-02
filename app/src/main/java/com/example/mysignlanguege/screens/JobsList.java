package com.example.mysignlanguege.screens;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.adapters.JobAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobsList extends BaseActivity {

    private Business selectedBusiness;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<Job> jobList = new ArrayList<>();
    private DatabaseReference jobsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ⬅ קבלת האובייקט המלא של העסק
        selectedBusiness = (Business) getIntent().getSerializableExtra("business");
        if (selectedBusiness == null) {
            Toast.makeText(this, "שגיאה בטעינת העסק", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        jobAdapter = new JobAdapter(this, jobList, job -> {
            FirebaseDatabase.getInstance().getReference("Jobs")
                    .child(job.getId())
                    .removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(JobsList.this, "המשרה נמחקה", Toast.LENGTH_SHORT).show();
                        loadJobs();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(JobsList.this, "שגיאה במחיקה", Toast.LENGTH_SHORT).show());
        });

        recyclerView.setAdapter(jobAdapter);
        loadJobs();
    }

    private void loadJobs() {
        String businessId = selectedBusiness.getId();
        jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");

        jobsRef.orderByChild("businessId").equalTo(businessId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        jobList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Job job = ds.getValue(Job.class);
                            if (job != null) {
                                job.setId(ds.getKey());
                                jobList.add(job);
                            }
                        }
                        jobAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(JobsList.this, "שגיאה בטעינת משרות", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
