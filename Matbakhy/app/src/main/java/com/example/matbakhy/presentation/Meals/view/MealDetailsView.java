package com.example.matbakhy.presentation.Meals.view;

import androidx.lifecycle.LifecycleOwner;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface MealDetailsView {
    void onAddToFav();
    void isFav(boolean isFav);
    void removeMealFromFav();
    LifecycleOwner getLifecycleOwner();
    void onSuccess(List<Meal> meals);
    void onFailure(String errorMessage);
}
