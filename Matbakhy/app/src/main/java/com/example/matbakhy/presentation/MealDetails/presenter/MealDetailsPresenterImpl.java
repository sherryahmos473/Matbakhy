package com.example.matbakhy.presentation.MealDetails.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.MealRepositry;
import com.example.matbakhy.data.Meals.dataSource.MealRemoteResponse;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.data.auth.callbacks.LogoutCallback;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsFragment;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsFragmentArgs;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MealDetailsPresenterImpl implements MealDetailsPresenter{
    MealRepositry mealRepositry;
    MealDetailsView mealDetailsView;
    AuthRepository authRepository;
    public MealDetailsPresenterImpl(Context context,MealDetailsView mealDetailsView){
        mealRepositry = new MealRepositry(context);
        authRepository = new AuthRepository(context);
        this.mealDetailsView = mealDetailsView;
    }
    public void addMealToFav(Meal meal) {
        mealRepositry.insertMealInFav(meal);
        mealDetailsView.onAddToFav();
    }

    public void removeMealFromFav(Meal meal) {
        mealRepositry.deleteMealsFromFav(meal);
        mealDetailsView.removeMealFromFav();
    }

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
