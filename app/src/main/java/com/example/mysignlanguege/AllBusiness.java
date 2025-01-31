package com.example.mysignlanguege;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.adapters.BusinessAdapter;  // Import your BusinessAdapter
import com.example.mysignlanguege.models.Business;  // Import your Business model
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllBusiness extends AppCompatActivity {

    private RecyclerView recyclerView;  // RecyclerView for businesses
    private BusinessAdapter businessAdapter;  // Adapter for displaying businesses
    private List<Business> businessList;  // List to hold businesses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_business);  // Use the correct layout for businesses

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Create a list for the businesses
        businessList = new ArrayList<>();

        // Set the layout manager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the BusinessAdapter
        businessAdapter = new BusinessAdapter(businessList);
        recyclerView.setAdapter(businessAdapter);

        // Fetch businesses from Firebase
        fetchBusinessesFromFirebase();
    }

    private void fetchBusinessesFromFirebase() {
        // Reference to the businesses node in Firebase
        DatabaseReference businessesRef = FirebaseDatabase.getInstance().getReference("businesss");

        // Listen for changes in the "businesses" node
        businessesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                businessList.clear();  // Clear previous data

                // Iterate through the children of "businesses" node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Business business = snapshot.getValue(Business.class);
                    if (business != null) {
                        businessList.add(business);  // Add the business to the list
                    }
                }

                businessAdapter.notifyDataSetChanged();  // Notify adapter to refresh the RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show error if fetching businesses fails
                Toast.makeText(AllBusiness.this, "Error fetching businesses.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
