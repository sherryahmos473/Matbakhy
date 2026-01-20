package com.example.matbakhy.presentation.Meals.presenter;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Meals.view.HomeView;

import java.util.List;

public class HomePresenterImpl implements HomePresenter{
    private MealRepositry mealRepositry;
    private HomeView homeView;
    public HomePresenterImpl(){
        mealRepositry = new MealRepositry();
    }

    @Override
    public void attachView(HomeView view) {
        this.homeView = view;
    }

    @Override
    public void detachView() {
        this.homeView = null;
    }

    @Override
    public void getMealOfTheDay() {
        mealRepositry.getMealOfTheDay(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                homeView.getMealOfTheDay(mealList.get(0));
            }

            @Override
            public void onFailure(String errorMessage) {
                homeView.onFailure(errorMessage);
            }
        });
    }

    @Override
    public void getAllCategories() {
        mealRepositry.getAllCategories(new CategoriesRemoteResponse() {
            @Override
            public void onSuccess(List<Category> categories) {
                homeView.getAllCategories(categories);
            }
            @Override
            public void onFailure(String errorMessage) {
                homeView.onFailure(errorMessage);
            }
        });
    }
}
