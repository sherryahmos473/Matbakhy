package com.example.matbakhy.data.datasources.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.matbakhy.data.callbacks.SimpleCallback;
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

    private static volatile SharedPrefServices instance;
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


    public Completable saveUserLoginRx(String email, String name, String userId) {
        return Completable.fromAction(() -> {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.putString(KEY_USER_EMAIL, email);
                    editor.putString(KEY_USER_NAME, name);
                    editor.putString(KEY_USER_ID, userId);
                    editor.putBoolean(KEY_IS_GUEST, false);
                    editor.apply();
                    Log.d(TAG, "User saved: " + email);
                })
                .subscribeOn(Schedulers.io())
                .doOnError(error ->
                        Log.e(TAG, "Failed to save user: " + error.getMessage()));
    }

    public Single<Boolean> isLoggedInRx() {
        return Single.fromCallable(() ->
                        pref.getBoolean(KEY_IS_LOGGED_IN, false))
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getUserEmailRx() {
        return Single.fromCallable(() -> {
                    String email = pref.getString(KEY_USER_EMAIL, "");
                    return email != null ? email : "";
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getUserNameRx() {
        return Single.fromCallable(() -> {
                    String name = pref.getString(KEY_USER_NAME, "");
                    return name != null ? name : "";
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getUserIdRx() {
        return Single.fromCallable(() -> {
                    String userId = pref.getString(KEY_USER_ID, "");
                    return userId != null ? userId : "";
                })
                .subscribeOn(Schedulers.io());
    }

    public Completable loginAsGuestRx() {
        return Completable.fromAction(() -> {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(KEY_IS_GUEST, true);
                    editor.putBoolean(KEY_IS_LOGGED_IN, false);
                    editor.apply();
                    Log.d(TAG, "User logged in as guest");
                })
                .subscribeOn(Schedulers.io())
                .doOnError(error ->
                        Log.e(TAG, "Failed to login as guest: " + error.getMessage()));
    }

    public Completable clearUserDataRx() {
        return Completable.fromAction(() -> {
                    pref.edit().clear().apply();
                    Log.d(TAG, "User data cleared");
                })
                .subscribeOn(Schedulers.io())
                .doOnError(error ->
                        Log.e(TAG, "Failed to clear user data: " + error.getMessage()));
    }

    public Single<Boolean> isGuestRx() {
        return Single.fromCallable(() ->
                        pref.getBoolean(KEY_IS_GUEST, false))
                .subscribeOn(Schedulers.io());
    }

    public Completable saveUserDataRx(String email, String name, String userId, boolean isGuest) {
        return Completable.fromAction(() -> {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(KEY_IS_LOGGED_IN, !isGuest);
                    editor.putString(KEY_USER_EMAIL, email);
                    editor.putString(KEY_USER_NAME, name);
                    editor.putString(KEY_USER_ID, userId);
                    editor.putBoolean(KEY_IS_GUEST, isGuest);
                    editor.apply();
                    Log.d(TAG, "User data saved - Guest: " + isGuest + ", Email: " + email);
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<User> getUserDataRx() {
        return Single.fromCallable(() -> {
                    String email = pref.getString(KEY_USER_EMAIL, "");
                    String name = pref.getString(KEY_USER_NAME, "");
                    String userId = pref.getString(KEY_USER_ID, "");
                    boolean isLoggedIn = pref.getBoolean(KEY_IS_LOGGED_IN, false);
                    boolean isGuest = pref.getBoolean(KEY_IS_GUEST, false);

                    User user = new User(
                             userId != null ? userId : "",
                            email != null ? email : "",
                            name != null ? name : "");
                    user.setGuest(isGuest);
                    user.setLoggedIn(isLoggedIn);
                    return user;
                })
                .subscribeOn(Schedulers.io());
    }


    public void saveUserLogin(String email, String name, String userId, SimpleCallback callback) {
        saveUserLoginRx(email, name, userId)
                .subscribe(
                        () -> {
                            if (callback != null) {
                                callback.onSuccess("User saved successfully");
                            }
                        },
                        error -> {
                            if (callback != null) {
                                callback.onFailure("Failed to save user: " + error.getMessage());
                            }
                        }
                );
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public void loginAsGuest(SimpleCallback callback) {
        loginAsGuestRx()
                .subscribe(
                        () -> {
                            if (callback != null) {
                                callback.onSuccess("User logged in as a guest");
                            }
                        },
                        error -> {
                            if (callback != null) {
                                callback.onFailure("Failed to login: " + error.getMessage());
                            }
                        }
                );
    }

    public void clearUserData(SimpleCallback callback) {
        clearUserDataRx()
                .subscribe(
                        () -> {
                            if (callback != null) {
                                callback.onSuccess("User data cleared");
                            }
                        },
                        error -> {
                            if (callback != null) {
                                callback.onFailure("Failed to clear data: " + error.getMessage());
                            }
                        }
                );
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