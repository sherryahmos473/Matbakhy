package com.example.matbakhy.presentation.MealsList.views;

import com.example.matbakhy.data.model.Meal;

public interface MealListView {
    void onFailure(String errorMessage);
    void onClickMeal(Meal meal);
}
