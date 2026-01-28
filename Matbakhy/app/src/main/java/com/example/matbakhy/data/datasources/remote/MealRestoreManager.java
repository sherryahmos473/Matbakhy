package com.example.matbakhy.data.datasources.remote;

import android.content.Context;
import android.util.Log;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.callbacks.RestoreCallback;

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

        backupService.restoreMealsFromFirebase(new RestoreCallback() {
            @Override
            public void onSuccess(int restoredCount, List<Meal> meals, String message) {
                callback.onSuccess(restoredCount, meals, message);
            }
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
}