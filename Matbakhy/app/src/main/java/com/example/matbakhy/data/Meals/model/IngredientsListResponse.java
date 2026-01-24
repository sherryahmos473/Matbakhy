package com.example.matbakhy.data.Meals.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientsListResponse {
    @SerializedName("meals")
    List<Ingredient> ingredients;

    public IngredientsListResponse(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsListResponse() {
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
