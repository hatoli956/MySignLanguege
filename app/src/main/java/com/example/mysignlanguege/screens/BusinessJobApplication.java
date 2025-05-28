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
import com.example.mysignlanguege.adapters.BusinessUserAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class BusinessJobApplication extends BaseActivity {

    private RecyclerView recyclerView;
    private BusinessUserAdapter adapter;
    private List<Business> businessList = new ArrayList<>();
    private DatabaseService databaseService = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_job_application);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ שימוש בפריסה item_business_job
        adapter = new BusinessUserAdapter(businessList, this, R.layout.item_business_job);
        recyclerView.setAdapter(adapter);

        // ✅ ניתן להאזין ללחיצה על עסק (אם רוצים)
        adapter.setOnBusinessClickListener(business -> {
            Intent intent = new Intent(this, JobApplication.class);
            intent.putExtra("business", business);
            startActivity(intent);
        });

        loadBusinesses();
    }

    private void loadBusinesses() {
        databaseService.getBusinesss(new DatabaseService.DatabaseCallback<List<Business>>() {
            @Override
            public void onCompleted(List<Business> businesses) {
                businessList.clear();
                businessList.addAll(businesses);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(BusinessJobApplication.this, "שגיאה בטעינת עסקים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
