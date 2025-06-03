package com.example.mysignlanguege.screens;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.adapters.UserAppliedJobsAdapter;
import com.example.mysignlanguege.models.Job;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class UserAppliedJobs extends BaseActivity {

    private RecyclerView recyclerView;
    private UserAppliedJobsAdapter adapter;
    private List<Job> appliedJobsList = new ArrayList<>();
    private DatabaseService databaseService = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_applied_jobs);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(android.graphics.Color.parseColor("#1A237E"));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAppliedJobsAdapter(this, appliedJobsList, new UserAppliedJobsAdapter.OnJobRemoveListener() {
            @Override
            public void onRemoveJob(Job job, int position) {
                removeJob(job, position);
            }
        });

        recyclerView.setAdapter(adapter);

        loadAppliedJobs();
    }

    private void loadAppliedJobs() {
        if (Login.user == null) {
            Toast.makeText(this, "אנא התחבר קודם כל", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = Login.user.getId();

        databaseService.getAppliedJobsForUser(userId, new DatabaseService.DatabaseCallback<List<Job>>() {
            @Override
            public void onCompleted(List<Job> jobs) {
                appliedJobsList.clear();
                appliedJobsList.addAll(jobs);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(UserAppliedJobs.this, "שגיאה בטעינת המשרות שהוגשו", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeJob(Job job, int position) {
        if (Login.user == null) {
            Toast.makeText(this, "אנא התחבר קודם כל", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = Login.user.getId();

        databaseService.removeAppliedJobForUser(userId, job.getId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                appliedJobsList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(UserAppliedJobs.this, "המשרה הוסרה מהרשימה", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(UserAppliedJobs.this, "שגיאה בהסרת המשרה", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
