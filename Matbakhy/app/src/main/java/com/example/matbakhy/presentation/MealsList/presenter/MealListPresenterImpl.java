package com.example.matbakhy.presentation.MealsList.presenter;

import android.content.Context;

import com.example.matbakhy.data.Meals.MealRepositry; // Make sure import is correct
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.MealsList.views.MealListView;

import java.util.List;

public class MealListPresenterImpl implements MealListPresenter {
    private MealListView view;
    private MealRepositry repository;

    public MealListPresenterImpl(Context context) {
        this.repository = new MealRepositry(context);
    }

    @Override
    public void attachView(MealListView view) {
        this.view = view;
    }

    @Override
    public void getMealOfCategory(String categoryName) {
        if (repository == null) return;

        repository.getMealOfCategory(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (view != null) {
                    view.onSuccess(meals);
                }
            }

            @Override
            public void onFailure(String error) {
                if (view != null) {
                    view.onFailure(error);
                }
            }
        }, categoryName);
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getMealByName(String name) {
        repository.getMealByName(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                view.onClickMeal(mealList.get(0));
            }

            @Override
            public void onFailure(String errorMessage) {
                view.onFailure(errorMessage);
            }
        }, name);
    }
}