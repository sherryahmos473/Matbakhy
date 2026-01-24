package com.example.matbakhy.presentation.Calender.presenter;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Meals.model.Meal;

import java.util.List;

public interface CalenderPresenter {

    LiveData<List<Meal>> getCalMeal();
    void deleteMeal(Meal meal);
}
