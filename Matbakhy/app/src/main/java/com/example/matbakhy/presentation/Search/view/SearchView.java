package com.example.matbakhy.presentation.Search.view;

import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface SearchView {
    void getCategories(List<Category> categories);
    void getCountries(List<Area> countries);
    void getIngredients(List<String> ingredients);
    void getMealByName(Meal meal);
    void getMealByFLetter(List<Meal> mealList);

    void onFailure(String errorMessage);
    void onSuccess(List<Meal> mealList);
}
