package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenter;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenterImpl;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView , CategoryListener{

    HomePresenter homePresenter;
    CategoryListAdapter categoryListAdapter;
    RecyclerView categoryList;
    ProgressBar progressBar;
    TextView MealOfTheDayName, MealOfTheDayArea,MealOfTheDayCategory;
    ImageView MealOfTheDayImage;
    CardView imageCard;
    View view;
    Button btn;

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
        progressBar = view.findViewById(R.id.progress_circular);
        progressBar.setVisibility(view.VISIBLE);
        imageCard = view.findViewById(R.id.mealCard);
        btn = view.findViewById(R.id.button);
        categoryListAdapter = new CategoryListAdapter(this);
        categoryList = view.findViewById(R.id.categoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false);
        categoryList.setLayoutManager(layoutManager);
        categoryList.setAdapter(categoryListAdapter);
        homePresenter = new HomePresenterImpl(getContext());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.logout();

            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter.attachView(this);
        homePresenter.getMealOfTheDay();
        homePresenter.getAllCategories();;
    }

    @Override
    public void getMealOfTheDay(Meal meal) {
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
        bundle.putParcelable("meal_object", meal);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void onFailure(String errorMeassge) {
        new MyToast(getContext(),"Can't Load");
    }

    @Override
    public void getAllCategories(List<Category> categories) {
        Log.d("TAG", "getAllCategories: " + categories.size());
        progressBar.setVisibility(view.GONE);
        categoryListAdapter.setCategoryList(categories);
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
    }

    @Override
    public void getMealOfCategory(String category) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        Log.d("TAG", "getMealOfCategory: ");
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_mealListFragment, bundle);
    }
}
