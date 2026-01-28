package com.example.matbakhy.presentation.Favorite.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Meal;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class FavoritePresenterImpl implements FavoritePresenter {
    MealRepository mealRepository;
    FavoriteView favoriteView;
    public FavoritePresenterImpl(Context context, FavoriteView favoriteView){
        mealRepository = new MealRepository(context);
        this.favoriteView = favoriteView;
    }
    @Override
    public void getFavMeal() {
        mealRepository.getFavMeals().observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList ->favoriteView.getMeals(mealList),
                        throwable -> favoriteView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealRepository.deleteMealsFromFav(meal).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> favoriteView.onMealDeleted(),
                throwable -> favoriteView.onFailure(throwable.getMessage())
        );
        getFavMeal();
    }
}
