package com.example.matbakhy.presentation.Meals.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.dataSource.AreaRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.CategoriesRemoteResponse;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.data.auth.callbacks.LogoutCallback;
import com.example.matbakhy.presentation.Meals.view.HomeView;

import java.util.List;

public class HomePresenterImpl implements HomePresenter{
    private MealRepositry mealRepositry;
    private final AuthRepository authRepository;
    private HomeView homeView;

    public HomePresenterImpl(Context context){

        mealRepositry = new MealRepositry(context);
        this.authRepository = new AuthRepository(context);
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
    public void getMealOfCategory(String categoryName) {
        if (mealRepositry == null) return;

        mealRepositry.getMealOfCategory(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (homeView != null) {
                    Log.d("Meals", "onSuccess: "+ meals.size());
                    homeView.onSuccess(meals);
                }
            }
            @Override
            public void onFailure(String error) {
                if (homeView != null) {
                    homeView.onFailure(error);
                }
            }
        }, categoryName);
    }

    @Override
    public void getMealOfCountry(String countryName) {
        if (mealRepositry == null) return;

        mealRepositry.getMealOfCountry(new MealRemoteResponse() {
            @Override
            public void onSuccess(List<Meal> meals) {
                if (homeView != null) {
                    Log.d("Meals", "onSuccess: "+ meals.size());
                    homeView.onSuccess(meals);
                }
            }
            @Override
            public void onFailure(String error) {
                if (homeView != null) {
                    homeView.onFailure(error);
                }
            }
        }, countryName);
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
    @Override
    public void getAllCountries() {
        mealRepositry.getAllCountries(new AreaRemoteResponse() {
            @Override
            public void onSuccess(List<Area> areas) {
                homeView.getAllCountries(areas);
            }
            @Override
            public void onFailure(String errorMessage) {
                homeView.onFailure(errorMessage);
            }
        });
    }

    public void logout() {
        authRepository.logoutWithBackup(new LogoutCallback() {
            @Override
            public void onLogoutComplete(boolean backupSuccess, String message) {
                homeView.navigateToLogin();
            }
        });
    }


    public boolean isNetworkAvailable(Context context ) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            android.net.NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    @Override
    public boolean isGuest() {
        return authRepository.isGuest();
    }


}
