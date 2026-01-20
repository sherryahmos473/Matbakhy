package com.example.matbakhy.presentation.MealsList.views;

import static android.view.View.VISIBLE;

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
import android.widget.ProgressBar;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.MealsList.presenter.MealListPresenter;
import com.example.matbakhy.presentation.MealsList.presenter.MealListPresenterImpl;
import java.util.List;

public class MealListFragment extends Fragment implements MealListView{
    private String categoryName;
    private MealListPresenter mealListPresenter;
    ProgressBar progressBar;
    List<Meal> meals;
    MealListAdapter mealListAdapter;
    String ClickedMeal;
    RecyclerView recyclerView;
    View view;

    public MealListFragment() {
    }

    public static MealListFragment newInstance(String categoryName) {
        MealListFragment fragment = new MealListFragment();
        Bundle args = new Bundle();
        args.putString("category", categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meal_list, container, false);
        progressBar = view.findViewById(R.id.progress_circular);
        recyclerView = view.findViewById(R.id.recycleView);;
        progressBar.setVisibility(VISIBLE);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealListPresenter = new MealListPresenterImpl();
        mealListPresenter.attachView(this);

        if (categoryName != null) {
            progressBar.setVisibility(View.VISIBLE);
            mealListPresenter.getMealOfCategory(categoryName);
        } else {
            onFailure("No category specified");
        }
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        progressBar.setVisibility(View.GONE);
        mealListAdapter.setMealList(mealList);
        Log.d("TAG", "Meals loaded: " + mealList.size());
    }

    @Override
    public void onFailure(String errMessge) {
        progressBar.setVisibility(View.GONE);
        if (getContext() != null) {
            new MyToast(getContext(), errMessge);
        }
    }

    @Override
    public void onClickMeal(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal_object", meal);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_mealListFragment_to_mealDetailsFragment, bundle);
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
    }
    @Override
    public void onStop() {
        super.onStop();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mealListPresenter != null) {
            mealListPresenter.detachView();
        }
    }
}