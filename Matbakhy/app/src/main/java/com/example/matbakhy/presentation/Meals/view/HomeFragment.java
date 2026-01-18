package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenter;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenterImpl;

public class HomeFragment extends Fragment implements HomeView{

    HomePresenter homePresenter;
    TextView MealOfTheDayName, MealOfTheDayArea,MealOfTheDayIngredients,MealOfTheDayCategory;
    ImageView MealOfTheDayImage;
    View view;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        MealOfTheDayName = view.findViewById(R.id.mealofTheDayName);
        MealOfTheDayIngredients = view.findViewById(R.id.mealOfTheDayIngredients);
        MealOfTheDayCategory = view.findViewById(R.id.mealOfTheDayCategory);
        MealOfTheDayArea = view.findViewById(R.id.mealOfTheDayArea);
        MealOfTheDayImage = view.findViewById(R.id.mealOfTheDayImage);
        homePresenter = new HomePresenterImpl();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter.attachView(this);
        homePresenter.getMealOfTheDay();
    }

    @Override
    public void onSuccess(Meal meal) {
        MealOfTheDayName.setText(meal.getName());
        MealOfTheDayIngredients.setText(meal.getIngredients().size() + " ingredients");
        MealOfTheDayArea.setText(meal.getArea());
        MealOfTheDayCategory.setText(meal.getCategory());
        Glide.with(view)
                .load(meal.getThumbnail())
                .into(MealOfTheDayImage);
    }

    @Override
    public void onFailure(String errorMeassge) {
        new MyToast(getContext(),"Can't Load");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
    }
}
