package com.example.mysignlanguege.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.Job;
import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void getAppliedJobsForUser(String userId, DatabaseCallback<List<Job>> callback) {
        DatabaseReference appliedJobsRef = FirebaseDatabase.getInstance()
                .getReference("Users/" + userId + "/appliedJobs");

        appliedJobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> jobs = new ArrayList<>();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    Job job = jobSnap.getValue(Job.class);
                    if (job != null) {
                        jobs.add(job);
                    }
                }
                callback.onCompleted(jobs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailed(error.toException());
            }
        });
    }
    public void removeAppliedJobForUser(String userId, String jobId, DatabaseCallback<Void> callback) {
        String path = "Users/" + userId + "/appliedJobs/" + jobId;
        deleteData(path, callback);
    }

    public void getBusinessById(String businessId, DatabaseCallback<Business> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("businesses")
                .document(businessId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Business business = documentSnapshot.toObject(Business.class);
                        callback.onCompleted(business);
                    } else {
                        callback.onCompleted(null);
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailed(e);
                });
    }


    /// read data from the database at a specific path
    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }
    public void getDataAtPath(String path, DatabaseCallback<Map<String, Object>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            Map<String, Object> data = (Map<String, Object>) task.getResult().getValue();
            callback.onCompleted(data);
        });
    }

    public void getInterestedBusinesses(String userId, DatabaseCallback<Map<String, Object>> callback) {
        readData("Users/" + userId + "/interestedBusinesses").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            Map<String, Object> data = (Map<String, Object>) task.getResult().getValue();
            callback.onCompleted(data);
        });
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

    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });

            callback.onCompleted(tList);
        });
    }

    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        readData(path).removeValue((error, ref) -> {
            if (error != null) {
                if (callback == null) return;
                callback.onFailed(error.toException());
            } else {
                if (callback == null) return;
                callback.onCompleted(null);
            }
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
        getDataList("businesss", Business.class, callback);
    }

    public void removeBusiness(String businessId, DatabaseCallback<Void> callback) {
        deleteData("businesss/"+ businessId, callback);
    }

    public void getJobsForBusiness(String businessId, DatabaseCallback<List<Job>> callback) {
        DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Jobs");

        jobsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Job> jobs = new ArrayList<>();
                for (DataSnapshot jobSnap : snapshot.getChildren()) {
                    Job job = jobSnap.getValue(Job.class);
                    if (job != null && businessId.equals(job.getBusinessId())) {
                        jobs.add(job);
                    }
                }
                callback.onCompleted(jobs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailed(error.toException());
            }
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
        createNewUser(user, callback);
    }

    public void getBusinesssFromPath(@NotNull final String path, @NotNull final DatabaseCallback<List<Business>> callback) {
        getDataList(path, Business.class, callback);
    }
}
