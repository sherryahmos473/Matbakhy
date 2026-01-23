package com.example.matbakhy.presentation.Meals.presenter;

import android.content.Context;

import androidx.lifecycle.Observer;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Meals.view.MealDetailsView;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{
    MealRepositry mealRepositry;
    MealDetailsView mealDetailsView;
    public MealDetailsPresenterImpl(Context context,MealDetailsView mealDetailsView){
        mealRepositry = new MealRepositry(context);
        this.mealDetailsView = mealDetailsView;
    }
    @Override
    public void addMealToFav(Meal meal) {
        mealRepositry.insertMealInFav(meal);
        mealDetailsView.onAddToFav();
    }

    @Override
    public void removeMealFromFav(Meal meal) {
        mealRepositry.deleteMealsFromFav(meal);
        mealDetailsView.removeMealFromFav();
    }

    @Override
    public void isFavorite(String mealId) {
        if (mealDetailsView == null || mealDetailsView.getLifecycleOwner() == null) {
            return;
        }

        mealRepositry.isFavorite(mealId)
                .observe(mealDetailsView.getLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isFavorite) {
                        if (mealDetailsView != null) {
                            mealDetailsView.isFav(isFavorite != null && isFavorite);
                        }
                    }
                });
    }
}
