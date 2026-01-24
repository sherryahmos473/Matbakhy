package com.example.matbakhy.presentation.Search;

import com.example.matbakhy.presentation.Meals.view.HomeView;

public interface SearchPresenter {
    void attachView(SearchView view);
    public void detachView();
    void getAllCategories();
    void getAllCountries();
    public void getMealOfCategory(String categoryName);
    public void getMealOfCountry(String countryName);
}
