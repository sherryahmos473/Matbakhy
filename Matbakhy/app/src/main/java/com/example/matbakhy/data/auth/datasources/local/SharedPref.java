package com.example.matbakhy.data.auth.datasources.local;

import com.example.matbakhy.data.auth.callbacks.SimpleCallback;

public interface SharedPref {
    void saveUserLogin(String email, String name, String userId, SimpleCallback callback);
    boolean isLoggedIn();
    String getUserEmail();
    String getUserName();
    String getUserId();
    void clearUserData(SimpleCallback callback);
}