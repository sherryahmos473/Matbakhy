package com.example.matbakhy.presentation.Meals.view;

import com.example.matbakhy.data.model.Area;
import com.example.matbakhy.data.model.Category;
import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface HomeView {
    void getMealOfTheDay(Meal meal);
    void onFailure(String errorMessage);
    void getAllCategories(List<Category> categories);
    void getAllCountries(List<Area> countries);
    void onSuccess(List<Meal> mealList);
    void navigateToLogin();
    void onGuestStatus(boolean isGuest);
}
