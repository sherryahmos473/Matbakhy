package com.example.matbakhy.data.Favorite.localDataSource;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Favorite.DataBase.AppDataBase;
import com.example.matbakhy.data.Favorite.DataBase.MealDAO;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public class MealsLocalDataSource {
    public MealDAO mealsDAO;
    public MealsLocalDataSource(Context context){
        AppDataBase dataBase = AppDataBase.getInstance(context);
        mealsDAO = dataBase.mealDAO();
    }
    public void insertFavMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("insert", "run: "+ meal);
                mealsDAO.insertFavMeal(meal);
            }
        }).start();

    }
    public void insertMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealsDAO.insertFavMeal(meal);
            }
        }).start();

    }
    public void insertCalMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("insert", "run: "+ meal);
                mealsDAO.insertCalMeal(meal);
            }
        }).start();

    }
    public void deleteFavMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealsDAO.deleteFavMeal(meal.getId());
                mealsDAO.deleteIfNotFavoriteAndNotPlanned(meal.getId());
            }
        }).start();
    }
    public void deleteCalMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealsDAO.deleteCalMeal(meal.getId());
                mealsDAO.deleteIfNotFavoriteAndNotPlanned(meal.getId());
            }
        }).start();
    }
    public LiveData<List<Meal>> getFavMeals(){
        return mealsDAO.getFavMeals();
    }
    public LiveData<List<Meal>> getCalMeals(){
        return mealsDAO.getCalMeals();
    }
    public LiveData<List<Meal>> getAllMealsSync(){
        return  mealsDAO.getAllMealsSync();
    }
    public LiveData<Boolean> isFavorite(String mealId) {
        return mealsDAO.isFavorite(mealId);
    }
    public LiveData<Boolean> isCal(String mealId) {
        return mealsDAO.isCal(mealId);
    }
    public void deleteAllMeals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealsDAO.deleteAllMeals();
                Log.d("MealsLocalDataSource", "Deleted all meals from local database");
            }
        }).start();
    }
}
