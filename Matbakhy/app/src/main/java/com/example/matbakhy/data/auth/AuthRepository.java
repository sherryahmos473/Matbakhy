package com.example.matbakhy.data.auth;

import android.content.Context;
import android.content.Intent;

import com.example.matbakhy.data.auth.callbacks.AuthCallback;
import com.example.matbakhy.data.auth.callbacks.SimpleCallback;
import com.example.matbakhy.data.auth.datasources.local.SharedPref;
import com.example.matbakhy.data.auth.datasources.remote.FirebaseServices;
import com.example.matbakhy.data.auth.datasources.remote.FirebaseServices;

public class AuthRepository {
    private final FirebaseServices firebaseServices;
    private final SharedPref sharedPrefDataSource;
    private final Context context;

    public AuthRepository(Context context,
                          FirebaseServices firebaseServices,
                          SharedPref sharedPrefDataSource) {
        this.context = context;
        this.firebaseServices = firebaseServices;
        this.sharedPrefDataSource = sharedPrefDataSource;
        this.firebaseServices.initialize(context);
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

    public boolean isValidEmail(String email) {
        return firebaseServices.isValidEmail(email);
    }

    public boolean isValidPassword(String password) {
        return firebaseServices.isValidPassword(password);
    }
}