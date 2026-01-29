package com.example.matbakhy.presentation.MealDetails.presenter;

import androidx.fragment.app.FragmentManager;

import com.example.matbakhy.data.model.Meal;

public interface MealDetailsPresenter {
    public  void isFavorite(String mealId);
    public  void isCal(String mealId);
    void getMealOfIngredient(String ingredient);
    String extractYouTubeVideoId(String url);
    void isGuest();
    void onBackClicked();
    void calenderOnClick(FragmentManager fragmentManager,Meal meal,boolean is_planned);
    void favOnClick(Meal meal,boolean is_fav);
    void login();
}
