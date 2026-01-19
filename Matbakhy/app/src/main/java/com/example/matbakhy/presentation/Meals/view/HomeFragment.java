package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
    TextView MealOfTheDayName, MealOfTheDayArea,MealOfTheDayCategory;
    ImageView MealOfTheDayImage;
    CardView imageCard;
    View view;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

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
        MealOfTheDayCategory = view.findViewById(R.id.mealOfTheDayCategory);
        MealOfTheDayArea = view.findViewById(R.id.mealOfTheDayArea);
        MealOfTheDayImage = view.findViewById(R.id.mealOfTheDayImage);
        imageCard = view.findViewById(R.id.mealCard);
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
        MealOfTheDayArea.setText(meal.getArea() + " " +meal.getIngredients().size() + " ingredients");
        MealOfTheDayCategory.setText(meal.getCategory());
        Glide.with(view)
                .load(meal.getThumbnail())
                .into(MealOfTheDayImage);
        imageCard.setOnClickListener(v -> navigateToMealDetails(meal));
    }
    private void navigateToMealDetails(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal_object", meal);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
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
