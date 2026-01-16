package com.example.matbakhy;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "MatbakhyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";

    private static SharedPrefManager instance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private SharedPrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUserLogin(String email, String name, String userId) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Get user email
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    // Get user name
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    // Get user ID
    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    // Clear user data (logout)
    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}