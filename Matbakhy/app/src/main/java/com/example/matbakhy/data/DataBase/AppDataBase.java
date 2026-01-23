package com.example.matbakhy.data.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.matbakhy.data.Meals.model.Meal;

@Database(entities = {Meal.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract MealDAO mealDAO();
    private static AppDataBase instance = null;

    public static AppDataBase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"mealsdb").build();
        }
        return instance;
    }
}
