package com.example.matbakhy.data.callbacks;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface RestoreCallback {
    void onSuccess(int restoredCount, List<Meal> meals, String message);
    void onError(String errorMessage);
}