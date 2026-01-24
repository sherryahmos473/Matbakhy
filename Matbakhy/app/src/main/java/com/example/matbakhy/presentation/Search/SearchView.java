package com.example.matbakhy.presentation.Search;

import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface SearchView {
    void getCategories(List<Category> categories);
    void getCountries(List<Area> countries);

    void onFailure(String errorMeassge);
    void onSuccess(List<Meal> mealList);
}
