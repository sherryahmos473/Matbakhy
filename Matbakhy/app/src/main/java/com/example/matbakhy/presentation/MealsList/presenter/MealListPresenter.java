package com.example.matbakhy.presentation.MealsList.presenter;

import com.example.matbakhy.presentation.MealsList.views.MealListView;

public interface MealListPresenter {
    public void attachView(MealListView view);
    public void detachView();
    void getMealByName(String name);
}
