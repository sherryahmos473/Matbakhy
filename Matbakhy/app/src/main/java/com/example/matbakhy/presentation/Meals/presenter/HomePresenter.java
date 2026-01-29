package com.example.matbakhy.presentation.Meals.presenter;

import android.content.Context;

import com.example.matbakhy.presentation.Meals.view.HomeView;

public interface HomePresenter {
    void attachView(HomeView view);
    void detachView();
    void getMealOfTheDay();
    void getAllCategories();
    void getAllCountries();
    public void getMealOfCategory(String categoryName);
    public void getMealOfCountry(String countryName);
    void logout();
    boolean isNetworkAvailable(Context context);
    void isGuest();
}
