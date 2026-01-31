package com.example.matbakhy.presentation.Auth.presenter;


import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.presentation.Auth.view.ForgotPasswordView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordPresenterImpl implements ForgotPasswordPresenter {
    private static final String TAG = "ForgotPasswordPresenter";

    private ForgotPasswordView view;
    private final AuthRepository authRepository;
    private String userEmail = "";

    public ForgotPasswordPresenterImpl(Context context) {
        this.authRepository = new AuthRepository(context);
    }

    public void attachView(ForgotPasswordView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public void onSendResetClicked() {
        if (view == null) return;

        String email = view.getEmail();
        userEmail = email;

        if (!validateEmail(email)) {
            return;
        }

        sendResetEmail(email);
    }

    public void onResendEmailClicked() {
        if (view == null) return;

        if (userEmail.isEmpty()) {
            view.showToast("Email not found");
            return;
        }

        sendResetEmail(userEmail);
    }

    public void onRememberPasswordClicked() {
        if (view != null) {
            view.navigateToLogin();
        }
    }

    public void onBackClicked() {
        if (view != null) {
            view.navigateBack();
        }
    }

    public void onDestroyView() {
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            view.showEmailError("Email is required");
            return false;
        }

        if (!isValidEmail(email)) {
            view.showEmailError("Please enter a valid email");
            return false;
        }

        view.clearEmailError();
        return true;
    }

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendResetEmail(String email) {
        if (view == null) return;

        view.showLoading();

        Log.d(TAG, "Sending password reset email to: " + email);

        authRepository.sendPasswordResetEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.d(TAG, "Reset email sent successfully");
                            view.showSuccessScreen(email);
                            view.showToast("Reset email sent to " + email);
                        }
                        , throwable -> {
                            if (view != null) {
                                view.hideLoading();
                                Log.e(TAG, "Failed to send reset email: " + throwable.getMessage());
                                view.showError(throwable.getMessage());
                            }
                        }
                );
    }
}