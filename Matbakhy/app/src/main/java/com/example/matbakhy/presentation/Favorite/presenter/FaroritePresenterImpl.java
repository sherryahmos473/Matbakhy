package com.example.matbakhy.presentation.Favorite.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public class FaroritePresenterImpl implements FaroritePresenter{
    MealRepositry mealRepositry;
    FavoriteView favoriteView;
    public FaroritePresenterImpl(Context context, FavoriteView favoriteView){
        mealRepositry = new MealRepositry(context);
        this.favoriteView = favoriteView;
    }
    @Override
    public LiveData<List<Meal>> getFavMeal() {
        return mealRepositry.getAllLocalMeals();
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealRepositry.deleteMealsFromFav(meal);
        favoriteView.onMealDeleted();
    }
}
