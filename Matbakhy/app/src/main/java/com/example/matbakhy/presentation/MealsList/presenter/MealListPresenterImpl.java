package com.example.matbakhy.presentation.MealsList.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.matbakhy.data.MealRepository; // Make sure import is correct
import com.example.matbakhy.presentation.MealsList.views.MealListView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MealListPresenterImpl implements MealListPresenter {
    private MealListView view;
    private MealRepository repository;

    public MealListPresenterImpl(Context context) {
        this.repository = new MealRepository(context);
    }

    @Override
    public void attachView(MealListView view) {
        this.view = view;
    }


    @Override
    public void detachView() {
        this.view = null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMealByName(String name) {
        repository.getMealByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            Log.d("TAG", "getMealByName: " +mealList.get(0));
                            if (mealList != null && !mealList.isEmpty() && mealList.get(0) != null && mealList.get(0).getArea() != null && mealList.get(0).getCategory() != null && mealList.get(0).getInstructions() != null) {
                                view.onClickMeal(mealList.get(0));
                            } else {
                                view.onFailure("This Meal Is Not Available");
                            }
                        },
                        throwable -> {
                            if (throwable instanceof NullPointerException &&
                                    throwable.getMessage() != null &&
                                    throwable.getMessage().contains("mapper function returned a null value")) {
                                view.onFailure("Meal data could not be loaded");
                            } else {
                                view.onFailure(throwable.getMessage());
                            }
                        }
                );
    }
}