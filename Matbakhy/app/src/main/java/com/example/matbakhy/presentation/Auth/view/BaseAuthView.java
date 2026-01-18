package com.example.matbakhy.presentation.Auth.view;


public interface BaseAuthView {
    void showLoading(String message);
    void hideLoading();
    void showEmailError(String message);
    void showPasswordError(String message);
    void clearErrors();
    void showToast(String message);
    void navigateToHome(String email);
    void setEmail(String email);
    String getEmail();
    String getPassword();
    void clearPasswordField();
    void focusPasswordField();
    void startGoogleSignInIntent(android.content.Intent intent);
    void clearEmailField();
}