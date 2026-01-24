package com.example.matbakhy.presentation.Search.presenter;

import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.dataSource.AreaRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.IngredientRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Ingredient;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Search.view.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenterImpl implements SearchPresenter {
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

    @Override
    public void getAllIngredients() {
        mealRepositry.getAllIngredients(new IngredientRemoteResponse() {
            @Override
            public void onSuccess(List<Ingredient> ingredients) {
                List<String> result = new ArrayList<>();
                Log.d("SearchFragment", "onSuccess: " + ingredients.size());

                for (Ingredient ingredient : ingredients){
                    result.add(ingredient.getName());
                }
                searchView.getIngredients(result);
                Log.d("SearchFragment", "onSuccess: " + result.size());
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
                Log.d("Meals", "onSuccess: "+ meals.size());
                searchView.onSuccess(meals);
            }
            @Override
            public void onFailure(String error) {
                searchView.onFailure(error);
            }
        }, categoryName);
    }

    @Override
    public void getMealOfCountry(String countryName) {
        if (mealRepositry == null) return;

        mealRepositry.getMealOfCountry(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> meals) {
                Log.d("Meals", "onSuccess: "+ meals.size());
                searchView.onSuccess(meals);
            }
            @Override
            public void onFailure(String error) {
                searchView.onFailure(error);
            }
        }, countryName);
    }

    @Override
    public void getMealOfIngredient(String ingredient) {
        mealRepositry.getMealOfIngredient(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                Log.d("SearchFragment", "onSuccess: "+ mealList.size());
                searchView.onSuccess(mealList);
            }

            @Override
            public void onFailure(String errorMessage) {
                searchView.onFailure(errorMessage);
            }
        }, ingredient);
    }

    @Override
    public void getMealByName(String name) {
        mealRepositry.getMealByName(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                searchView.getMealByName(mealList.get(0));
            }

            @Override
            public void onFailure(String errorMessage) {
                searchView.onFailure(errorMessage);
            }
        },name);
    }

}
