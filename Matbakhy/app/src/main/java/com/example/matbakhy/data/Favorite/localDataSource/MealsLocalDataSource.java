package com.example.matbakhy.data.Favorite.localDataSource;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.DataBase.AppDataBase;
import com.example.matbakhy.data.DataBase.MealDAO;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public class MealsLocalDataSource {
    public MealDAO mealsDAO;
    public MealsLocalDataSource(Context context){
        AppDataBase dataBase = AppDataBase.getInstance(context);
        mealsDAO = dataBase.mealDAO();
    }
    public void insertMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("insert", "run: "+ meal);
                mealsDAO.insertMeal(meal);
            }
        }).start();

    }
    public void deleteMeal(Meal meal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealsDAO.deleteMeal(meal);
            }
        }).start();
    }
    public LiveData<List<Meal>> getMeals(){
        return mealsDAO.getMeals();
    }
    public LiveData<Boolean> isFavorite(String mealId) {
        return mealsDAO.isFavorite(mealId);
    }
}
