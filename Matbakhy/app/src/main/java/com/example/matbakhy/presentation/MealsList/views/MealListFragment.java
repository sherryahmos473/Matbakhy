package com.example.matbakhy.presentation.MealsList.views;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.Meals.view.MealDetailsFragment;
import com.example.matbakhy.presentation.MealsList.presenter.MealListPresenter;
import com.example.matbakhy.presentation.MealsList.presenter.MealListPresenterImpl;
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

        if (getArguments() != null) {
            meals = getArguments().getParcelableArrayList("meals");
        }
        Log.d("Meals", "onCreate: " + meals.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            new MyToast(getContext(), errMessge);
        }
    }

    @Override
    public void onClickMeal(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal_object", meal);
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