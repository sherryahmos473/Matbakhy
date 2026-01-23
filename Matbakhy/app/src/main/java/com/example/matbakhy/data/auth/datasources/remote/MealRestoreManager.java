package com.example.matbakhy.data.auth.datasources.remote;

import android.content.Context;
import android.util.Log;
import com.example.matbakhy.data.Meals.model.Meal;
import java.util.List;

public class MealRestoreManager {
    private static final String TAG = "MealRestoreManager";

    private final Context context;
    private final FirebaseBackupService backupService;

    public MealRestoreManager(Context context) {
        this.context = context;
        this.backupService = new FirebaseBackupService();
    }

    public void restoreMealsFromFirebase(RestoreCallback callback) {
        Log.d(TAG, "Starting meal restoration from Firebase");

        backupService.restoreMealsFromFirebase(new FirebaseBackupService.RestoreCallback() {
            @Override
            public void onSuccess(int restoredCount, List<Meal> meals, String message) {
                Log.d(TAG, "Restored " + restoredCount + " meals from Firebase");
                callback.onSuccess(restoredCount, meals, message);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Restore failed: " + errorMessage);
                callback.onError(errorMessage);
            }
        });
    }

    public interface RestoreCallback {
        void onSuccess(int restoredCount, List<Meal> meals, String message);
        void onError(String errorMessage);
    }
}