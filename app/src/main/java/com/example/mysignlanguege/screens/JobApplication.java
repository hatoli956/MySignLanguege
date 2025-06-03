package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.adapters.JobApplicationAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.Job;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class JobApplication extends BaseActivity {

    private RecyclerView recyclerView;
    private JobApplicationAdapter adapter;
    private List<Job> jobList = new ArrayList<>();
    private DatabaseService databaseService = DatabaseService.getInstance();
    private Business selectedBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_application);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectedBusiness = (Business) getIntent().getSerializableExtra("business");

        // **Added: set apply listener to adapter**
        adapter = new JobApplicationAdapter(this, jobList, new JobApplicationAdapter.OnJobApplyListener() {
            @Override
            public void onApplyJob(Job job) {
                if (Login.user != null) {
                    // Save job to user's applied jobs in DB
                    databaseService.writeData("Users/" + Login.user.getId() + "/appliedJobs/" + job.getId(),
                            job, new DatabaseService.DatabaseCallback<Void>() {
                                @Override
                                public void onCompleted(Void object) {
                                    Toast.makeText(JobApplication.this, "הגשת מועמדות בוצעה בהצלחה", Toast.LENGTH_SHORT).show();

                                    // Open email client after successful save
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("message/rfc822");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{job.getEmail()});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "הגשת מועמדות למשרה: " + job.getJobTitle());
                                    intent.putExtra(Intent.EXTRA_TEXT, "שלום,\nאני מעוניין להגיש מועמדות למשרה שפרסמתם.");

                                    Intent chooser = Intent.createChooser(intent, "בחר אפליקציית דוא\"ל");
                                    try {
                                        startActivity(chooser);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(JobApplication.this, "לא נמצאה אפליקציית דוא\"ל מותקנת", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailed(Exception e) {
                                    Toast.makeText(JobApplication.this, "הגשת מועמדות נכשלה", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(JobApplication.this, "אנא התחבר קודם כל", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(adapter);

        if (selectedBusiness != null) {
            loadJobsForBusiness(selectedBusiness.getId());
        } else {
            Toast.makeText(this, "שגיאה בטעינת העסק", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadJobsForBusiness(String businessId) {
        databaseService.getJobsForBusiness(businessId, new DatabaseService.DatabaseCallback<List<Job>>() {
            @Override
            public void onCompleted(List<Job> jobs) {
                jobList.clear();
                jobList.addAll(jobs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(JobApplication.this, "שגיאה בטעינת משרות", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
