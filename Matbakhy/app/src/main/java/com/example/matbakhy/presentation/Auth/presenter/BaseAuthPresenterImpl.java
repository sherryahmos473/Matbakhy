package com.example.matbakhy.presentation.Auth.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.data.model.User;
import com.example.matbakhy.presentation.Auth.view.BaseAuthView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseAuthPresenterImpl implements BaseAuthPresenter {
    protected static final String TAG = "BaseAuthPresenter";
    protected static final int RC_SIGN_IN = 9001;

    protected BaseAuthView view;
    protected final AuthRepository authRepository;
    protected final CompositeDisposable disposables = new CompositeDisposable();

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
        disposables.clear();
    }

    @Override
    public void onGoogleSignInClicked() {
        if (view == null) return;

        disposables.add(
                authRepository.isNetworkAvailable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isAvailable -> {
                                    if (isAvailable) {
                                        startGoogleSignIn();
                                    } else {
                                        view.showError("No internet connection. Please check your network.");
                                    }
                                },
                                error -> view.showError("Network check failed: " + error.getMessage())
                        )
        );
    }

    public void onGoogleSignInClickedWithRestore() {
        if (view == null) return;

        disposables.add(
                authRepository.isNetworkAvailable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isAvailable -> {
                                    if (isAvailable) {
                                        startGoogleSignIn();
                                    } else {
                                        view.showError("No internet connection. Please check your network.");
                                    }
                                },
                                error -> view.showError("Network check failed: " + error.getMessage())
                        )
        );
    }

    private void startGoogleSignIn() {
        disposables.add(
                authRepository.getGoogleSignInIntent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                intent -> {
                                    view.showLoading("Signing in with Google...");
                                    view.startGoogleSignInIntent(intent, RC_SIGN_IN);
                                },
                                error -> view.showError("Google Sign-In not available: " + error.getMessage())
                        )
        );
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
                    view.showError("Google Sign-In cancelled or failed.");
                }
            }
        }
    }

    private void handleGoogleSignInResultWithRestore(Intent data) {
        disposables.add(
                authRepository.handleGoogleSignInWithRestore(data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    if (view != null) {
                                        Log.d(TAG, "Google Sign-In successful for: " + user.getEmail());
                                        view.showToast("Welcome " + (user.getName() != null ? user.getName() : "User") + "!");
                                        view.navigateToHome(user.getEmail());
                                    }
                                },
                                error -> {
                                    if (view != null) {
                                        Log.e(TAG, "Google Sign-In failed: " + error.getMessage());
                                        view.showError(error.getMessage());
                                    }
                                }
                        )
        );
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
        disposables.clear();
    }

    protected boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    protected void handleGoogleSignInResult(Intent data) {
        disposables.add(
                authRepository.handleGoogleSignInResult(data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> {
                                    if (view != null) {
                                        Log.d(TAG, "Google Sign-In successful for: " + user.getEmail());
                                        view.showToast("Welcome " + (user.getName() != null ? user.getName() : "User") + "!");
                                        view.navigateToHome(user.getEmail());
                                    }
                                },
                                error -> {
                                    if (view != null) {
                                        Log.e(TAG, "Google Sign-Up failed: " + error.getMessage());
                                        view.showError(error.getMessage());
                                    }
                                }
                        )
        );
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
        disposables.add(
                authRepository.loginAsGuest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (view != null) {
                                        view.showToast("Logged in successfully");
                                        view.navigateToHome("none");
                                    }
                                },
                                error -> {
                                    if (view != null) {
                                        view.showError("Can't login: " + error.getMessage());
                                    }
                                }
                        )
        );
    }

    @Override
    public void checkIfGuest() {
        disposables.add(
                authRepository.isGuest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isGuest -> {
                                    if (view != null) {
                                        view.onGuestStatus(isGuest);
                                    }
                                },
                                error -> {
                                    if (view != null) {
                                        view.onGuestStatus(false);
                                    }
                                }
                        )
        );
    }

    @Override
    public boolean isGuest() {
        return false; // This should be handled asynchronously via checkIfGuest()
    }
}