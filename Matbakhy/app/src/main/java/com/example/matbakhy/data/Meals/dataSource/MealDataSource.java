package com.example.matbakhy.data.Meals.dataSource;

import android.util.Log;

import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.AreaResponse;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.CategoryListResponse;
import com.example.matbakhy.data.Meals.model.Ingredient;
import com.example.matbakhy.data.Meals.model.IngredientsListResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.Meals.model.MealListResponse;
import com.example.matbakhy.data.Meals.dataSource.Network.MealServices;
import com.example.matbakhy.data.Meals.dataSource.Network.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDataSource {
    public MealServices mealServices;
    public MealDataSource(){
        mealServices = Network.getInstance().services;
    }
    public void getMealOfTheDay(MealRemoteResponse callback){
        mealServices.getMealOfTheDay().enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Meal> products = response.body().getMeals();
                    callback.onSuccess(products);
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getMealOfCategory(MealRemoteResponse callback, String category){
        mealServices.getMealOfCategory(category).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Meal> products = response.body().getMeals();
                    callback.onSuccess(products);
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getMealOfCountry(MealRemoteResponse callback, String country){
        mealServices.getMealOfCountry(country).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Meal> products = response.body().getMeals();
                    callback.onSuccess(products);
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getMealOfIngredient(MealRemoteResponse callback, String ingredient){
        mealServices.getMealOfIngredient(ingredient).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Meal> products = response.body().getMeals();
                    callback.onSuccess(products);
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getMealByName(MealRemoteResponse callback, String name){
        mealServices.getMealByName(name).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Meal> products = response.body().getMeals();
                    callback.onSuccess(products);
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getMealByFLetter(MealRemoteResponse callback, String FLetter){
        mealServices.getMealByFLetter(FLetter).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Meal> products = response.body().getMeals();
                    callback.onSuccess(products);
                }
            }
            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getAllCategories(CategoriesRemoteResponse callback){
        mealServices.getAllCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Category> categories = response.body().getCategories();
                    callback.onSuccess(categories);
                }
            }
            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getAllIngredients(IngredientRemoteResponse callback){
        mealServices.getAllingredients().enqueue(new Callback<IngredientsListResponse>() {
            @Override
            public void onResponse(Call<IngredientsListResponse> call, Response<IngredientsListResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Ingredient> ingredients = response.body().getIngredients();
                    Log.d("SearchFragment", "onResponse: " + ingredients.size());
                    callback.onSuccess(ingredients);
                }
            }
            @Override
            public void onFailure(Call<IngredientsListResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
    public void getAllCountries(AreaRemoteResponse callback){
        mealServices.getAllCountries().enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<String> countries = response.body().getAreaNames();
                    List<Area> areas = Area.convertToArea(countries);
                    callback.onSuccess(areas);
                }
            }
            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                callback.onFailure("Something Wrong Happend");
            }
        });
    }
}
