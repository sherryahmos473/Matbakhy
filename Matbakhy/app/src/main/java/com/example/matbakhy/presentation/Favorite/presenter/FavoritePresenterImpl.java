package com.example.matbakhy.presentation.Favorite.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Meal;

import java.util.List;

public class FavoritePresenterImpl implements FavoritePresenter {
    MealRepository mealRepository;
    FavoriteView favoriteView;
    public FavoritePresenterImpl(Context context, FavoriteView favoriteView){
        mealRepository = new MealRepository(context);
        this.favoriteView = favoriteView;
    }
    @Override
    public LiveData<List<Meal>> getFavMeal() {
        return mealRepository.getFavMeals();
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealRepository.deleteMealsFromFav(meal);
        favoriteView.onMealDeleted();
    }
}
