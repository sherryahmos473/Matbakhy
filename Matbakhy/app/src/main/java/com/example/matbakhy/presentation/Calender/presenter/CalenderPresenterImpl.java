package com.example.matbakhy.presentation.Calender.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.presentation.Calender.views.CalenderView;

import java.util.List;

public class CalenderPresenterImpl implements CalenderPresenter {
    MealRepository mealRepository;
    CalenderView calenderView;
    public CalenderPresenterImpl(Context context, CalenderView calenderView){
        mealRepository = new MealRepository(context);
        this.calenderView = calenderView;
    }
    @Override
    public LiveData<List<Meal>> getCalMeal() {
        return mealRepository.getCalMeals();
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealRepository.deleteMealsFromCal(meal);
        calenderView.onMealDeleted();
    }
}
