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

public class MealDetailsPresenterImpl implements MealDetailsPresenter{
    MealRepository mealRepository;
    MealDetailsView mealDetailsView;
    AuthRepository authRepository;
    public MealDetailsPresenterImpl(Context context,MealDetailsView mealDetailsView){
        mealRepository = new MealRepository(context);
        authRepository = new AuthRepository(context);
        this.mealDetailsView = mealDetailsView;
    }
    public void addMealToFav(Meal meal) {
        mealRepository.insertMealInFav(meal);
        mealDetailsView.onAddToFav();
    }

    public void removeMealFromFav(Meal meal) {
        mealRepository.deleteMealsFromFav(meal);
        mealDetailsView.removeMealFromFav();
    }

    public void removeMealFromCal(Meal meal) {
        mealRepository.deleteMealsFromCal(meal);
        mealDetailsView.removeMealFromCal();
    }

    @Override
    public void isFavorite(String mealId) {
        mealRepository.isFavorite(mealId).observe(mealDetailsView.getLifecycleOwner(), new Observer<Boolean>() {
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
        mealRepository.isCal(mealId).observe(mealDetailsView.getLifecycleOwner(), new Observer<Boolean>() {
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
        mealRepository.getMealOfIngredient(ingredient)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> mealDetailsView.onSuccess(meals),
                        throwable -> mealDetailsView.onFailure(throwable.getMessage())
                );
    }

    public void addMealToCal(Meal meal, String date) {
        mealRepository.insertMealInCal(meal,date);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        addMealToCal(meal, formattedDate);
    }
}
