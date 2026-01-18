package com.example.matbakhy.data.Meals.dataSource;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.Meals.model.MealListResponse;
import com.example.matbakhy.data.Network.MealServices;
import com.example.matbakhy.data.Network.Network;

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
}
