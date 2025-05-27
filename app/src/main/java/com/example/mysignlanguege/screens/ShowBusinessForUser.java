package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.adapters.BusinessUserAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.AuthenticationService;
import com.example.mysignlanguege.services.DatabaseService;
import com.example.mysignlanguege.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class ShowBusinessForUser extends BaseActivity implements BusinessUserAdapter.OnBusinessInteractionListener {

    private static final String TAG = "ShowBusinessForUser";
    private RecyclerView recyclerView;
    private BusinessUserAdapter businessUserAdapter;
    private List<Business> businessList;
    private DatabaseService databaseService;
    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_for_user);

        // Initialize services
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        // Check if user is authenticated
        if (!authenticationService.isUserSignedIn() || !SharedPreferencesUtil.isUserLoggedIn(this)) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, Login.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize the list of businesses
        businessList = new ArrayList<>();

        // Set up RecyclerView with LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the BusinessUserAdapter with the interaction listener
        businessUserAdapter = new BusinessUserAdapter(businessList, this);
        recyclerView.setAdapter(businessUserAdapter);

        // Fetch businesses from the database
        loadBusinesses();
    }

    public void GoBack(View view) {
        Intent go = new Intent(getApplicationContext(), AfterLogin.class);
        startActivity(go);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check authentication state when activity resumes
        if (!authenticationService.isUserSignedIn() || !SharedPreferencesUtil.isUserLoggedIn(this)) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, Login.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    }

    private void loadBusinesses() {
        databaseService.getBusinesss(new DatabaseService.DatabaseCallback<List<Business>>() {
            @Override
            public void onCompleted(List<Business> businesses) {
                businessList.clear();
                businessList.addAll(businesses);
                businessUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                // Log the error and show a failure message to the user
                Log.e(TAG, "onFailed: ", e);
                Toast.makeText(ShowBusinessForUser.this, "Failed to load businesses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAddToInterestedClicked(Business business) {
        if (Login.user != null && business != null) {
            String userId = Login.user.getId();
            String businessId = business.getId();
            
            String path = "Users/" + userId + "/interestedBusinesses/" + businessId;
            databaseService.writeData(path, business, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(ShowBusinessForUser.this, "נוסף למועדפים בהצלחה", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(ShowBusinessForUser.this, 
                        "שגיאה בהוספה למועדפים: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
        }
    }

    public void Intrested(View view) {
        Intent go = new Intent(getApplicationContext(), UpdatePassword.InterestedBusinessesActivity.class);
        startActivity(go);
    }
}
