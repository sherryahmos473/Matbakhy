package com.example.matbakhy.data.Favorite.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

@Dao
public interface MealDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavMeal(Meal meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCalMeal(Meal meal);

    @Query("SELECT * FROM meals WHERE is_favorite = 1")
    LiveData<List<Meal>> getFavMeals();
    @Query("SELECT * FROM meals WHERE is_planned = 1")
    LiveData<List<Meal>> getCalMeals();

    @Query("UPDATE meals SET is_favorite = 0 WHERE id = :mealId")
    void deleteFavMeal(String mealId);
    @Query("UPDATE meals SET is_planned = 0 WHERE id = :mealId")
    void deleteCalMeal(String mealId);

    @Query("SELECT is_favorite FROM meals WHERE id = :mealId")
    LiveData<Boolean> isFavorite(String mealId);
    @Query("SELECT is_planned FROM meals WHERE id = :mealId")
    LiveData<Boolean> isCal(String mealId);

    @Query("DELETE FROM meals")
    void deleteAllMeals();

    @Query("DELETE FROM meals WHERE id = :mealId AND is_favorite = 0 AND is_planned = 0")
    int deleteIfNotFavoriteAndNotPlanned(String mealId);


    @Query("SELECT * FROM meals")
    LiveData<List<Meal>> getAllMealsSync();
}
