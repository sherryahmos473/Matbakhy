package com.example.matbakhy.presentation.Meals.presenter;

import com.example.matbakhy.data.Meals.model.Meal;

public interface MealDetailsPresenter {
    public void addMealToFav(Meal meal);
    public void removeMealFromFav(Meal meal);
    public  void isFavorite(String mealId);
    void getMealOfIngredient(String ingredient);
}
