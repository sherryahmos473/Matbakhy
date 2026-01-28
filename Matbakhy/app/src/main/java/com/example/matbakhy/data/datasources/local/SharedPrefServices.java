package com.example.matbakhy.data.datasources.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.matbakhy.data.callbacks.SimpleCallback;

public class SharedPrefServices implements SharedPref {
    private static final String PREF_NAME = "MatbakhyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_GUEST = "isGuest";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";

    private static volatile SharedPrefServices instance;
    private final SharedPreferences pref;

    public SharedPrefServices(Context context) {
        this.pref = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefServices getInstance(Context context) {
        if (instance == null) {
            synchronized (SharedPrefServices.class) {
                if (instance == null) {
                    instance = new SharedPrefServices(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void saveUserLogin(String email, String name, String userId, SimpleCallback callback) {
        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putString(KEY_USER_EMAIL, email);
            editor.putString(KEY_USER_NAME, name);
            editor.putString(KEY_USER_ID, userId);
            editor.putBoolean(KEY_IS_GUEST, false);
            editor.apply();

            if (callback != null) {
                callback.onSuccess("User saved successfully");
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure("Failed to save user: " + e.getMessage());
            }
        }
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
        try {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(KEY_IS_GUEST, true);
            editor.putBoolean(KEY_IS_LOGGED_IN, false); // Not a logged-in user
            editor.apply();

            if (callback != null) {
                callback.onSuccess("User logged in as a guest");
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure("Failed to login: " + e.getMessage());
            }
        }
    }

    @Override
    public void clearUserData(SimpleCallback callback) {
        try {
            pref.edit().clear().apply();

            if (callback != null) {
                callback.onSuccess("User data cleared");
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onFailure("Failed to clear data: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isGuest() {
        return pref.getBoolean(KEY_IS_GUEST, false);
    }
}