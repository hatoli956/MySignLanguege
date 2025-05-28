package com.example.mysignlanguege.screens;

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
import com.example.mysignlanguege.adapters.JobAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.Job;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class JobApplication extends BaseActivity {

    private RecyclerView recyclerView;
    private JobAdapter adapter;
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

        adapter = new JobAdapter(this, jobList, null); // null כי אין צורך במחיקה כאן
        recyclerView.setAdapter(adapter);

        selectedBusiness = (Business) getIntent().getSerializableExtra("business");

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
