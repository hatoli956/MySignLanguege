package com.example.mysignlanguege;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.adapters.BusinessAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.screens.AdminPage;
import com.example.mysignlanguege.screens.UpdateBusiness;
import com.example.mysignlanguege.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MyBusinessesList extends BaseActivity implements BusinessAdapter.OnBusinessInteractionListener {

    private static final String TAG = "MyBusinessesList";

    private RecyclerView recyclerView;
    private TextView tvNoBusinesses;
    private BusinessAdapter businessAdapter;
    private List<Business> businessList;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the correct layout file name here:
        setContentView(R.layout.activity_my_businesses_list);

        databaseService = DatabaseService.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }

        recyclerView = findViewById(R.id.recyclerView);
        tvNoBusinesses = findViewById(R.id.tvNoBusinesses);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        businessList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(this, businessList, this);
        recyclerView.setAdapter(businessAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBusinesses();
    }

    private void loadBusinesses() {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseService.getBusinesss(new DatabaseService.DatabaseCallback<List<Business>>() {
            @Override
            @MainThread
            public void onCompleted(List<Business> businesses) {
                businessList.clear();

                for (Business business : businesses) {
                    if (business.getOwnerId() != null && business.getOwnerId().equals(currentUserId)) {
                        businessList.add(business);
                    }
                }

                businessAdapter.notifyDataSetChanged();

                if (businessList.isEmpty()) {
                    tvNoBusinesses.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvNoBusinesses.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            @MainThread
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load businesses", e);
                Toast.makeText(MyBusinessesList.this, "Failed to load businesses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    @Override
    public void onDeleteBusinessClicked(Business business) {
        if (business == null) return;

        databaseService.removeBusiness(business.getId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            @MainThread
            public void onCompleted(Void unused) {
                Toast.makeText(MyBusinessesList.this, "העסק נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                loadBusinesses();
            }

            @Override
            @MainThread
            public void onFailed(Exception e) {
                Toast.makeText(MyBusinessesList.this, "שגיאה במחיקת העסק: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickListener(Business business) {
        Intent intent = new Intent(MyBusinessesList.this, UpdateBusiness.class);
        intent.putExtra("business", business);  // 'business' must be Serializable
        startActivity(intent);

    }
}
