    package com.example.mysignlanguege.screens;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.mysignlanguege.R;
    import com.example.mysignlanguege.adapters.BusinessUserAdapter;
    import com.example.mysignlanguege.models.Business;

    import java.util.ArrayList;
    import java.util.List;

    public class InterestedBusinessesActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private BusinessUserAdapter businessUserAdapter;
        private List<Business> interestedBusinessList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_interested_businesses);

            recyclerView = findViewById(R.id.recyclerViewInterested);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            interestedBusinessList = new ArrayList<>();
            loadInterestedBusinesses();

            businessUserAdapter = new BusinessUserAdapter(interestedBusinessList, this);
            recyclerView.setAdapter(businessUserAdapter);
        }


        public void btnGoBack1(View view) {
            Intent go = new Intent(getApplicationContext(), AfterLogin.class);
            startActivity(go);
        }

        private void loadInterestedBusinesses() {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            String interestedBusinesses = sharedPreferences.getString("interestedBusinesses", "");

            String[] businessIds = interestedBusinesses.split(",");
            for (String id : businessIds) {
                if (!id.isEmpty()) {
                    Business business = getBusinessById(id); // Fetch business from local DB or API
                    interestedBusinessList.add(business);
                }
            }

            if (interestedBusinessList.isEmpty()) {
                Toast.makeText(this, "No interested businesses found.", Toast.LENGTH_SHORT).show();
            }
        }


        private Business getBusinessById(String id) {
            // In a real app, you'd fetch the business data from a database or API.

            // Return a new Business instance with all parameters correctly populated
            return new Business(
                    id,                               // Business ID
                    "Sample Business",                // Name
                    "Category",                       // Category
                    "123-456-7890",                   // Phone
                    "business@example.com",           // Email
                    "123 Sample St",                  // Street
                    "http://www.samplebusiness.com",  // Website
                    "Sample City",                    // City
                    "Sample business details",        // Details
                    "sampleBase64ImageString"         // Image (this should be a base64 string)
            );
        }

    }