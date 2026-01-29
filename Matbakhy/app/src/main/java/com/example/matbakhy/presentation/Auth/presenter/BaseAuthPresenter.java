package com.example.matbakhy.presentation.Auth.presenter;


import com.example.matbakhy.data.callbacks.SimpleCallback;
import com.example.matbakhy.presentation.Auth.view.BaseAuthView;

public interface BaseAuthPresenter {
    void attachView(BaseAuthView view);
    void detachView();
    void onGoogleSignInClicked();
    void onGoogleSignInResult(int requestCode, int resultCode, android.content.Intent data);
    void onViewCreated();
    void loginGuest();
    void checkIfGuest();
    boolean isGuest();
    void onDestroyView();
    void onGoogleSignInClickedWithRestore();
}