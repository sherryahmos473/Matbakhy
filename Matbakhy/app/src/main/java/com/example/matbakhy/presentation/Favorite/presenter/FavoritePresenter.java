package com.example.matbakhy.presentation.Favorite.presenter;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface FavoritePresenter {
    LiveData<List<Meal>> getFavMeal();
    void deleteMeal(Meal meal);
}
