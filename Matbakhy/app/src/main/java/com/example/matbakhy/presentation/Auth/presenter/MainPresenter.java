package com.example.matbakhy.presentation.Auth.presenter;


import com.example.matbakhy.presentation.Auth.view.MainView;

public interface MainPresenter {
    void attachView(MainView view);
    void detachView();
    void checkLoginStatus();
    void onDestroy();
}