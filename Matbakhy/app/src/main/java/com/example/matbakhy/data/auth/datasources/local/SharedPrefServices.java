// SharedPrefDataSourceImpl.java
package com.example.matbakhy.data.auth.datasources.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.matbakhy.data.auth.callbacks.SimpleCallback;

public class SharedPrefServices implements SharedPref {
    private static final String PREF_NAME = "MatbakhyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_GUEST = "isGuest";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";

    private final SharedPreferences pref;
    private final Handler mainHandler;

    public SharedPrefServices(Context context) {
        this.pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void saveUserLogin(String email, String name, String userId, SimpleCallback callback) {
        executeAsync(() -> {
            try {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                editor.putString(KEY_USER_EMAIL, email);
                editor.putString(KEY_USER_NAME, name);
                editor.putString(KEY_USER_ID, userId);
                editor.apply();
                notifySuccess(callback, "User saved successfully");
            } catch (Exception e) {
                notifyError(callback, "Failed to save user: " + e.getMessage());
            }
        });
    }


    @Override
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    @Override
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    @Override
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    @Override
    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    @Override
    public void loginAsGuest(SimpleCallback callback) {
        executeAsync(() -> {
            try {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(KEY_IS_GUEST, true);
                editor.apply();
                notifySuccess(callback, "User logged in as a guest");
            } catch (Exception e) {
                notifyError(callback, "Failed to login : " + e.getMessage());
            }
        });
    }

    @Override
    public void clearUserData(SimpleCallback callback) {
        executeAsync(() -> {
            try {
                pref.edit().clear().apply();
                notifySuccess(callback, "User data cleared");
            } catch (Exception e) {
                notifyError(callback, "Failed to clear data: " + e.getMessage());
            }
        });
    }

    @Override
    public boolean isGuest() {
        return pref.getBoolean(KEY_IS_GUEST, false);
    }

    private void executeAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    private void notifySuccess(SimpleCallback callback, String message) {
        mainHandler.post(() -> callback.onSuccess(message));
    }

    private void notifyError(SimpleCallback callback, String error) {
        mainHandler.post(() -> callback.onFailure(error));
    }
}