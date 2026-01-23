package com.example.matbakhy.presentation.Meals.view;

import androidx.lifecycle.LifecycleOwner;

public interface MealDetailsView {
    void onAddToFav();
    void isFav(boolean isFav);
    void removeMealFromFav();
    LifecycleOwner getLifecycleOwner();
}
