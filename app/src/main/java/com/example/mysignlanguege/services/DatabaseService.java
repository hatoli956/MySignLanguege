package com.example.mysignlanguege.services;

import android.util.Log;

import androidx.annotation.Nullable;


import com.example.mysignlanguege.models.Business;
import com.example.mysignlanguege.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see #getInstance()
/// @see FirebaseDatabase

/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    /// @see Log
    private static final String TAG = "DatabaseService";


    /// callback interface for database operations
    /// @param <T> the type of the object to return
    /// @see DatabaseCallback#onCompleted(Object)
    /// @see DatabaseCallback#onFailed(Exception)
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully
        void onCompleted(T object);

        /// called when the operation fails with an exception
        void onFailed(Exception e);
    }

    /// the instance of this class
    /// @see #getInstance()
    private static DatabaseService instance;

    /// the reference to the database
    /// @see DatabaseReference
    /// @see FirebaseDatabase#getReference()
    private final DatabaseReference databaseReference;

    /// use getInstance() to get an instance of this class
    /// @see DatabaseService#getInstance()
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /// get an instance of this class
    /// @return an instance of this class
    /// @see DatabaseService
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }


    // private generic methods to write and read data from the database

    /// write data to the database at a specific path
    /// @param path the path to write the data to
    /// @param data the data to write (can be any object, but must be serializable, i.e. must have a default constructor and all fields must have getters and setters)
    /// @param callback the callback to call when the operation is completed
    /// @return void
    /// @see DatabaseCallback
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
    /// @param path the path to read the data from
    /// @return a DatabaseReference object to read the data from
    /// @see DatabaseReference

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }


    /// get data from the database at a specific path
    /// @param path the path to get the data from
    /// @param clazz the class of the object to return
    /// @param callback the callback to call when the operation is completed
    /// @return void
    /// @see DatabaseCallback
    /// @see Class
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
    /// @param path the path to generate the id for
    /// @return a new id for the object
    /// @see String
    /// @see DatabaseReference#push()

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // end of private methods for reading and writing data

    // public methods to interact with the database

    /// create a new user in the database
    /// @param user the user object to create
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive void
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see User
    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Users/" + user.getId(), user, callback);
    }

    /// create a new business in the database
    /// @param business the business object to create
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive void
    ///             if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Business



    public void createNewBusiness( Business business,  DatabaseCallback<Void> callback) {
        writeData("businesss/" + business.getId(), business, callback);
    }


    /// get a user from the database
    /// @param uid the id of the user to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the user object
    ///             if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see User
    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("Users/" + uid, User.class, callback);
    }



    /// get a business from the database
    /// @param businessId the id of the business to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the business object
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Business
    public void getBusiness(@NotNull final String businessId, @NotNull final DatabaseCallback<Business> callback) {
        getData("businesss/" + businessId, Business.class, callback);
    }


    /// generate a new id for a new business in the database
    /// @return a new id for the business
    /// @see #generateNewId(String)
    /// @see Business
    public String generateBusinessId() {
        return generateNewId("businesss");
    }

    /// get all the businesss from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of business objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    /// @see Business
    /// @see #getData(String, Class, DatabaseCallback)
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

    /// get all the users from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of business objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    /// @see Business
    /// @see #getData(String, Class, DatabaseCallback)
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


}