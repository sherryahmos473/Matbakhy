package com.example.matbakhy.data.datasources.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.matbakhy.data.model.User;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class SharedPrefServices {
    private static final String TAG = "SharedPrefServices";
    private static final String PREF_NAME = "MatbakhyPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_GUEST = "isGuest";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ID = "userId";

    private static  SharedPrefServices instance;
    private final SharedPreferences pref;

    private SharedPrefServices(Context context) {
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

    public Completable loginAsGuestRx() {
        return Completable.fromAction(() -> {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(KEY_IS_GUEST, true);
                    editor.putBoolean(KEY_IS_LOGGED_IN, false);
                    editor.apply();
                    Log.d(TAG, "User logged in as guest");
                })
                .doOnError(error ->
                        Log.e(TAG, "Failed to login as guest: " + error.getMessage()));
    }
    public boolean isGuest() {
        return pref.getBoolean(KEY_IS_GUEST, false);
    }


    public void saveUserLoginSync(String email, String name, String userId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_ID, userId);
        editor.putBoolean(KEY_IS_GUEST, false);
        editor.apply();
    }

    public void clearUserDataSync() {
        pref.edit().clear().apply();
    }

    public boolean isLoggedInSync() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserEmailSync() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getUserNameSync() {
        return pref.getString(KEY_USER_NAME, "");
    }
}