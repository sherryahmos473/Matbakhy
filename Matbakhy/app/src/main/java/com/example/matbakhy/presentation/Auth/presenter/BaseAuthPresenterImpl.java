package com.example.matbakhy.presentation.Auth.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.data.auth.callbacks.AuthCallback;
import com.example.matbakhy.data.auth.model.User;
import com.example.matbakhy.presentation.Auth.view.BaseAuthView;

public abstract class BaseAuthPresenterImpl implements BaseAuthPresenter {
    protected static final String TAG = "BaseAuthPresenter";
    protected static final int RC_SIGN_IN = 9001;

    protected BaseAuthView view;
    protected final AuthRepository authRepository;

    protected BaseAuthPresenterImpl(Context context) {
        this.authRepository = new AuthRepository(context);
    }

    @Override
    public void attachView(BaseAuthView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onGoogleSignInClicked() {
        if (view == null) return;
        if (!authRepository.isNetworkAvailable()) {
            view.showToast("No internet connection. Please check your network.");
            return;
        }
        Intent signInIntent = authRepository.getGoogleSignInIntent();
        if (signInIntent != null) {
            view.showLoading("Signing in with Google...");
            view.startGoogleSignInIntent(signInIntent);
        } else {
            view.showToast("Google Sign-In not available. Please try again.");
        }
    }

    @Override
    public void onGoogleSignInResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (view != null) {
                view.hideLoading();
            }

            if (data != null) {
                handleGoogleSignInResult(data);
            } else {
                Log.e(TAG, "Google Sign-In returned null data");
                if (view != null) {
                    view.showToast("Google Sign-In failed. Please try again.");
                }
            }
        }
    }

    @Override
    public void onViewCreated() {
        if (view != null) {
            view.clearEmailField();
            view.clearErrors();
        }
    }

    @Override
    public void onDestroyView() {
        // Clean up if needed
    }

    // Protected helper methods shared by children
    protected boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    protected void handleGoogleSignInResult(Intent data) {
        authRepository.handleGoogleSignInResult(data, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                if (view != null) {
                    Log.d(TAG, "Google Sign-In successful for: " + user.getEmail());
                    view.showToast("Welcome " + (user.getName() != null ? user.getName() : "User") + "!");
                    view.navigateToHome(user.getEmail());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    Log.e(TAG, "Google Sign-Up failed: " + errorMessage);
                    view.showToast(errorMessage);
                }
            }
        });
    }

    protected boolean validateEmailAndPassword(String email, String password) {
        boolean isValid = true;

        if (view == null) return false;
        view.clearErrors();
        if (email.isEmpty()) {
            view.showEmailError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            view.showEmailError("Please enter a valid email");
            isValid = false;
        }

        if (password.isEmpty()) {
            view.showPasswordError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            view.showPasswordError("Password must be at least 6 characters");
            isValid = false;
        }


        return isValid;
    }
}