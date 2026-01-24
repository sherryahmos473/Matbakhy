package com.example.matbakhy.presentation.Auth.presenter;


import com.example.matbakhy.presentation.Auth.view.BaseAuthView;

public interface BaseAuthPresenter {
    void attachView(BaseAuthView view);
    void detachView();
    void onGoogleSignInClicked();
    void onGoogleSignInResult(int requestCode, int resultCode, android.content.Intent data);
    void onViewCreated();
    void onDestroyView();
    void onGoogleSignInClickedWithRestore();
}