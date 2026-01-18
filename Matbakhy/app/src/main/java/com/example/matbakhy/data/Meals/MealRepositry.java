package com.example.matbakhy.data.Meals;

import com.example.matbakhy.data.Meals.dataSource.MealOfTheDayDataSource;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.Meals.model.MealListResponse;

import java.util.List;

public class MealRepositry {
    private MealOfTheDayDataSource mealServices;
    public MealRepositry(){
        mealServices = new MealOfTheDayDataSource();
    }
    public List<Meal> getMealOfTheDay(MealRemoteResponse callback){
        return mealServices.getMealOfTheDay(callback);
    }
}
