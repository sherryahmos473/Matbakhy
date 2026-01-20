package com.example.matbakhy.presentation.MealsList.presenter;

import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.MealsList.views.MealListView;

import java.util.List;

public interface MealListPresenter {
    public void attachView(MealListView view);
    void getMealOfCategory(String category);
    public void detachView();
    void getMealByName(String name);
}
