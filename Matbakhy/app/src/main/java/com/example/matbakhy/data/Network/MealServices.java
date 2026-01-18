package com.example.matbakhy.data.Network;

import com.example.matbakhy.data.Meals.model.MealListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MealServices {
    @GET("random.php")
    Call<MealListResponse> getMealOfTheDay();
}
