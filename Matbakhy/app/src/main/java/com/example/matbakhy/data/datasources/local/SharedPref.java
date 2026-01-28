package com.example.matbakhy.data.datasources.local;

import com.example.matbakhy.data.callbacks.SimpleCallback;

public interface SharedPref {
    void saveUserLogin(String email, String name, String userId, SimpleCallback callback);
    boolean isLoggedIn();
    String getUserEmail();
    String getUserName();
    String getUserId();
    void loginAsGuest(SimpleCallback callback);
    void clearUserData(SimpleCallback callback);
    boolean isGuest();
}