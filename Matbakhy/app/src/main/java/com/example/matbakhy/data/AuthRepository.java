package com.example.matbakhy.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;


import com.example.matbakhy.data.callbacks.AuthCallback;
import com.example.matbakhy.data.callbacks.BackupCallback;
import com.example.matbakhy.data.callbacks.LogoutCallback;
import com.example.matbakhy.data.callbacks.SimpleCallback;
import com.example.matbakhy.data.datasources.local.SharedPref;
import com.example.matbakhy.data.datasources.local.SharedPrefServices;
import com.example.matbakhy.data.datasources.remote.AuthNetwork;
import com.example.matbakhy.data.datasources.remote.FirebaseBackupService;
import com.example.matbakhy.data.datasources.remote.FirebaseServices;
import com.example.matbakhy.data.datasources.remote.MealBackupManager;
import com.example.matbakhy.data.datasources.remote.MealRestoreManager;
import com.example.matbakhy.data.datasources.remote.Network;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.model.User;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private final FirebaseServices firebaseServices;
    private final FirebaseBackupService firebaseBackupService;
    private  final SharedPref sharedPref;
    private final MealRepository mealRepository;
    Context context;

    public AuthRepository(Context context) {
        this.context = context;
        this.firebaseServices = Network.getInstance().firebaseServices;
        this.mealRepository = new MealRepository(context);
        this.firebaseServices.initialize(context);
        this.sharedPref = SharedPrefServices.getInstance(context);
        this.firebaseBackupService = new FirebaseBackupService();
    }
    public void loginAsGuest(SimpleCallback callback){
        sharedPref.loginAsGuest(callback);
    }
    public boolean isGuest(){
        return sharedPref.isGuest();
    }
    public void register(String email, String password, String name, AuthCallback callback) {
        firebaseServices.register(email, password, name, callback);
    }

    public void login(String email, String password, AuthCallback callback) {
        firebaseServices.login(email, password, callback);
    }

    public Intent getGoogleSignInIntent() {
        return firebaseServices.getGoogleSignInIntent();
    }

    public void handleGoogleSignInResult(Intent data, AuthCallback callback) {
        firebaseServices.handleGoogleSignInResult(data, callback);
    }

    public void sendPasswordResetEmail(String email, SimpleCallback callback) {
        firebaseServices.sendPasswordResetEmail(email, callback);
    }

    public void logout() {
        firebaseServices.logout();
    }

    public boolean isUserLoggedIn() {
        return firebaseServices.isUserLoggedIn();
    }

    public String getCurrentUserEmail() {
        return firebaseServices.getCurrentUserEmail();
    }

    public String getCurrentUserName() {
        return firebaseServices.getCurrentUserName();
    }

    public boolean isNetworkAvailable() {
        return firebaseServices.isNetworkAvailable();
    }
    public void logoutWithBackup(LogoutCallback callback) {
        backupUserData(new BackupCallback() {
            @Override
            public void onSuccess(int backedUpCount, String message) {
                firebaseServices.logout();
                mealRepository.clearAllLocalMeals();
                callback.onLogoutComplete(true, message);
            }

            @Override
            public void onError(String errorMessage) {
                firebaseServices.logout();
                callback.onLogoutComplete(false, "Logged out but backup failed: " + errorMessage);
            }
        });
    }

    private void backupUserData(BackupCallback callback) {
        mealRepository.getAllMealsFromLocal().subscribeOn(Schedulers.io()).subscribe(
                meals -> firebaseBackupService.backupAllMeals(meals));
    }

    public void loginWithRestore(String email, String password, AuthCallback callback) {
        Log.d(TAG, "Logging in with restore");

        firebaseServices.login(email, password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "Login successful for user: " + user.getEmail());

                restoreUserData(new RestoreCallback() {
                    @Override
                    public void onRestoreComplete(boolean success, int restoredCount, String message) {
                        if (success && restoredCount > 0) {
                            Log.d(TAG, "Restored " + restoredCount + " favorite meals");
                        }
                        callback.onSuccess(user);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }
    public void handleGoogleSignInWithRestore(Intent data, AuthCallback callback) {
        Log.d(TAG, "Handling Google sign in with restore");

        firebaseServices.handleGoogleSignInResult(data, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "Google sign in successful for: " + user.getEmail());
                restoreUserData(new RestoreCallback() {
                    @Override
                    public void onRestoreComplete(boolean success, int restoredCount, String message) {
                        if (success && restoredCount > 0) {
                            Log.d(TAG, "Restored " + restoredCount + " favorite meals");
                            callback.onSuccess(user);
                        } else {
                            callback.onSuccess(user);
                        }
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Google sign in failed: " + errorMessage);
                callback.onFailure(errorMessage);
            }
        });
    }

    private void restoreUserData(RestoreCallback callback) {
        MealRestoreManager restoreManager = new MealRestoreManager(context);

        restoreManager.restoreMealsFromFirebase(new com.example.matbakhy.data.callbacks.RestoreCallback() {
            @Override
            public void onSuccess(int restoredCount, List<Meal> meals, String message) {
                if (meals != null && !meals.isEmpty()) {
                    saveMealsLocally(meals);
                }
                callback.onRestoreComplete(true, restoredCount, message);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onRestoreComplete(false, 0, errorMessage);
            }
        });
    }

    private void saveMealsLocally(List<Meal> meals) {
        for (Meal meal : meals) {
            mealRepository.insertMeal(meal);
        }
    }

    public interface RestoreCallback {
        void onRestoreComplete(boolean success, int restoredCount, String message);
    }
}