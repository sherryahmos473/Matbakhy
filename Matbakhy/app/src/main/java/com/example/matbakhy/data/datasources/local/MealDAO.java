package com.example.matbakhy.data.datasources.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MealDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(Meal meal);

    @Query("SELECT * FROM meals WHERE is_favorite = 1")
    Single<List<Meal>> getFavMeals();

    @Query("SELECT * FROM meals WHERE is_planned = 1")
    Single<List<Meal>> getCalMeals();

    @Query("UPDATE meals SET is_favorite = 0 WHERE id = :mealId")
    Completable deleteFavMeal(String mealId);

    @Query("UPDATE meals SET is_planned = 0 WHERE id = :mealId")
    Completable deleteCalMeal(String mealId);

    @Query("SELECT is_favorite FROM meals WHERE id = :mealId")
    Maybe<Boolean> isFavorite(String mealId);

    @Query("SELECT is_planned FROM meals WHERE id = :mealId")
    Maybe<Boolean> isCal(String mealId);

    @Query("DELETE FROM meals")
    Completable deleteAllMeals();

    @Query("DELETE FROM meals WHERE is_favorite = 0 AND is_planned = 0")
    Completable deleteIfNotFavoriteAndNotPlanned();

    @Query("UPDATE meals SET is_planned = 0, plan_date = NULL, plan_date = 0 " +
            "WHERE is_planned = 1 AND plan_date < :todayTimestamp")
    Completable deletePlannedMealsBeforeToday(long todayTimestamp);
    @Query("SELECT * FROM meals")
    Single<List<Meal>> getAllMeals();
}