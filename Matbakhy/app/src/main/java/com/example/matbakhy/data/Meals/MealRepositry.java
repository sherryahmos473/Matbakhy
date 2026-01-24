package com.example.matbakhy.data.Meals;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Favorite.localDataSource.MealsLocalDataSource;
import com.example.matbakhy.data.Meals.dataSource.AreaRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealDataSource;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.AreaResponse;
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
    public void getMealOfCategory(MealRemoteResponse callback, String category){
        mealServices.getMealOfCategory(callback, category);
    }
    public void getAllCategories(CategoriesRemoteResponse callback){
        mealServices.getAllCategories(callback);
    }public void getAllCountries(AreaRemoteResponse callback){
        mealServices.getAllCountries(callback);
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
