package com.example.matbakhy.presentation.Auth.presenter;

import com.example.matbakhy.presentation.Auth.view.RegisterView;

public interface RegisterPresenter {
    void attachView(RegisterView view);
    void detachView();
    void onRegisterClicked();
    void onGoogleSignInClicked();
    void onLoginClicked();
    void onGoogleSignInResult(int requestCode, int resultCode, android.content.Intent data);
    void onViewCreated();
    void onDestroyView();
    void onGoogleSignInClickedWithRestore();
    void loginGuest();
    boolean isGuest();
}