package com.example.matbakhy.data.Meals.dataSource.Network;

import com.example.matbakhy.data.Meals.model.CategoryListResponse;
import com.example.matbakhy.data.Meals.model.MealListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MealServices {
    @GET("random.php")
    Call<MealListResponse> getMealOfTheDay();
    @GET("categories.php")
    Call<CategoryListResponse> getAllCategories();
    @GET("filter.php")
    Call<MealListResponse> getMealOfCategory(@Query("c") String category);
    @GET("search.php")
    Call<MealListResponse> getMealByName(@Query("s") String name);


}
