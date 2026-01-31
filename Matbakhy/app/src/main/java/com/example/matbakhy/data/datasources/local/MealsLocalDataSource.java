package com.example.matbakhy.data.datasources.local;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.model.Meal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        return mealsDAO.insertMeal(meal).doOnComplete(
                        ()-> Log.d("DEBUG","Insert successful!"+ Thread.currentThread().getName()))
                .doOnError(
                        error-> Log.e("DEBUG","Insert failed: "+error.getMessage())
                );
    }

    public Completable deleteFavMeal(Meal meal){
        return mealsDAO.deleteFavMeal(meal.getId())
                .andThen(mealsDAO.deleteIfNotFavoriteAndNotPlanned());}

    public Completable deleteCalMeal(Meal meal){
        return mealsDAO.deleteCalMeal(meal.getId())
                .andThen(mealsDAO.deleteIfNotFavoriteAndNotPlanned());
    }

    public Single<List<Meal>> getFavMeals(){
        return mealsDAO.getFavMeals();
    }

    public Completable cleanOldPlannedMeals() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long today = calendar.getTimeInMillis();
        return mealsDAO.deletePlannedMealsBeforeToday(today).andThen(mealsDAO.deleteIfNotFavoriteAndNotPlanned());
    }

    public Single<List<Meal>> getCalMeals(){
        return mealsDAO.getCalMeals();
    }


    public Single<List<Meal>> getAllMeals(){
        return mealsDAO.getAllMeals();
    }

    public Maybe<Boolean> isFavorite(String mealId) {
        return mealsDAO.isFavorite(mealId);
    }

    public Maybe<Boolean> isCal(String mealId) {
        return mealsDAO.isCal(mealId);
    }

    public Completable deleteAllMeals() {
        return mealsDAO.deleteAllMeals();
    }
}
