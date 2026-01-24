package com.example.matbakhy.data.Meals.dataSource;

import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Ingredient;

import java.util.List;

public interface IngredientRemoteResponse {
    void onSuccess(List<Ingredient> ingredients);
    void onFailure(String errorMessage);
}
