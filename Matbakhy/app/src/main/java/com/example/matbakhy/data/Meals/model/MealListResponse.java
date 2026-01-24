package com.example.matbakhy.data.Meals.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MealListResponse {
    @SerializedName("meals")
    private List<Meal> meals;

    public MealListResponse() {
    }

    public MealListResponse(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public boolean hasMeals() {
        return meals != null && !meals.isEmpty();
    }

}