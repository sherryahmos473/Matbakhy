package com.example.matbakhy.presentation.Auth.presenter;


import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.presentation.Auth.view.MainView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

        authRepository.isUserLoggedIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isLoggedIn -> {
                            if (isLoggedIn) {
                                // User is logged in, get email
                                authRepository.getCurrentUserEmail()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                email -> {
                                                    String finalEmail = (email == null || email.isEmpty()) ? "User" : email;
                                                    Log.d(TAG, "User is logged in with email: " + finalEmail);
                                                    view.navigateToHome(finalEmail);
                                                },
                                                error -> handleError(error)
                                        );
                            } else {
                                // User not logged in, check if guest
                                authRepository.isGuest()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                isGuest -> {
                                                    if (isGuest) {
                                                        view.navigateToHome("Guest");
                                                    } else {
                                                        Log.d(TAG, "User is not logged in, navigating to AuthActivity");
                                                        view.navigateToAuth();
                                                    }
                                                },
                                                error -> handleError(error)
                                        );
                            }
                        },
                        error -> handleError(error)
                );
    }

    private void handleError(Throwable error) {
        Log.e(TAG, "Error checking login status: " + error.getMessage());
        if (view != null) {
            view.showError("Error checking login status. Please try again.");
            view.navigateToAuth();
        }
    }

    public void onDestroy() {
    }
}