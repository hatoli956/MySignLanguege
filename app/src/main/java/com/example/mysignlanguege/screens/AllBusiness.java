package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.R;
import com.example.mysignlanguege.adapters.BusinessAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AllBusiness extends AppCompatActivity {

    private static final String TAG ="ReadBusineess" ;
    private RecyclerView recyclerView;  // RecyclerView for businesses
    private BusinessAdapter businessAdapter;  // Adapter for displaying businesses
    private List<Business> businessList;  // List to hold businesses

    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_business);  // Use the correct layout for businesses

          databaseService=DatabaseService.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Create a list for the businesses
        businessList = new ArrayList<>();

;

        // Set the layout manager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the BusinessAdapter
        businessAdapter = new BusinessAdapter(businessList);
        recyclerView.setAdapter(businessAdapter);





        databaseService.getBusinesss(new DatabaseService.DatabaseCallback<List<Business>>() {
            @Override
            public void onCompleted(List<Business> object) {

                businessList.clear();
                businessList.addAll(object);
                /// notify the adapter that the data has changed
                /// this specifies that the data has changed
                /// and the adapter should update the view
                /// @see FoodSpinnerAdapter#notifyDataSetChanged()
                businessAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);


            }
        });




    }


    public void btnGoBack1(View view) {
        Intent go = new Intent(getApplicationContext(), AdminPage.class);
        startActivity(go);
    }
}
