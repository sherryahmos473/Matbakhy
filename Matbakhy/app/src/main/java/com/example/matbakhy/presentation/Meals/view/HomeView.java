package com.example.matbakhy.presentation.Meals.view;

import com.example.matbakhy.data.Meals.model.Meal;

public interface HomeView {
    void onSuccess(Meal meal);
    void onFailure(String errorMeassge);
}
