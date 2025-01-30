package com.example.mysignlanguege;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysignlanguege.adapters.UserAdapter;
import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowUsers extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;  // This will hold the list of users you want to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        // Initialize RecyclerView
        RecyclerView = findViewById(R.id.recyclerView);

        // Create or fetch your list of users
        userList = new ArrayList<>();

        // Set the layout manager for RecyclerView
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the UserAdapter
        userAdapter = new UserAdapter(userList);
        RecyclerView.setAdapter(userAdapter);

        // Fetch users from Firebase or any other data source
        fetchUsersFromFirebase();
    }

    private void fetchUsersFromFirebase() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Listen for changes in the "users" node in Firebase
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();  // Clear previous data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);  // Add user to the list
                    }
                }

                userAdapter.notifyDataSetChanged();  // Notify adapter to refresh the RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowUsers.this, "Error fetching users.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
