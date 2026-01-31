package com.example.matbakhy.presentation.Meals.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.presentation.Meals.view.HomeView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class HomePresenterImpl implements HomePresenter{
    private MealRepository mealRepository;
    private final AuthRepository authRepository;
    private HomeView homeView;
    protected final CompositeDisposable disposables = new CompositeDisposable();

    public HomePresenterImpl(Context context){

        mealRepository = new MealRepository(context);
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
    @SuppressLint("CheckResult")
    @Override
    public void getMealOfCategory(String categoryName) {
        mealRepository.getMealOfCategory(categoryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> homeView.onSuccess(meals),
                        throwable -> homeView.onFailure(throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMealOfCountry(String countryName) {
        if (mealRepository == null) return;

        mealRepository.getMealOfCountry(countryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> homeView.onSuccess(meals),
                        throwable -> homeView.onFailure(throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMealOfTheDay() {
        mealRepository.getMealOfTheDay()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> homeView.getMealOfTheDay(meals.get(0)),
                        throwable -> homeView.onFailure(throwable.getMessage())
                );
    }

    @SuppressLint("CheckResult")
    @Override
    public void getAllCategories() {
        mealRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> homeView.getAllCategories(categories),
                        throwable -> homeView.onFailure(throwable.getMessage())
                );
    }
    @SuppressLint("CheckResult")
    @Override
    public void getAllCountries() {
        mealRepository.getAllCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries -> homeView.getAllCountries(countries),
                        throwable -> homeView.onFailure(throwable.getMessage())
                );
    }

    public void logout() {
        authRepository.logoutWithBackup().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> homeView.navigateToLogin(),
                        throwable -> homeView.onFailure(throwable.getMessage())
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

    @Override
    public void isGuest() {
        disposables.add(
                authRepository.isGuest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isGuest -> {
                                    if (homeView != null) {
                                        Log.d("TAG", "isGuest: " + isGuest);
                                        homeView.onGuestStatus(isGuest);
                                    }
                                },
                                error -> {
                                    if (homeView != null) {
                                        homeView.onGuestStatus(false);
                                    }
                                }
                        )
        );
    }

}
