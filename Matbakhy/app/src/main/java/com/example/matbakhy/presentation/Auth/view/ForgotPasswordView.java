package com.example.matbakhy.presentation.Auth.view;

public interface ForgotPasswordView {
    void showLoading();
    void hideLoading();
    void showEmailError(String message);
    void clearEmailError();
    void showToast(String message);
    void showSuccessScreen(String email);
    void hideInputForm();
    void showInputForm();
    void navigateToLogin();
    void navigateBack();
    String getEmail();
    void setEmail(String email);
}