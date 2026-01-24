package com.example.matbakhy.data.auth.callbacks;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface RestoreCallback {
    void onSuccess(int restoredCount, List<Meal> meals, String message);
    void onError(String errorMessage);
}