package com.example.matbakhy.presentation.Auth.presenter;


import com.example.matbakhy.presentation.Auth.view.LoginView;

public interface LoginPresenter {
    void attachView(LoginView view);
    void detachView();
    void onLoginClicked();
    void onRegisterClicked();
    void onForgotPasswordClicked();
    void onGoogleSignInClicked();
    void onGoogleSignInResult(int requestCode, int resultCode, android.content.Intent data);
    void checkIfUserLoggedIn();
    void onViewCreated();
    void onDestroyView();
    void onGoogleSignInClickedWithRestore();
    void loginGuest();
    boolean isGuest();
}
