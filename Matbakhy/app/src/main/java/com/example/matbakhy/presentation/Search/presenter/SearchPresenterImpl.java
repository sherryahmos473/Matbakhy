package com.example.matbakhy.presentation.Search.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;

import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Ingredient;
import com.example.matbakhy.presentation.Search.view.SearchView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> searchView.getCategories(categories),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void getAllCountries() {
        mealRepository.getAllCountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries -> searchView.getCountries(countries),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getAllIngredients() {
        mealRepository.getAllIngredients()
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getMealOfCountry(String countryName) {
        mealRepository.getMealOfCountry(countryName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getMealOfIngredient(String ingredient) {
        mealRepository.getMealOfIngredient(ingredient)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onSuccess(meals),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }

    @Override
    public void getMealByName(String name) {
        mealRepository.getMealByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.getMealByName(meals.getFirst()),
                        throwable -> searchView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void getMealByFLetter(String FLetter) {
        mealRepository.getMealByFLetter(FLetter)
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
