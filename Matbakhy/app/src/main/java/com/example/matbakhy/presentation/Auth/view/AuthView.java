package com.example.matbakhy.presentation.Auth.view;

public interface AuthView {
    void showLoading();
    void hideLoading();
    void showEmailError(String message);
    void showPasswordError(String message);
    void clearErrors();
    void showToast(String message);
    void navigateToHome(String email);
    void navigateToRegister();
    void navigateToForgotPassword();
    void setEmail(String email);

    String getEmail();
    String getPassword();
    void clearPasswordField();
    void focusPasswordField();
    void startGoogleSignInIntent(android.content.Intent intent);
    void clearEmailField();
}
