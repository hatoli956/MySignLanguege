package com.example.mysignlanguege;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.adapters.BusinessAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.screens.BaseActivity;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AllBusiness extends BaseActivity implements BusinessAdapter.OnBusinessInteractionListener {

    private static final String TAG = "AllBusinessActivity";
    private RecyclerView recyclerView;
    private BusinessAdapter businessAdapter;
    private List<Business> businessList;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_business);

        databaseService = DatabaseService.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        businessList = new ArrayList<>();
        // העברת context כארגומנט ראשון
        businessAdapter = new BusinessAdapter(this, businessList, this);
        recyclerView.setAdapter(businessAdapter);

        loadBusinesses();
    }
    public void GoBack(View view) {
        Intent go = new Intent(getApplicationContext(), com.example.mysignlanguege.AdminPage.class);
        startActivity(go);
    }

    private void loadBusinesses() {
        databaseService.getBusinesss(new DatabaseService.DatabaseCallback<List<Business>>() {
            @Override
            @MainThread
            public void onCompleted(List<Business> businesses) {
                businessList.clear();
                businessList.addAll(businesses);
                businessAdapter.notifyDataSetChanged();
            }

            @Override
            @MainThread
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load businesses", e);
                Toast.makeText(AllBusiness.this, "Failed to load businesses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteBusinessClicked(Business business) {
        if (business == null) return;

        databaseService.removeBusiness(business.getId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            @MainThread
            public void onCompleted(Void unused) {
                Toast.makeText(AllBusiness.this, "העסק נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                loadBusinesses();
            }

            @Override
            @MainThread
            public void onFailed(Exception e) {
                Toast.makeText(AllBusiness.this, "שגיאה במחיקת העסק: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
