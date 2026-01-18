package com.example.matbakhy.data.Meals.dataSource;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface MealRemoteResponse {
    void onSuccess(List<Meal> mealList);
    void onFailure(String errorMessage);
}
