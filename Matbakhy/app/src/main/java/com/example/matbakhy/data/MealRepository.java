package com.example.matbakhy.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.matbakhy.data.callbacks.AuthCallback;
import com.example.matbakhy.data.callbacks.BackupCallback;
import com.example.matbakhy.data.callbacks.LogoutCallback;
import com.example.matbakhy.data.callbacks.SimpleCallback;
import com.example.matbakhy.data.datasources.local.AppDataBase;
import com.example.matbakhy.data.datasources.local.MealsLocalDataSource;
import com.example.matbakhy.data.datasources.local.SharedPref;
import com.example.matbakhy.data.datasources.local.SharedPrefServices;
import com.example.matbakhy.data.datasources.remote.FirebaseServices;
import com.example.matbakhy.data.datasources.remote.MealDataSource;
import com.example.matbakhy.data.datasources.remote.MealRestoreManager;
import com.example.matbakhy.data.datasources.remote.Network;
import com.example.matbakhy.data.model.Area;
import com.example.matbakhy.data.model.Category;
import com.example.matbakhy.data.model.Ingredient;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.datasources.remote.MealBackupManager;
import com.example.matbakhy.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;


public class MealRepository {
    private MealDataSource mealServices;
    private MealsLocalDataSource mealsLocalDataSource;
    private final FirebaseServices firebaseServices;


    public MealRepository(Context context){
        mealServices = new MealDataSource();
        mealsLocalDataSource = new MealsLocalDataSource(context);
        this.firebaseServices = Network.getInstance().firebaseServices;
        this.firebaseServices.initialize(context);
    }
    public Single<List<Meal>> getMealOfTheDay(){
        return mealServices.getMealOfTheDay();
    }
    public Single<List<Meal>> getMealByName(String name){
        return mealServices.getMealByName(name);
    }
    public Single<List<Meal>> getMealByFLetter(String FLetter){
        return mealServices.getMealByName(FLetter);
    }
    public Single<List<Meal>> getMealOfCategory(String category){
        return mealServices.getMealOfCategory(category);
    }
    public Single<List<Meal>> getMealOfCountry(String country){
        return mealServices.getMealOfCountry(country);
    }
    public Single<List<Meal>> getMealOfIngredient(String ingredient){
        return mealServices.getMealOfIngredient(ingredient);
    }
    public Single<List<Category>> getAllCategories(){
        return mealServices.getAllCategories();
    }
    public Single<List<Area>> getAllCountries(){
        return mealServices.getAllCountries();
    }
    public Single<List<Ingredient>> getAllIngredients(){
        return mealServices.getAllIngredients();
    }
    public Single<List<Meal>> getFavMeals() {
        return mealsLocalDataSource.getFavMeals();
    }
    public Single<List<Meal>> getCalMeals() {
        return mealsLocalDataSource.getCalMeals();
    }
    public Completable insertMealInFav(Meal meal){
        meal.setFavorite(true);
        return mealsLocalDataSource.insertMeal(meal);


    }
    public Completable insertMeal(Meal meal){
        return mealsLocalDataSource.insertMeal(meal);

    }
    public Completable insertMealInCal(Meal meal,String cal){
        meal.setPlanned(true);
        meal.setPlanDate(cal);
        return mealsLocalDataSource.insertMeal(meal);

    }
    public Completable deleteMealsFromFav(Meal meal){
        return mealsLocalDataSource.deleteFavMeal(meal);
    }
    public Completable deleteMealsFromCal(Meal meal){
        return mealsLocalDataSource.deleteCalMeal(meal);
    }
    public Maybe<Boolean> isFavorite(String mealId){
        return mealsLocalDataSource.isFavorite(mealId);
    }
    public Maybe<Boolean> isCal(String mealId){
        return mealsLocalDataSource.isCal(mealId);
    }
    public Single<List<Meal>> getAllMealsFromLocal() {
        return mealsLocalDataSource.getAllMeals();
    }
    public Completable clearAllLocalMeals() {
        return mealsLocalDataSource.deleteAllMeals();
    }
}
