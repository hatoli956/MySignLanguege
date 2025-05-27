package com.example.mysignlanguege.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.BaseActivity;
import com.example.mysignlanguege.R;
import com.example.mysignlanguege.adapters.BusinessAdapter;
import com.example.mysignlanguege.adapters.BusinessUserAdapter;
import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.services.DatabaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdatePassword extends BaseActivity {

    EditText etNewPassword, etConfirmPassword;
    Button btnUpdatePassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#1A237E"));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        // Set click listener for the button
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdatePassword();
            }
        });
    }

    private void handleUpdatePassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(UpdatePassword.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(UpdatePassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(UpdatePassword.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update password in Firebase
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Password updated successfully");
                                Toast.makeText(UpdatePassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Return to the previous screen
                            } else {
                                Log.e("TAG", "Password update failed", task.getException());
                                Toast.makeText(UpdatePassword.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(UpdatePassword.this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public static class About extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setStatusBarColor(Color.parseColor("#1A237E"));
            }

        }

        public void goBack(View view) {
            finish();
        }
    }

    public static class SplashActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            // Start a thread to delay for 3 seconds and then move to MainActivity
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000); // Delay for 3 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Start MainActivity
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Close SplashActivity so it doesn't remain in backstack
                    }
                }
            };

            thread.start();
        }
    }

    public static class EmployerPage extends AppCompatActivity {
        private Toolbar toolbar;
        private Button btnGoStore2, btnGoMyCart, btnGoPersonalArea;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setStatusBarColor(Color.parseColor("#1A237E"));
            }
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_employer_page);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // קישור לרכיבי ה-XML
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar); // חשוב להשתמש בזה ל-toolbar מסוג AppCompat

            btnGoStore2 = findViewById(R.id.btnAddBusiness);
            btnGoMyCart = findViewById(R.id.btnMyBusiness);
            btnGoPersonalArea = findViewById(R.id.btnGoPersonalArea);

            // אפשר גם להשתמש ב-onClick XML (כפי שעשית), או כאן:
            /*
            btnGoStore2.setOnClickListener(v -> goShowBuisness(v));
            btnGoMyCart.setOnClickListener(v -> goIntrested(v));
            btnGoPersonalArea.setOnClickListener(v -> UpdateUser(v));
            */
        }

        // פעולה עבור כפתור "הוסף עסק"
        public void goShowBuisness(View view) {
            Intent intent = new Intent(this, AddBusiness.class);
            startActivity(intent);
        }

        // פעולה עבור כפתור "רשימה של העסקים שלי"
        public void goIntrested(View view) {
            Intent intent = new Intent(this, MyBusinessesList.class);
            startActivity(intent);
        }

        // פעולה עבור כפתור "לאיזור האישי"
        public void UpdateUser(View view) {
            Intent intent = new Intent(this, UpdateUserDetails.class);
            startActivity(intent);
        }
    }

    public static class MyBusinessesList extends BaseActivity implements BusinessAdapter.OnBusinessInteractionListener {

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

    public static class  InterestedBusinessesActivity extends BaseActivity implements BusinessUserAdapter.OnBusinessRemoveListener {

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
            if (Login.user == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = Login.user.getId();
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
            if (Login.user == null) {
                Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
                return;
            }

            interestedBusinessList.clear();
            businessUserAdapter.notifyDataSetChanged();

            String path = "Users/" + Login.user.getId() + "/interestedBusinesses";

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
}
