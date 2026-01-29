package com.example.matbakhy.presentation.MealDetails.presenter;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.example.matbakhy.R;
import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.callbacks.LogoutCallback;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{
    MealRepository mealRepository;
    MealDetailsView mealDetailsView;
    AuthRepository authRepository;
    protected final CompositeDisposable disposables = new CompositeDisposable();
    public MealDetailsPresenterImpl(Context context,MealDetailsView mealDetailsView){
        mealRepository = new MealRepository(context);
        authRepository = new AuthRepository(context);
        this.mealDetailsView = mealDetailsView;
    }
    public void addMealToFav(Meal meal) {
        mealRepository.insertMealInFav(meal).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.onAddToFav(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }

    public void removeMealFromFav(Meal meal) {
        mealRepository.deleteMealsFromFav(meal).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.removeMealFromFav(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }

    public void removeMealFromCal(Meal meal) {
        mealRepository.deleteMealsFromCal(meal).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.removeMealFromCal(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }

    @Override
    public void isFavorite(String mealId) {
        mealRepository.isFavorite(mealId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> mealDetailsView.isFav(isFavorite != null && isFavorite),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void isCal(String mealId) {
        mealRepository.isCal(mealId).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> mealDetailsView.isCal(isFavorite != null && isFavorite),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void getMealOfIngredient(String ingredient) {
        mealRepository.getMealOfIngredient(ingredient)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> mealDetailsView.onSuccess(meals),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }

    public void addMealToCal(Meal meal, Long date) {
        mealRepository.insertMealInCal(meal,date).observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.onAddToCal(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }
    public String extractYouTubeVideoId(String url) {
        if (url.contains("v=")) return url.split("v=")[1].split("&")[0];
        return null;
    }

    @Override
    public void isGuest() {
        disposables.add(
                authRepository.isGuest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isGuest -> {
                                    if (mealDetailsView != null && isGuest) {
                                        mealDetailsView.onGuestStatus(isGuest);
                                    }
                                },
                                error -> {
                                    if (mealDetailsView != null) {
                                        mealDetailsView.onGuestStatus(false);
                                    }
                                }
                        )
        );
    }

    @Override
    public void login() {
        authRepository.logoutWithBackup(new LogoutCallback() {
            @Override
            public void onLogoutComplete(boolean backupSuccess, String message) {
                mealDetailsView.navToLogin();
            }
        });

    }
    public void onBackClicked() {
        mealDetailsView.navigateBack();

    }

    @Override
    public void calenderOnClick(FragmentManager fragmentManager,Meal meal,boolean is_planned) {
        if(!is_planned){
            showCalender(fragmentManager,meal);
        }else {
            removeMealFromCal(meal);
        }
    }

    @Override
    public void favOnClick(Meal meal, boolean is_fav) {
        if(is_fav) {
            removeMealFromFav(meal);
        }else {
            addMealToFav(meal);
        }

    }

    private void showCalender(FragmentManager fragmentManager,Meal meal){
        CalendarConstraints calendarConstraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build();

        MaterialDatePicker<Long> materialDatePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(calendarConstraints)
                        .setTheme(R.style.MaterialDatePickerTheme)
                        .build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            onDateSelected(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),meal
            );
        });

        materialDatePicker.show(fragmentManager, "DATE_PICKER");
    }
    private void onDateSelected(int year, int month, int dayOfMonth,Meal meal) {
        int actualMonth = month + 1;
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
         selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        long timestamp = selectedDate.getTimeInMillis();

        addMealToCal(meal, timestamp);
    }
}
