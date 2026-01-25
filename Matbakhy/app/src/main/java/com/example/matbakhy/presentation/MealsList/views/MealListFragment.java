package com.example.matbakhy.presentation.MealsList.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.Meals.model.MealList;
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.MealDetails.view.MealDetailsFragmentArgs;
import com.example.matbakhy.presentation.Meals.view.HomeFragmentDirections;
import com.example.matbakhy.presentation.MealsList.presenter.MealListPresenter;
import com.example.matbakhy.presentation.MealsList.presenter.MealListPresenterImpl;

import java.util.Arrays;
import java.util.List;

public class MealListFragment extends Fragment implements MealListView{
    private List<Meal> meals;
    private MealListPresenter mealListPresenter;
    MealListAdapter mealListAdapter;
    RecyclerView recyclerView;
    TextView title;
    View view;

    public MealListFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MealListFragmentArgs args = MealListFragmentArgs.fromBundle(getArguments());
        MealList  mealList = args.getMealList();
        meals = mealList.getMeals();



        view = inflater.inflate(R.layout.fragment_meal_list, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        title = view.findViewById(R.id.category_name);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealListPresenter = new MealListPresenterImpl(getContext());
        mealListPresenter.attachView(this);

    }

    @Override
    public void onFailure(String errMessge) {
        if (getContext() != null) {
            new MySnackBar(view, errMessge);
        }
    }

    @Override
    public void onClickMeal(Meal meal) {
        MealListFragmentDirections.ActionMealListFragmentToMealDetailsFragment action = MealListFragmentDirections.actionMealListFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(view).navigate(action);
    }


    private void setupRecyclerView() {
        mealListAdapter = new MealListAdapter(new MealListAdapter.MealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                Log.d("MealListFragment", "Meal clicked: " + meal.getName());
                mealListPresenter.getMealByName(meal.getName());
            }
        });
        recyclerView.setAdapter(mealListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mealListAdapter.setMealList(meals);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mealListPresenter != null) {
            mealListPresenter.detachView();
        }
    }
}