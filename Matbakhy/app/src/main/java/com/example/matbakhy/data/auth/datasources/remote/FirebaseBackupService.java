package com.example.matbakhy.data.auth.datasources.remote;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.matbakhy.data.Meals.model.FirebaseMeal;
import com.example.matbakhy.data.Meals.model.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseBackupService {
    private static final String TAG = "FirebaseBackupService";
    private static final String BACKUP_PATH = "user_meals_backup";

    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseReference;

    public FirebaseBackupService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void backupAllMeals(List<Meal> meals, BackupCallback callback) {
        if (firebaseAuth.getCurrentUser() == null) {
            callback.onError("User not authenticated");
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();
        String userEmail = firebaseAuth.getCurrentUser().getEmail();

        Log.d(TAG, "Starting backup for user: " + userId);
        Log.d(TAG, "Number of meals to backup: " + (meals != null ? meals.size() : 0));

        if (meals == null || meals.isEmpty()) {
            callback.onSuccess(0, "No meals to backup");
            return;
        }

        DatabaseReference userBackupRef = databaseReference
                .child(BACKUP_PATH)
                .child(userId)
                .child("meals");

        userBackupRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "✅ Old meals deleted successfully");

                    saveNewMeals(meals, userId, userEmail, userBackupRef, callback);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Failed to delete old meals: " + e.getMessage());
                    callback.onError("Failed to clear old data: " + e.getMessage());
                });
    }

    private void saveNewMeals(List<Meal> meals, String userId, String userEmail,
                              DatabaseReference userBackupRef, BackupCallback callback) {
        Map<String, Object> batchUpdate = new HashMap<>();

        for (Meal meal : meals) {
            String mealId = meal.getId() != null ? meal.getId() : "meal_" + System.currentTimeMillis();
            FirebaseMeal firebaseMeal = new FirebaseMeal(meal, userId, userEmail);
            batchUpdate.put(mealId, firebaseMeal);
            Log.d(TAG, "Preparing meal for backup: " + meal.getName() + " (ID: " + mealId + ")");
        }

        Log.d(TAG, "Saving " + batchUpdate.size() + " meals to Firebase...");

        userBackupRef.updateChildren(batchUpdate)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "✅ Successfully backed up " + meals.size() + " meals");
                    callback.onSuccess(meals.size(), "Backup completed successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Backup failed: " + e.getMessage(), e);
                    callback.onError("Backup failed: " + e.getMessage());
                });
    }
    public interface BackupCallback {
        void onSuccess(int backedUpCount, String message);
        void onError(String errorMessage);
    }
    public void restoreMealsFromFirebase(RestoreCallback callback) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            callback.onError("User not authenticated");
            return;
        }

        String userId = currentUser.getUid();
        Log.d(TAG, "Restoring meals for user: " + userId);

        DatabaseReference userBackupRef = databaseReference
                .child(BACKUP_PATH)
                .child(userId)
                .child("meals");

        userBackupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.d(TAG, "No backup found for user");
                    callback.onSuccess(0,new ArrayList<>(),"No backup found");
                    return;
                }

                List<Meal> restoredMeals = new ArrayList<>();
                int count = 0;

                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
                    try {
                        Meal meal = convertSnapshotToMeal(mealSnapshot);
                        if (meal != null) {
                            restoredMeals.add(meal);
                            count++;
                            Log.d(TAG, "Restored meal: " + meal.getName());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing meal: " + e.getMessage());
                    }
                }

                Log.d(TAG, "Successfully restored " + count + " meals");
                callback.onSuccess(count, restoredMeals, "Restored " + count + " meals");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Restore cancelled: " + error.getMessage());
                callback.onError("Restore failed: " + error.getMessage());
            }
        });
    }

    private Meal convertSnapshotToMeal(DataSnapshot snapshot) {
        try {
            Meal meal = new Meal();

            meal.setId(snapshot.child("id").getValue(String.class));
            meal.setName(snapshot.child("name").getValue(String.class));
            meal.setThumbnail(snapshot.child("thumbnail").getValue(String.class));
            meal.setCategory(snapshot.child("category").getValue(String.class));
            meal.setArea(snapshot.child("area").getValue(String.class));
            meal.setInstructions(snapshot.child("instructions").getValue(String.class));

            Boolean isFavorite = snapshot.child("is_favorite").getValue(Boolean.class);
            if (isFavorite != null) {
                meal.setFavorite(isFavorite);
            } else {
                meal.setFavorite(true); // Default to favorite when restoring
            }

            // المكونات والكميات
            for (int i = 1; i <= 20; i++) {
                String ingredient = snapshot.child("ingredient_" + i).getValue(String.class);
                String measure = snapshot.child("measure_" + i).getValue(String.class);

                if (ingredient != null && !ingredient.isEmpty()) {
                    setIngredient(meal, i, ingredient);
                }
                if (measure != null && !measure.isEmpty()) {
                    setMeasure(meal, i, measure);
                }
            }

            return meal;
        } catch (Exception e) {
            Log.e(TAG, "Error converting snapshot to meal", e);
            return null;
        }
    }

    private void setIngredient(Meal meal, int index, String value) {
        try {
            Method method = meal.getClass().getMethod("setIngredient" + index, String.class);
            method.invoke(meal, value);
        } catch (Exception e) {
            Log.e(TAG, "Error setting ingredient " + index, e);
        }
    }

    private void setMeasure(Meal meal, int index, String value) {
        try {
            Method method = meal.getClass().getMethod("setMeasure" + index, String.class);
            method.invoke(meal, value);
        } catch (Exception e) {
            Log.e(TAG, "Error setting measure " + index, e);
        }
    }

    public interface RestoreCallback {
        void onSuccess(int restoredCount, List<Meal> meals, String message);
        void onError(String errorMessage);
    }
}