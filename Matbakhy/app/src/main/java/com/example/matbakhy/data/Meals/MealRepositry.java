package com.example.matbakhy.data.Meals;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Favorite.localDataSource.MealsLocalDataSource;
import com.example.matbakhy.data.Meals.dataSource.AreaRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.IngredientRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealDataSource;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.auth.callbacks.BackupCallback;
import com.example.matbakhy.data.auth.datasources.remote.MealBackupManager;

import java.util.List;

public class MealRepositry {
    private MealDataSource mealServices;
    private MealsLocalDataSource mealsLocalDataSource;
    private MealBackupManager backupManager;

    public MealRepositry(Context context){
        mealServices = new MealDataSource();
        mealsLocalDataSource = new MealsLocalDataSource(context);
        backupManager = new MealBackupManager(context);
    }
    public void getMealOfTheDay(MealRemoteResponse callback){
        mealServices.getMealOfTheDay(callback);
    }
    public void getMealByName(MealRemoteResponse callback, String name){
        mealServices.getMealByName(callback, name);
    }
    public void getMealByFLetter(MealRemoteResponse callback, String FLetter){
        mealServices.getMealByName(callback, FLetter);
    }
    public void getMealOfCategory(MealRemoteResponse callback, String category){
        mealServices.getMealOfCategory(callback, category);
    }
    public void getMealOfCountry(MealRemoteResponse callback, String country){
        mealServices.getMealOfCountry(callback, country);
    }
    public void getMealOfIngredient(MealRemoteResponse callback, String ingredient){
        mealServices.getMealOfIngredient(callback, ingredient);
    }
    public void getAllCategories(CategoriesRemoteResponse callback){
        mealServices.getAllCategories(callback);
    }
    public void getAllCountries(AreaRemoteResponse callback){
        mealServices.getAllCountries(callback);
    }
    public void getAllIngredients(IngredientRemoteResponse callback){
        mealServices.getAllIngredients(callback);
    }
    public LiveData<List<Meal>> getFavMeals() {
        return mealsLocalDataSource.getFavMeals();
    }
    public LiveData<List<Meal>> getCalMeals() {
        return mealsLocalDataSource.getCalMeals();
    }
    public void insertMealInFav(Meal meal){
        if (meal.getId() != null) {
            meal.setFavorite(true);
            mealsLocalDataSource.insertFavMeal(meal);
        }
    }
    public void insertMeal(Meal meal){
        if (meal.getId() != null) {
            mealsLocalDataSource.insertFavMeal(meal);
        }
    }
    public void insertMealInCal(Meal meal,String cal){
        if (meal.getId() != null) {
            meal.setPlanned(true);
            meal.setPlanDate(cal);
            mealsLocalDataSource.insertCalMeal(meal);
        }
    }
    public void deleteMealsFromFav(Meal meal){
        mealsLocalDataSource.deleteFavMeal(meal);
    }
    public void deleteMealsFromCal(Meal meal){
        mealsLocalDataSource.deleteCalMeal(meal);
    }
    public LiveData<Boolean> isFavorite(String mealId){
        return mealsLocalDataSource.isFavorite(mealId);
    }
    public LiveData<Boolean> isCal(String mealId){
        return mealsLocalDataSource.isCal(mealId);
    }
    public LiveData<List<Meal>> getAllMealsFromLocal() {
        return mealsLocalDataSource.getAllMealsSync();
    }
    public void clearAllLocalMeals() {
        mealsLocalDataSource.deleteAllMeals();
    }

}
