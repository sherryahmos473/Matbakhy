package com.example.matbakhy.presentation.MealDetails.presenter;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsView;

import java.util.List;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{
    MealRepositry mealRepositry;
    MealDetailsView mealDetailsView;
    AuthRepository authRepository;
    public MealDetailsPresenterImpl(Context context,MealDetailsView mealDetailsView){
        mealRepositry = new MealRepositry(context);
        authRepository = new AuthRepository(context);
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
    public void removeMealFromCal(Meal meal) {
        mealRepositry.deleteMealsFromCal(meal);
        mealDetailsView.removeMealFromCal();
    }

    @Override
    public void isFavorite(String mealId) {
        mealRepositry.isFavorite(mealId).observe(mealDetailsView.getLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavorite) {
                if (mealDetailsView != null) {
                    mealDetailsView.isFav(isFavorite != null && isFavorite);
                }
            }
        });
    }
    @Override
    public void isCal(String mealId) {
        mealRepositry.isCal(mealId).observe(mealDetailsView.getLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPlanned) {
                if (mealDetailsView != null) {
                    mealDetailsView.isCal(isPlanned != null && isPlanned);
                }
            }
        });
    }
    @Override
    public void getMealOfIngredient(String ingredient) {
        mealRepositry.getMealOfIngredient(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                Log.d("SearchFragment", "onSuccess: "+ mealList.size());
                mealDetailsView.onSuccess(mealList);
            }
            @Override
            public void onFailure(String errorMessage) {
                mealDetailsView.onFailure(errorMessage);
            }
        }, ingredient);
    }

    @Override
    public void addMealToCal(Meal meal, String date) {
        mealRepositry.insertMealInCal(meal,date);
        mealDetailsView.onAddToCal();
    }
    public String extractYouTubeVideoId(String url) {
        if (url.contains("v=")) return url.split("v=")[1].split("&")[0];
        return null;
    }

    @Override
    public boolean isGuest() {
        return authRepository.isGuest();

    }

    @Override
    public void login() {
        mealDetailsView.navToLogin();
    }
}
