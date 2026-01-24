package com.example.matbakhy.presentation.Meals.presenter;

import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Meals.view.HomeView;

import java.util.List;

public interface HomePresenter {
    void attachView(HomeView view);
    void detachView();
    void getMealOfTheDay();
    void getAllCategories();
    void getAllCountries();
    public void getMealOfCategory(String categoryName);
    public void getMealOfCountry(String countryName);
    void logout();
}
