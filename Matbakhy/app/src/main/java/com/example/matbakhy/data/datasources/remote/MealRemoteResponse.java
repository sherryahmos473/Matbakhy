package com.example.matbakhy.data.datasources.remote;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface MealRemoteResponse {
    void onSuccess(List<Meal> mealList);
    void onFailure(String errorMessage);
}
