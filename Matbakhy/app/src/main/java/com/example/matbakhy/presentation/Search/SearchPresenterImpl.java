package com.example.matbakhy.presentation.Search;

import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.dataSource.AreaRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Meals.view.HomeView;

import java.util.List;

public class SearchPresenterImpl implements SearchPresenter{
    private MealRepositry mealRepositry;
    private SearchView searchView;
    public SearchPresenterImpl(Context context){
        mealRepositry = new MealRepositry(context);
    }

    @Override
    public void attachView(SearchView view) {
        this.searchView = view;
    }

    @Override
    public void detachView() {
        this.searchView = null;
    }
    public void getAllCategories() {
        mealRepositry.getAllCategories(new CategoriesRemoteResponse() {
            @Override
            public void onSuccess(List<Category> categories) {
                searchView.getCategories(categories);
            }
            @Override
            public void onFailure(String errorMessage) {
                searchView.onFailure(errorMessage);
            }
        });
    }
    @Override
    public void getAllCountries() {
        mealRepositry.getAllCountries(new AreaRemoteResponse() {
            @Override
            public void onSuccess(List<Area> areas) {
                searchView.getCountries(areas);
            }
            @Override
            public void onFailure(String errorMessage) {
                searchView.onFailure(errorMessage);
            }
        });
    }
    public void getMealOfCategory(String categoryName) {
        if (mealRepositry == null) return;

        mealRepositry.getMealOfCategory(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (searchView != null) {
                    Log.d("Meals", "onSuccess: "+ meals.size());
                    searchView.onSuccess(meals);
                }
            }
            @Override
            public void onFailure(String error) {
                if (searchView != null) {
                    searchView.onFailure(error);
                }
            }
        }, categoryName);
    }

    @Override
    public void getMealOfCountry(String countryName) {
        if (mealRepositry == null) return;

        mealRepositry.getMealOfCountry(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (searchView != null) {
                    Log.d("Meals", "onSuccess: "+ meals.size());
                    searchView.onSuccess(meals);
                }
            }
            @Override
            public void onFailure(String error) {
                if (searchView != null) {
                    searchView.onFailure(error);
                }
            }
        }, countryName);
    }
}
