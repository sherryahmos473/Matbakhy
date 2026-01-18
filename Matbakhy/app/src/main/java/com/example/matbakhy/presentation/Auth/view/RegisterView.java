package com.example.matbakhy.presentation.Auth.view;

public interface RegisterView extends BaseAuthView {
    void showNameError(String message);
    void showConfirmPasswordError(String message);
    void navigateToLogin();
    void setName(String name);
    String getName();
    String getConfirmPassword();
    void clearAllFields();
}