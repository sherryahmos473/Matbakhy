package com.example.matbakhy.data.datasources.remote;

import com.example.matbakhy.data.model.AreaResponse;
import com.example.matbakhy.data.model.CategoryListResponse;
import com.example.matbakhy.data.model.IngredientsListResponse;
import com.example.matbakhy.data.model.MealListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealServices {
    @GET("random.php")
    Single<MealListResponse> getMealOfTheDay();
    @GET("categories.php")
    Single<CategoryListResponse> getAllCategories();
    @GET("filter.php")
    Single<MealListResponse> getMealOfCategory(@Query("c") String category);
    @GET("filter.php")
    Single<MealListResponse> getMealOfCountry(@Query("a") String country);
    @GET("filter.php")
    Single<MealListResponse> getMealOfIngredient(@Query("i") String ingredient);
    @GET("search.php")
    Single<MealListResponse> getMealByName(@Query("s") String name);
    @GET("search.php")
    Single<MealListResponse> getMealByFLetter(@Query("f") String FLetter);
    @GET("list.php?a=list")
    Single<AreaResponse> getAllCountries();
    @GET("list.php?i=list")
    Single<IngredientsListResponse> getAllingredients();
}
