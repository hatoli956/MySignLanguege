package com.example.mysignlanguege.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    private static final String TAG = "DatabaseService";

    /// callback interface for database operations
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully
        void onCompleted(T object);

        /// called when the operation fails with an exception
        void onFailed(Exception e);
    }

    /// the instance of this class
    private static DatabaseService instance;

    /// the reference to the database
    private final DatabaseReference databaseReference;

    /// use getInstance() to get an instance of this class
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /// get an instance of this class
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // private generic methods to write and read data from the database

    /// write data to the database at a specific path
    public void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(task.getResult());
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    public void removeBusiness(String businessId, DatabaseCallback<Void> callback) {
        // Reference to the "businesses" node and the business to be deleted by its ID
        DatabaseReference businessRef = databaseReference.child("businesses").child(businessId);

        // Log the business ID to ensure it's correct
        Log.d("DatabaseService", "Removing business with ID: " + businessId);

        businessRef.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Successfully removed
                        Log.d("DatabaseService", "Business successfully removed from Firebase");
                        callback.onCompleted(null);
                    } else {
                        // Handle failure (log the error)
                        Log.e("DatabaseService", "Error removing business", task.getException());
                        callback.onFailed(task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    // This will catch issues like network failures
                    Log.e("DatabaseService", "Failed to remove business from Firebase", e);
                    callback.onFailed(e);
                });
    }

    /// read data from the database at a specific path
    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    /// get data from the database at a specific path
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    /// generate a new id for a new object in the database
    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // public methods to interact with the database

    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback);
    }

    public void createNewBusiness(Business business, DatabaseCallback<Void> callback) {
        writeData("businesss/" + business.getId(), business, callback);
    }

    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("Users/" + uid, User.class, callback);
    }

    public void getBusiness(@NotNull final String businessId, @NotNull final DatabaseCallback<Business> callback) {
        getData("businesss/" + businessId, Business.class, callback);
    }

    public String generateBusinessId() {
        return generateNewId("businesss");
    }

    public void getBusinesss(@NotNull final DatabaseCallback<List<Business>> callback) {
        readData("businesss").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Business> businesss = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Business business = dataSnapshot.getValue(Business.class);
                Log.d(TAG, "Got business: " + business);
                businesss.add(business);
            });

            callback.onCompleted(businesss);
        });
    }

    public void getUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        readData("Users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<User> users = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Got user: " + user);
                users.add(user);
            });

            callback.onCompleted(users);
        });
    }

    /// Update user details in the database
    public void updateUserDetails(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        // Reference to the "Users" node and the specific user by their ID
        DatabaseReference userRef = databaseReference.child("Users").child(user.getId());

        // Create a map of the updated fields
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("fName", user.getfName());
        userUpdates.put("lName", user.getlName());
        userUpdates.put("phone", user.getPhone());
        userUpdates.put("email", user.getEmail());
        userUpdates.put("password", user.getPassword());

        // Perform the update
        userRef.updateChildren(userUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (callback != null) {
                            callback.onCompleted(null);  // Indicate success
                        }
                        Log.d(TAG, "User details updated successfully");
                    } else {
                        if (callback != null) {
                            callback.onFailed(task.getException());  // Indicate failure
                        }
                        Log.e(TAG, "Error updating user details", task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailed(e);  // Indicate failure
                    }
                    Log.e(TAG, "Failed to update user details", e);
                });
    }
}
