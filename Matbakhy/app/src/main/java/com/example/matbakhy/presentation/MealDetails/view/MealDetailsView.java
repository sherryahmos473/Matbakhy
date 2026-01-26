package com.example.matbakhy.presentation.MealDetails.view;

import androidx.lifecycle.LifecycleOwner;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface MealDetailsView {
    void onAddToFav();
    void onAddToCal();
    void isFav(boolean isFav);
    void isCal(boolean isPlanned);
    void removeMealFromFav();
    void removeMealFromCal();
    LifecycleOwner getLifecycleOwner();
    void onSuccess(List<Meal> meals);
    void onFailure(String errorMessage);
    void guestDialog();
    void navToLogin();
}
