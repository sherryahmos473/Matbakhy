package com.example.matbakhy.presentation.Calender.presenter;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.model.Meal;

import java.util.List;

public interface CalenderPresenter {

    void getCalMeal();
    void deleteMeal(Meal meal);
}
