package com.example.matbakhy.data;

import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.datasources.local.MealsLocalDataSource;
import com.example.matbakhy.data.datasources.remote.FirebaseBackupService;
import com.example.matbakhy.data.datasources.remote.FirebaseServices;
import com.example.matbakhy.data.datasources.remote.MealDataSource;
import com.example.matbakhy.data.datasources.remote.Network;
import com.example.matbakhy.data.model.Area;
import com.example.matbakhy.data.model.Category;
import com.example.matbakhy.data.model.Ingredient;
import com.example.matbakhy.data.model.Meal;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class MealRepository {
    private final MealDataSource mealServices;
    private final MealsLocalDataSource mealsLocalDataSource;
    private final FirebaseBackupService firebaseBackupService;

    public MealRepository(Context context){
        mealServices = new MealDataSource(context);
        mealsLocalDataSource = new MealsLocalDataSource(context);
        firebaseBackupService = new FirebaseBackupService();
    }
    public Single<List<Meal>> getMealOfTheDay(){
        return mealServices.getMealOfTheDay();
    }
    public Single<List<Meal>> getMealByName(String name){
        return mealServices.getMealByName(name);
    }
    public Single<List<Meal>> getMealByFLetter(String FLetter){
        return mealServices.getMealByFLetter(FLetter);
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
        return mealsLocalDataSource.insertMeal(meal).andThen(syncSingleMealToFirebase(meal));
    }
    public Completable insertMeal(Meal meal){
        return mealsLocalDataSource.insertMeal(meal).andThen(syncSingleMealToFirebase(meal));

    }
    public Completable insertMealInCal(Meal meal,Long cal){
        meal.setPlanned(true);
        meal.setPlanDate(cal);

        return mealsLocalDataSource.insertMeal(meal)
                .andThen(syncSingleMealToFirebase(meal))
                .doOnComplete(() -> Log.d("DEBUG", "Insert successful!"))
                .doOnError(error -> Log.e("DEBUG", "Insert failed: " + error.getMessage()));

    }
    public Completable CleanOldPlannedMeals(){
        return mealsLocalDataSource.cleanOldPlannedMeals();
    }
    public Completable deleteMealsFromFav(Meal meal){
        meal.setFavorite(false);
        return mealsLocalDataSource.deleteFavMeal(meal)
                .andThen(syncSingleMealToFirebase(meal));
    }
    public Completable deleteMealsFromCal(Meal meal){
        meal.setPlanned(false);
        meal.setPlanDate(null);
        return mealsLocalDataSource.deleteCalMeal(meal)
                .andThen(syncSingleMealToFirebase(meal));
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
    private Completable syncSingleMealToFirebase(Meal meal) {
        return Completable.create(emitter -> {
            firebaseBackupService.syncSingleMeal(meal);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
