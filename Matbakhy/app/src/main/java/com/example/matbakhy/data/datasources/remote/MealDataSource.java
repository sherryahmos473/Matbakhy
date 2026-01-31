package com.example.matbakhy.data.datasources.remote;

import android.content.Context;

import com.example.matbakhy.data.model.Area;
import com.example.matbakhy.data.model.AreaResponse;
import com.example.matbakhy.data.model.Category;
import com.example.matbakhy.data.model.CategoryListResponse;
import com.example.matbakhy.data.model.Ingredient;
import com.example.matbakhy.data.model.IngredientsListResponse;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.model.MealListResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MealDataSource {
    public MealServices mealServices;
    public MealDataSource(Context context){
        mealServices = Network.getInstance(context).services;
    }
    public Single<List<Meal>> getMealOfTheDay(){
       return mealServices.getMealOfTheDay().subscribeOn(Schedulers.io())
               .map(MealListResponse::getMeals);
    }
    public Single<List<Meal>> getMealOfCategory(String category){
        return mealServices.getMealOfCategory(category).subscribeOn(Schedulers.io()).map(MealListResponse::getMeals);
    }
    public Single<List<Meal>> getMealOfCountry(String country){
        return mealServices.getMealOfCountry(country).subscribeOn(Schedulers.io()).map(MealListResponse::getMeals);
    }
    public  Single<List<Meal>> getMealOfIngredient(String ingredient){
        return mealServices.getMealOfIngredient(ingredient).subscribeOn(Schedulers.io()).map(MealListResponse::getMeals);
    }
    public  Single<List<Meal>> getMealByName(String name){
        return mealServices.getMealByName(name).subscribeOn(Schedulers.io()).map(MealListResponse::getMeals);
    }
    public Single<List<Meal>> getMealByFLetter(String FLetter){
        return mealServices.getMealByFLetter(FLetter).subscribeOn(Schedulers.io()).map(MealListResponse::getMeals);
    }
    public Single<List<Category>> getAllCategories(){
        return mealServices.getAllCategories().subscribeOn(Schedulers.io()).map(CategoryListResponse::getCategories);
    }
    public Single<List<Ingredient>> getAllIngredients(){
        return mealServices.getAllIngredients().subscribeOn(Schedulers.io()).map(IngredientsListResponse::getIngredients);
    }
    public Single<List<Area>> getAllCountries(){
        return mealServices.getAllCountries().subscribeOn(Schedulers.io())
                .map(AreaResponse::getAreas);
    }
}
