package com.example.matbakhy.presentation.Search.presenter;

import android.content.Context;

import com.example.matbakhy.presentation.Search.view.SearchView;

public interface SearchPresenter {
    void attachView(SearchView view);
    public void detachView();
    void getAllCategories();
    void getAllCountries();
    void getAllIngredients();
    public void getMealOfCategory(String categoryName);
    public void getMealOfCountry(String countryName);
    public void getMealOfIngredient(String ingredient);
     public  void getMealByName(String name);
     public  void getMealByFLetter(String FLetter);
    public boolean isNetworkAvailable(Context context);
}
