package com.example.matbakhy.data.datasources.local;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.model.Meal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MealsLocalDataSource {
    public MealDAO mealsDAO;
    public MealsLocalDataSource(Context context){
       AppDataBase dataBase = AppDataBase.getInstance(context);
        mealsDAO = dataBase.mealDAO();
    }
    public Completable insertMeal(Meal meal){
        return mealsDAO.insertMeal(meal)
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteFavMeal(Meal meal){
        return mealsDAO.deleteFavMeal(meal.getId())
                .andThen(mealsDAO.deleteIfNotFavoriteAndNotPlanned(meal.getId()))
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteCalMeal(Meal meal){
        return mealsDAO.deleteCalMeal(meal.getId())
                .andThen(mealsDAO.deleteIfNotFavoriteAndNotPlanned(meal.getId()))
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Meal>> getFavMeals(){
        return mealsDAO.getFavMeals()
                .subscribeOn(Schedulers.io());
    }

    public Completable cleanOldPlannedMeals() {
        long today = System.currentTimeMillis();
        return mealsDAO.deletePlannedMealsBeforeToday(today)
                .subscribeOn(Schedulers.io());
    }

    public Single<List<Meal>> getCalMeals(){
        return mealsDAO.getCalMeals()
                .subscribeOn(Schedulers.io());
    }


    public Single<List<Meal>> getAllMeals(){
        return mealsDAO.getAllMeals()
                .subscribeOn(Schedulers.io());
    }

    public Maybe<Boolean> isFavorite(String mealId) {
        return mealsDAO.isFavorite(mealId)
                .subscribeOn(Schedulers.io());
    }

    public Maybe<Boolean> isCal(String mealId) {
        return mealsDAO.isCal(mealId)
                .subscribeOn(Schedulers.io());
    }

    public Completable deleteAllMeals() {
        return mealsDAO.deleteAllMeals()
                .subscribeOn(Schedulers.io());
    }
}
