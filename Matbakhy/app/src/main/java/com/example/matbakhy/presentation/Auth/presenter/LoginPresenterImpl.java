package com.example.matbakhy.presentation.Auth.presenter;


import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.example.matbakhy.data.callbacks.AuthCallback;
import com.example.matbakhy.data.model.User;
import com.example.matbakhy.presentation.Auth.view.LoginView;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl extends BaseAuthPresenterImpl implements LoginPresenter {
    private LoginView loginView;

    public LoginPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        this.loginView = (LoginView) view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.loginView = null;
    }

    @Override
    public void onLoginClicked() {
        if (loginView == null) return;
        String email = loginView.getEmail();
        String password = loginView.getPassword();

        if (!validateEmailAndPassword(email, password)) {
            return;
        }
        Log.d(TAG, "onLoginClicked: ");
        performLogin(email, password);
    }

    @Override
    public void onRegisterClicked() {
        if (loginView != null) {
            loginView.navigateToRegister();
        }
    }

    @Override
    public void onForgotPasswordClicked() {
        if (loginView != null) {
            loginView.navigateToForgotPassword();
        }
    }

    @Override
    public void checkIfUserLoggedIn() {
        disposables.add(
                Single.zip(
                                authRepository.isUserLoggedIn(),
                                authRepository.getCurrentUserEmail(),
                                (isLoggedIn, email) -> new Pair<>(isLoggedIn, email)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    if (result.first && loginView != null) {
                                        loginView.navigateToHome(result.second);
                                    }
                                },
                                error -> {
                                    if (loginView != null) {
                                        loginView.showError("Failed to check login status");
                                    }
                                }
                        )
        );
    }

    private void performLogin(String email, String password) {
        if (loginView == null) return;
        Log.d(TAG, "performLogin: ");
        loginView.showLoading("Logging in...");

        authRepository.loginWithRestore(email, password, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                if (loginView != null) {
                    loginView.hideLoading();
                    loginView.showToast("Login successful!");
                    loginView.navigateToHome(email);
                }
            }
            @Override
            public void onFailure(String errorMessage) {
                if (loginView != null) {
                    loginView.hideLoading();
                    loginView.showToast(errorMessage);
                    loginView.clearPasswordField();
                    loginView.focusPasswordField();
                }
            }
        });
    }
}