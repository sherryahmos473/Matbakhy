package com.example.matbakhy.data.datasources.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.matbakhy.data.model.FirebaseMeal;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.callbacks.BackupCallback;
import com.example.matbakhy.data.callbacks.RestoreCallback;
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
                    saveNewMeals(meals, userId, userEmail, userBackupRef, callback);
                })
                .addOnFailureListener(e -> {
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
        }


        userBackupRef.updateChildren(batchUpdate)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(meals.size(), "Backup completed successfully");
                })
                .addOnFailureListener(e -> {
                    callback.onError("Backup failed: " + e.getMessage());
                });
    }

    public void restoreMealsFromFirebase(RestoreCallback callback) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            callback.onError("User not authenticated");
            return;
        }

        String userId = currentUser.getUid();

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
                            Log.d(TAG, "Restored meal: " + meal.getName() + "is favorite "+ meal.isFavorite());
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
            meal.setFavorite(snapshot.child("is_favorite").getValue(Boolean.class));
            meal.setPlanned(snapshot.child("is_planned").getValue(Boolean.class));
            meal.setPlanDate(snapshot.child("plan_date").getValue(String.class));


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
}