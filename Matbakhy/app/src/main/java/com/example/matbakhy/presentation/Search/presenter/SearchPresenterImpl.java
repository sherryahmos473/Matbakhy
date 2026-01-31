package com.example.matbakhy.presentation.Search.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Ingredient;
import com.example.matbakhy.presentation.Search.view.SearchView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenterImpl implements SearchPresenter {
    private MealRepository mealRepository;
    private SearchView searchView;
    public SearchPresenterImpl(Context context){
        mealRepository = new MealRepository(context);
    }

    @Override
    public void attachView(SearchView view) {
        this.searchView = view;
    }

    @Override
    public void detachView() {
        this.searchView = null;
    }
    public void getAllCategories() {
        mealRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> searchView.getCategories(categories),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void getAllCountries() {
        mealRepository.getAllCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries -> searchView.getCountries(countries),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getAllIngredients() {
        mealRepository.getAllIngredients()
                .subscribeOn(Schedulers.io())
                .map(ingredients -> {
                    List<String> ingredientNames = new ArrayList<>();
                    for (Ingredient ingredient : ingredients) {
                        ingredientNames.add(ingredient.getName());
                    }
                    return ingredientNames;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredients -> searchView.getIngredients(ingredients),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    public void getMealOfCategory(String categoryName) {
        mealRepository.getMealOfCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getMealOfCountry(String countryName) {
        mealRepository.getMealOfCountry(countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getMealOfIngredient(String ingredient) {
        mealRepository.getMealOfIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMealByName(String name) {
        mealRepository.getMealByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            if (mealList != null && !mealList.isEmpty() && mealList.get(0) != null && mealList.get(0).getArea() != null && mealList.get(0).getCategory() != null && mealList.get(0).getInstructions() != null) {
                                searchView.getMealByName(mealList.get(0));
                            } else {
                                searchView.onFailure("This Meal Is Not Available");
                            }
                        },
                        throwable -> {
                            if (throwable instanceof NullPointerException &&
                                    throwable.getMessage() != null &&
                                    throwable.getMessage().contains("mapper function returned a null value")) {
                                searchView.onFailure("Meal data could not be loaded");
                            } else {
                                searchView.onFailure(throwable.getMessage());
                            }
                        }
                );
    }
    @Override
    public void getMealByFLetter(String FLetter) {
        mealRepository.getMealByFLetter(FLetter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );


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
}
