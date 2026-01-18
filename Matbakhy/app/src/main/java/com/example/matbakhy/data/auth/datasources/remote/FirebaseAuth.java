// FirebaseAuthDataSource.java
package com.example.matbakhy.data.auth.datasources.remote;

import android.content.Context;
import android.content.Intent;

import com.example.matbakhy.data.auth.callbacks.AuthCallback;
import com.example.matbakhy.data.auth.callbacks.SimpleCallback;

public interface FirebaseAuth {
    void initialize(Context context);
    void register(String email, String password, String name, AuthCallback callback);
    void login(String email, String password, AuthCallback callback);
    Intent getGoogleSignInIntent();
    void handleGoogleSignInResult(Intent data, AuthCallback callback);
    void sendPasswordResetEmail(String email, SimpleCallback callback);
    void logout();
    boolean isUserLoggedIn();
    String getCurrentUserEmail();
    String getCurrentUserName();
    boolean isNetworkAvailable();
    boolean isValidEmail(String email);
    boolean isValidPassword(String password);
}