package com.example.matbakhy.data.Meals.dataSource;

import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.CategoryListResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.Meals.model.MealListResponse;
import com.example.matbakhy.data.Meals.dataSource.Network.MealServices;
import com.example.matbakhy.data.Meals.dataSource.Network.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealOfTheDayDataSource {
    public MealServices mealServices;
    public MealOfTheDayDataSource(){
        mealServices = Network.getInstance().services;
    }
    public List<Meal> getMealOfTheDay(MealRemoteResponse callback){
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
        return null;
    }
    public List<Meal> getMealOfCategory(MealRemoteResponse callback,String category){
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
        return null;
    }
    public List<Meal> getMealByName(MealRemoteResponse callback,String name){
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
        return null;
    }
    public List<Category> getAllCategories(CategoriesRemoteResponse callback){
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
        return null;
    }
}
