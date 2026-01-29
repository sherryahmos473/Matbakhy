package com.example.matbakhy.presentation.Calender.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.presentation.Calender.views.CalenderView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CalenderPresenterImpl implements CalenderPresenter {
    MealRepository mealRepository;
    CalenderView calenderView;
    public CalenderPresenterImpl(Context context, CalenderView calenderView){
        mealRepository = new MealRepository(context);
        this.calenderView = calenderView;
    }
    @Override
    public void getCalMeal() {
        mealRepository.clearAllLocalMeals();
        mealRepository.getCalMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                mealList ->calenderView.getMeals(mealList),
                throwable -> calenderView.onFailure(throwable.getMessage())
        );
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealRepository.deleteMealsFromCal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> calenderView.onMealDeleted(),
                throwable -> calenderView.onFailure(throwable.getMessage())
        );
        getCalMeal();
    }
}
