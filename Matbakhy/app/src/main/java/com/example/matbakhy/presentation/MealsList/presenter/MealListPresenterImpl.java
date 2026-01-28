package com.example.matbakhy.presentation.MealsList.presenter;

import android.content.Context;

import com.example.matbakhy.data.MealRepository; // Make sure import is correct
import com.example.matbakhy.presentation.MealsList.views.MealListView;

import io.reactivex.android.schedulers.AndroidSchedulers;

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

    @Override
    public void getMealByName(String name) {
        repository.getMealByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> view.onClickMeal(mealList.get(0))
                        , throwable -> view.onFailure(throwable.getMessage())
                );
    }
}