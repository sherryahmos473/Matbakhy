package com.example.matbakhy.presentation.Auth.view;

public interface MainView {
    void navigateToHome(String email);
    void navigateToAuth();
    void showError(String message);
}