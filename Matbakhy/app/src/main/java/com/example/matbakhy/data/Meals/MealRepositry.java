package com.example.matbakhy.data.Meals;

import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealOfTheDayDataSource;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public class MealRepositry {
    private MealOfTheDayDataSource mealServices;
    public MealRepositry(){
        mealServices = new MealOfTheDayDataSource();
    }
    public List<Meal> getMealOfTheDay(MealRemoteResponse callback){
        return mealServices.getMealOfTheDay(callback);
    }
    public List<Meal> getMealByName(MealRemoteResponse callback,String name){
        return mealServices.getMealByName(callback,name);
    }
    public List<Meal> getMealOfCategory(MealRemoteResponse callback, String category){
        return mealServices.getMealOfCategory(callback, category);
    }
    public List<Category> getAllCategories(CategoriesRemoteResponse callback){
        return mealServices.getAllCategories(callback);
    }

}
