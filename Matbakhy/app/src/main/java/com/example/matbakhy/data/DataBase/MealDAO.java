package com.example.matbakhy.data.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

@Dao
public interface MealDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal(Meal meal);

    @Query("SELECT * FROM meals")
    LiveData<List<Meal>> getMeals();
    @Delete
    void deleteMeal(Meal meal);
    @Query("SELECT is_favorite FROM meals WHERE id = :mealId")
    LiveData<Boolean> isFavorite(String mealId);
}
