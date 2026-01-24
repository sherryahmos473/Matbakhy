package com.example.matbakhy.presentation.Calender.presenter;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Calender.views.CalenderView;
import com.example.matbakhy.presentation.Favorite.presenter.FavoriteView;

import java.util.List;

public class CalenderPresenterImpl implements CalenderPresenter {
    MealRepositry mealRepositry;
    CalenderView calenderView;
    public CalenderPresenterImpl(Context context, CalenderView calenderView){
        mealRepositry = new MealRepositry(context);
        this.calenderView = calenderView;
    }
    @Override
    public LiveData<List<Meal>> getCalMeal() {
        return mealRepositry.getCalMeals();
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealRepositry.deleteMealsFromCal(meal);
        calenderView.onMealDeleted();
    }
}
