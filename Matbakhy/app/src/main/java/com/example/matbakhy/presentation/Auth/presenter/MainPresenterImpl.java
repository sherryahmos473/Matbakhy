package com.example.matbakhy.presentation.Auth.presenter;


import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.presentation.Auth.view.MainView;

public class MainPresenterImpl implements MainPresenter {
    private static final String TAG = "MainPresenter";

    private MainView view;
    private final AuthRepository authRepository;

    public MainPresenterImpl(Context context) {
        this.authRepository = new AuthRepository(context);
    }

    public void attachView(MainView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public void checkLoginStatus() {
        if (view == null) return;

        Log.d(TAG, "Checking login status...");

        try {
            if (authRepository.isUserLoggedIn()) {
                String userEmail = authRepository.getCurrentUserEmail();
                if (userEmail == null || userEmail.isEmpty()) {
                    userEmail = "User";
                }
                Log.d(TAG, "User is logged in with email: " + userEmail);
                view.navigateToHome(userEmail);
            } else {
                Log.d(TAG, "User is not logged in, navigating to AuthActivity");
                view.navigateToAuth();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking login status: " + e.getMessage());
            view.showError("Error checking login status. Please try again.");
            view.navigateToAuth();
        }
    }

    public void onDestroy() {
    }
}