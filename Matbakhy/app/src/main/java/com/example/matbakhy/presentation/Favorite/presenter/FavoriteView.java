package com.example.matbakhy.presentation.Favorite.presenter;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface FavoriteView {
    void getMeals(List<Meal> meals);
    void onFailure(String message);
    void onMealDeleted();
}
