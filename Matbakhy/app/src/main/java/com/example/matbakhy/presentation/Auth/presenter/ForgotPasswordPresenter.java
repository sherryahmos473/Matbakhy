package com.example.matbakhy.presentation.Auth.presenter;


import com.example.matbakhy.presentation.Auth.view.ForgotPasswordView;

public interface ForgotPasswordPresenter {
    void attachView(ForgotPasswordView view);
    void detachView();
    void onSendResetClicked();
    void onResendEmailClicked();
    void onRememberPasswordClicked();
    void onBackClicked();
    void onDestroyView();
}