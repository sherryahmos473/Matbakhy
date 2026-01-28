package com.example.matbakhy.data.datasources.remote;

import com.example.matbakhy.data.model.Ingredient;

import java.util.List;

public interface IngredientRemoteResponse {
    void onSuccess(List<Ingredient> ingredients);
    void onFailure(String errorMessage);
}
