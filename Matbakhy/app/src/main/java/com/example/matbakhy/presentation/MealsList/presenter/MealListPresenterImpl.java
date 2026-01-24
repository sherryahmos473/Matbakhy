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
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getMealByName(String name) {
        if (repository == null) {
            if (view != null) {
                view.onFailure("Repository not initialized");
            }
            return;
        }

        repository.getMealByName(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                if (view != null) {
                    if (mealList != null && !mealList.isEmpty()) {
                        view.onClickMeal(mealList.get(0));
                    } else {
                        view.onFailure("No meals found with name: " + name);
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    view.onFailure(errorMessage);
                }
            }
        }, name);
    }
}