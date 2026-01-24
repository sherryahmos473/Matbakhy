package com.example.matbakhy.presentation.MealsList.views;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface MealListView {
    void onFailure(String errorMessage);
    void onClickMeal(Meal meal);
}
