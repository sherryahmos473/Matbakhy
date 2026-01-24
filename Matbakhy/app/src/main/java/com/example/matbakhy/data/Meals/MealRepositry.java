package com.example.matbakhy.data.Meals;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.matbakhy.data.Favorite.localDataSource.MealsLocalDataSource;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealOfTheDayDataSource;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.auth.callbacks.BackupCallback;
import com.example.matbakhy.data.auth.datasources.remote.FirebaseBackupService;
import com.example.matbakhy.data.auth.datasources.remote.MealBackupManager;

import java.util.ArrayList;
import java.util.List;

public class MealRepositry {
    private MealOfTheDayDataSource mealServices;
    private MealsLocalDataSource mealsLocalDataSource;
    private MealBackupManager backupManager;

    public MealRepositry(Context context){
        mealServices = new MealOfTheDayDataSource();
        mealsLocalDataSource = new MealsLocalDataSource(context);
        backupManager = new MealBackupManager(context);
    }
    public List<Meal> getMealOfTheDay(MealRemoteResponse callback){
        return mealServices.getMealOfTheDay(callback);
    }
    public List<Meal> getMealByName(MealRemoteResponse callback,String name){
        return mealServices.getMealByName(callback,name);
    }
    public List<Meal> getMealOfCategory(MealRemoteResponse callback, String category){
        return mealServices.getMealOfCategory(callback, category);
    }
    public List<Category> getAllCategories(CategoriesRemoteResponse callback){
        return mealServices.getAllCategories(callback);
    }
    public LiveData<List<Meal>> getAllLocalMeals() {
        return mealsLocalDataSource.getMeals();
    }
    public void insertMealInFav(Meal meal){
        if (meal.getId() != null) {
            meal.setFavorite(true);
            mealsLocalDataSource.insertMeal(meal);
        }
    }
    public void deleteMealsFromFav(Meal meal){
        mealsLocalDataSource.deleteMeal(meal);
    }
    public LiveData<Boolean>  isFavorite(String mealId){
        return mealsLocalDataSource.isFavorite(mealId);
    }
    public void backupAllMealsToFirebase(BackupCallback callback) {
        List<Meal> meals = getAllMealsFromLocal();
        backupManager.backupMeals(meals, callback);
    }

    private List<Meal> getAllMealsFromLocal() {
        LiveData<List<Meal>> mealsLiveData = getAllLocalMeals();
        Log.d("TAG", "getAllMealsFromLocal: " + mealsLiveData);
        return mealsLocalDataSource.getAllMealsSync();
    }
    public void clearAllLocalMeals() {
        mealsLocalDataSource.deleteAllMeals();
    }

}
