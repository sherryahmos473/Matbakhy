package com.example.matbakhy.data.datasources.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.matbakhy.data.model.FirebaseMeal;
import com.example.matbakhy.data.model.Meal;
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

import io.reactivex.Single;

public class FirebaseBackupService {
    private static final String TAG = "FirebaseBackupService";
    private static final String BACKUP_PATH = "user_meals_backup";

    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseReference;

    public FirebaseBackupService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void backupAllMeals(List<Meal> meals) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User not authenticated");
            return;
        }

        DatabaseReference mealsRef = databaseReference
                .child(BACKUP_PATH)
                .child(user.getUid())
                .child("meals");

        Map<String, Object> batch = new HashMap<>();

        for (Meal meal : meals) {
            String mealId = meal.getId() != null
                    ? meal.getId()
                    : mealsRef.push().getKey();

            batch.put(mealId,
                    new FirebaseMeal(meal, user.getUid(), user.getEmail()));
        }

        mealsRef.setValue(batch)
                .addOnSuccessListener(v ->
                        Log.d(TAG, "Backup successful"))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Backup failed", e));
    }

    public Single<List<Meal>> restoreMealsFromFirebase() {
        return Single.create(emitter -> {

            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                emitter.onError(new Exception("User not authenticated"));
                return;
            }

            String userId = currentUser.getUid();
            Log.d(TAG, "restoreMealsFromFirebase: " + userId);

            DatabaseReference userBackupRef = databaseReference
                    .child(BACKUP_PATH)
                    .child(userId)
                    .child("meals");

            userBackupRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        Log.d(TAG, "No backup found for user");
                        emitter.onSuccess(new ArrayList<>());
                        return;
                    }

                    List<Meal> restoredMeals = new ArrayList<>();

                    for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
                        try {
                            Meal meal = convertSnapshotToMeal(mealSnapshot);
                            if (meal != null) {
                                restoredMeals.add(meal);
                                Log.d(TAG,
                                        "Restored meal: " + meal.getName() +
                                                " is favorite " + meal.isFavorite()
                                );
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing meal", e);
                        }
                    }

                    Log.d(TAG, "Successfully restored " + restoredMeals.size() + " meals");
                    emitter.onSuccess(restoredMeals);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
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

            Boolean fav = snapshot.child("is_favorite").getValue(Boolean.class);
            meal.setFavorite(fav != null && fav);

            Boolean planned = snapshot.child("is_planned").getValue(Boolean.class);
            meal.setPlanned(planned != null && planned);
            meal.setPlanDate(snapshot.child("plan_date").getValue(Long.class));

            for (int i = 1; i <= 20; i++) {
                String ing = snapshot.child("ingredient_" + i).getValue(String.class);
                String meas = snapshot.child("measure_" + i).getValue(String.class);

                if (ing != null && !ing.isEmpty()) setIngredient(meal, i, ing);
                if (meas != null && !meas.isEmpty()) setMeasure(meal, i, meas);
            }

            return meal;
        } catch (Exception e) {
            Log.e(TAG, "Meal parse error", e);
            return null;
        }
    }

    public void syncSingleMeal(Meal meal) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "User not authenticated");
            return;
        }
        Log.d(TAG, "Syncing meal - ID: " + meal.getId() +
                ", Name: " + meal.getName() +
                ", Favorite: " + meal.isFavorite() +
                ", Planned: " + meal.isPlanned());

        DatabaseReference mealRef = databaseReference
                .child(BACKUP_PATH)
                .child(user.getUid())
                .child("meals")
                .child(meal.getId());

        if (!meal.isFavorite() && !meal.isPlanned()) {
            Log.d(TAG, "Deleting meal from Firebase (not favorite or planned)");

            mealRef.removeValue()
                    .addOnSuccessListener(v ->
                            Log.d(TAG, "Meal removed from Firebase"))
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Failed to remove meal from Firebase", e));

            return;
        }

        FirebaseMeal firebaseMeal = new FirebaseMeal(meal, user.getUid(), user.getEmail());

        Log.d(TAG, "FirebaseMeal created - Favorite: " + firebaseMeal.isFavorite() +
                ", Planned: " + firebaseMeal.isPlanned());

        mealRef.setValue(firebaseMeal)
                .addOnSuccessListener(v ->
                        Log.d(TAG, "Meal synced to Firebase successfully"))
                .addOnFailureListener(e ->
                        Log.e(TAG, "Failed to sync meal to Firebase", e));
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