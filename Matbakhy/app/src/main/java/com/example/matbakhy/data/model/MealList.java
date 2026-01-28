package com.example.matbakhy.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MealList implements Parcelable {
    private List<Meal> meals;

    public MealList(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    protected MealList(Parcel in) {
        meals = new ArrayList<>();
        in.readList(meals, Meal.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(meals);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MealList> CREATOR = new Creator<MealList>() {
        @Override
        public MealList createFromParcel(Parcel in) {
            return new MealList(in);
        }

        @Override
        public MealList[] newArray(int size) {
            return new MealList[size];
        }
    };
}