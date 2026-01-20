package com.example.matbakhy.presentation.Meals.view;

import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface HomeView {
    void getMealOfTheDay(Meal meal);
    void onFailure(String errorMeassge);
    void getAllCategories(List<Category> categories);

}
