package com.example.matbakhy.presentation.MealDetails.presenter;

import com.example.matbakhy.data.Meals.model.Meal;

public interface MealDetailsPresenter {
    public void addMealToFav(Meal meal);
    public void removeMealFromFav(Meal meal);
    public void removeMealFromCal(Meal meal);
    public  void isFavorite(String mealId);
    public  void isCal(String mealId);
    void getMealOfIngredient(String ingredient);
    public void addMealToCal(Meal meal, String date);
    String extractYouTubeVideoId(String url);
}
