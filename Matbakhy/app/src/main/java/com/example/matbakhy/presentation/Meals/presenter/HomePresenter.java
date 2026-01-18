package com.example.matbakhy.presentation.Meals.presenter;

import com.example.matbakhy.presentation.Meals.view.HomeView;

public interface HomePresenter {
    void attachView(HomeView view);
    void detachView();
    void getMealOfTheDay();

}
