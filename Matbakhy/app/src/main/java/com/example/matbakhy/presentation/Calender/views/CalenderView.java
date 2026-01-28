package com.example.matbakhy.presentation.Calender.views;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface CalenderView {
    void getMeals(List<Meal> meals);
    void onFailure(String message);
    void onMealDeleted();
}
