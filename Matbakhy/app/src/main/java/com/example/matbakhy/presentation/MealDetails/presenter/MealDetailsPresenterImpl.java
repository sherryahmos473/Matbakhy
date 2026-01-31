package com.example.matbakhy.presentation.MealDetails.presenter;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.matbakhy.R;
import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.data.MealRepository;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
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
        mealRepository.insertMealInFav(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.onAddToFav(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }

    public void removeMealFromFav(Meal meal) {
        mealRepository.deleteMealsFromFav(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.removeMealFromFav(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }

    public void removeMealFromCal(Meal meal) {
        mealRepository.deleteMealsFromCal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.removeMealFromCal(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }

    @Override
    public void isFavorite(String mealId) {
        mealRepository.isFavorite(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> mealDetailsView.isFav(isFavorite != null && isFavorite),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void isCal(String mealId) {
        mealRepository.isCal(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFavorite -> mealDetailsView.isCal(isFavorite != null && isFavorite),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }
    @Override
    public void getMealOfIngredient(String ingredient) {
        mealRepository.getMealOfIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> mealDetailsView.onSuccess(meals),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }

    public void addMealToCal(Meal meal, Long date) {
        mealRepository.insertMealInCal(meal,date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                () -> mealDetailsView.onAddToCal(),
                throwable -> mealDetailsView.onFailure(throwable.getMessage())
        );
    }
    public String extractYouTubeVideoId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        if (url.contains("v=")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) {
                String videoId = parts[1].split("&")[0];
                if (!videoId.isEmpty()) {
                    return videoId;
                }
            }
        }

        if (url.contains("youtu.be/")) {
            String[] parts = url.split("youtu.be/");
            if (parts.length > 1) {
                String videoId = parts[1].split("\\?")[0];
                if (!videoId.isEmpty()) {
                    return videoId;
                }
            }
        }

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
        authRepository.logoutWithBackup().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealDetailsView.navToLogin(),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );

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
