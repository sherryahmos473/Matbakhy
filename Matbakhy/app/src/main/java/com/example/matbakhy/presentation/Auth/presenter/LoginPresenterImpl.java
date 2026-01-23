package com.example.matbakhy.presentation.Auth.presenter;


import android.content.Context;

import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.data.auth.callbacks.AuthCallback;
import com.example.matbakhy.data.auth.model.User;
import com.example.matbakhy.presentation.Auth.view.LoginView;

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
        if (authRepository.isUserLoggedIn()) {
            String email = authRepository.getCurrentUserEmail();
            if (loginView != null) {
                loginView.navigateToHome(email);
            }
        }
    }

    private void performLogin(String email, String password) {
        if (loginView == null) return;

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