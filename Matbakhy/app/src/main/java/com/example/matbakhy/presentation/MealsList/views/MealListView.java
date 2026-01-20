package com.example.matbakhy.presentation.MealsList.views;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface MealListView {
    void onSuccess(List<Meal> mealList);
    void onFailure(String errMessge);
    void onClickMeal(Meal meal);
}
