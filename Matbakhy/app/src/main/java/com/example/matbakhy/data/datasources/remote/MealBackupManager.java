package com.example.matbakhy.data.datasources.remote;

import android.content.Context;
import android.util.Log;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.callbacks.BackupCallback;

import java.util.List;

public class MealBackupManager {
    private static final String TAG = "MealBackupManager";

    private final Context context;
    private final FirebaseBackupService backupService;

    public MealBackupManager(Context context) {
        this.context = context;
        this.backupService = new FirebaseBackupService();
    }

    public void backupMeals(List<Meal> meals, BackupCallback callback) {
        if (meals == null || meals.isEmpty()) {
            callback.onSuccess(0, "No meals to backup");
            return;
        }
        Log.d(TAG, "Starting backup of " + meals.size() + " meals");
        backupService.backupAllMeals(meals, callback);
    }
}