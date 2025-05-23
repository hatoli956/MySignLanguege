package com.example.mysignlanguege;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.adapters.BusinessUserAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.screens.BaseActivity;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class  InterestedBusinessesActivity extends BaseActivity implements BusinessUserAdapter.OnBusinessRemoveListener {

    private RecyclerView recyclerView;
    private BusinessUserAdapter businessUserAdapter;
    private List<Business> interestedBusinessList;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_businesses);

        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerViewInterested);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        interestedBusinessList = new ArrayList<>();
        businessUserAdapter = new BusinessUserAdapter(interestedBusinessList, this);
        businessUserAdapter.setOnBusinessRemoveListener(this);
        recyclerView.setAdapter(businessUserAdapter);

        loadInterestedBusinesses();
    }

    @Override
    public void onBusinessRemove(@NonNull Business business) {
        if (com.example.mysignlanguege.Login.user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = com.example.mysignlanguege.Login.user.getId();
        String businessId = business.getId();

        Toast.makeText(this, "Removing business...", Toast.LENGTH_SHORT).show();

        databaseService.writeData("Users/" + userId + "/interestedBusinesses/" + businessId, null, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            @MainThread
            public void onCompleted(Void object) {
                interestedBusinessList.remove(business);
                businessUserAdapter.notifyDataSetChanged();
                updateInterestedBusinessesPreferences();
                Toast.makeText(InterestedBusinessesActivity.this, "Business removed from interests", Toast.LENGTH_SHORT).show();
            }

            @Override
            @MainThread
            public void onFailed(Exception e) {
                Toast.makeText(InterestedBusinessesActivity.this,
                        "Failed to remove business: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateInterestedBusinessesPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        // שמירת מזהי העסקים כטקסט אחד מופרד בפסיקים
        String updatedBusinesses = interestedBusinessList.stream()
                .map(Business::getId)
                .collect(Collectors.joining(","));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("interestedBusinesses", updatedBusinesses);
        editor.apply();
    }

    private void loadInterestedBusinesses() {
        if (com.example.mysignlanguege.Login.user == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        interestedBusinessList.clear();
        businessUserAdapter.notifyDataSetChanged();

        String path = "Users/" + com.example.mysignlanguege.Login.user.getId() + "/interestedBusinesses";

        databaseService.getDataAtPath(path, new DatabaseService.DatabaseCallback<Map<String, Object>>() {
            @Override
            @MainThread
            public void onCompleted(Map<String, Object> data) {
                if (data != null && !data.isEmpty()) {
                    List<String> businessIds = new ArrayList<>(data.keySet());

                    for (String businessId : businessIds) {
                        databaseService.getBusiness(businessId, new DatabaseService.DatabaseCallback<Business>() {
                            @Override
                            @MainThread
                            public void onCompleted(Business business) {
                                if (business != null) {
                                    interestedBusinessList.add(business);
                                    businessUserAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            @MainThread
                            public void onFailed(Exception e) {
                                // שים לב להוסיף טיפול בטעויות בעתיד, למשל Log.e
                            }
                        });
                    }
                } else {
                    Toast.makeText(InterestedBusinessesActivity.this,
                            "You have no interested businesses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            @MainThread
            public void onFailed(Exception e) {
                Toast.makeText(InterestedBusinessesActivity.this,
                        "Failed to load interested businesses", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
