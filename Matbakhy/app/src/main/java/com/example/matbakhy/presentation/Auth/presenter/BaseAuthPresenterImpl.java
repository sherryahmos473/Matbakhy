package com.example.matbakhy.presentation.Auth.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.data.callbacks.AuthCallback;
import com.example.matbakhy.data.callbacks.SimpleCallback;
import com.example.matbakhy.data.model.User;
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
            view.showError("No internet connection. Please check your network.");
            return;
        }
        Intent signInIntent = authRepository.getGoogleSignInIntent();
        if (signInIntent != null) {
            view.showLoading("Signing in with Google...");
            view.startGoogleSignInIntent(signInIntent, RC_SIGN_IN);
        } else {
            view.showError("Google Sign-In not available. Please try again.");
        }
    }

    public void onGoogleSignInClickedWithRestore() {
        if (view == null) return;

        if (!authRepository.isNetworkAvailable()) {
            view.showError("No internet connection. Please check your network.");
            return;
        }

        Intent signInIntent = authRepository.getGoogleSignInIntent();
        if (signInIntent != null) {
            view.showLoading("Signing in with Google...");
            view.startGoogleSignInIntent(signInIntent, RC_SIGN_IN);
        } else {
            view.showError("Google Sign-In not available. Please try again.");
        }
    }

    @Override
    public void onGoogleSignInResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (view != null) {
                view.hideLoading();
            }

            if (data != null) {
                handleGoogleSignInResultWithRestore(data);
            } else {
                Log.e(TAG, "Google Sign-In returned null data");
                if (view != null) {
                    view.showError("Google Sign-In failed. Please try again.");
                }
            }
        }
    }

    private void handleGoogleSignInResultWithRestore(Intent data) {
        authRepository.handleGoogleSignInWithRestore(data, new AuthCallback() {
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
                    Log.e(TAG, "Google Sign-In failed: " + errorMessage);
                    view.showError(errorMessage);
                }
            }
        });
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
    }

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
                    view.showError(errorMessage);
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
        Log.d(TAG, "validateEmailAndPassword: " + isValid);

        return isValid;
    }

    @Override
    public void loginGuest() {
        authRepository.loginAsGuest(new SimpleCallback() {
            @Override
            public void onSuccess(String message) {
                view.showToast("Logged in successfully");
                view.navigateToHome("none");
            }

            @Override
            public void onFailure(String error) {
                view.showError("Can't login");
            }
        });
    }

    @Override
    public boolean isGuest() {
        return authRepository.isGuest();
    }
}