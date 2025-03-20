package com.example.mysignlanguege;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.adapters.BusinessUserAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowBusinessForUser extends AppCompatActivity {

    private static final String TAG = "ShowBusinessForUser";
    private RecyclerView recyclerView;
    private BusinessUserAdapter businessUserAdapter;
    private List<Business> businessList;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_business_for_user);  // Use the correct layout for this page

        // Initialize the database service
        databaseService = DatabaseService.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize the list of businesses
        businessList = new ArrayList<>();

        // Set up RecyclerView with LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the BusinessUserAdapter
        businessUserAdapter = new BusinessUserAdapter(businessList,ShowBusinessForUser.this); // Pass the initialized list
        recyclerView.setAdapter(businessUserAdapter);

        // Fetch businesses from the database
        databaseService.getBusinesss(new DatabaseService.DatabaseCallback<List<Business>>() {
            @Override
            public void onCompleted(List<Business> object) {
                // Clear existing data and add new businesses to the list
                businessList.clear();
                businessList.addAll(object);

                // Notify the adapter that the data has changed
                businessUserAdapter.notifyDataSetChanged();
            }
            public void Intrested(View view) {
                Intent go = new Intent(getApplicationContext(), InterestedBusinessesActivity.class);
                startActivity(go);
            }
            @Override
            public void onFailed(Exception e) {
                // Log the error and show a failure message to the user
                Log.e(TAG, "onFailed: ", e);
                Toast.makeText(ShowBusinessForUser.this, "Failed to load businesses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoBack(View view) {
        Intent go = new Intent(getApplicationContext(), AfterLogin.class);
        startActivity(go);
    }
}
