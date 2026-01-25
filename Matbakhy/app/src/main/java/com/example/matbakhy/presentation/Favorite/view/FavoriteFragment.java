package com.example.matbakhy.presentation.Favorite.view;

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
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.Favorite.presenter.FavoritePresenter;
import com.example.matbakhy.presentation.Favorite.presenter.FavoritePresenterImpl;
import com.example.matbakhy.presentation.Favorite.presenter.FavoriteView;
import com.example.matbakhy.presentation.Meals.view.HomeFragmentDirections;
import com.example.matbakhy.presentation.MealsList.views.MealClickListener;


public class FavoriteFragment extends Fragment implements FavoriteOnClickListener, FavoriteView, MealClickListener {

    View view;
    ProgressBar progressBar;
    FavoriteAdapter favoriteAdapter;
    FavoritePresenter faroritePresenter;
    private View emptyStateView;

    RecyclerView recyclerView;

    public FavoriteFragment() {
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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

        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        progressBar = view.findViewById(R.id.progress_circular);
        progressBar.setVisibility(VISIBLE);
        recyclerView = view.findViewById(R.id.recycleView);

        emptyStateView = view.findViewById(R.id.emptyView);
        favoriteAdapter = new FavoriteAdapter(this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoriteAdapter);

        faroritePresenter = new FavoritePresenterImpl(getContext(), this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(() -> {
            recyclerView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            recyclerView.requestLayout();
        });
        faroritePresenter.getFavMeal()
                .observe(getViewLifecycleOwner(), meals -> {
                    if (meals != null && !meals.isEmpty()) {
                        hideEmptyState();

                        for (Meal meal : meals) {
                            Log.d("FavoriteFragment", "Meal: " + meal.getName() + " ID: " + meal.getId());
                        }
                    }else{
                        showEmptyState();
                    }
                    progressBar.setVisibility(View.GONE);
                    favoriteAdapter.setMealList(meals);

                });
    }
    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyStateView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateView.setVisibility(View.GONE);
    }
    @Override
    public void onMealDeleted() {
        new MySnackBar(view,"Meal Deleted");
    }

    @Override
    public void deleteFav(Meal meal) {
        faroritePresenter.deleteMeal(meal);
    }

    @Override
    public void onMealClick(Meal meal) {
        FavoriteFragmentDirections.ActionFavoriteFragmentToMealDetailsFragment action = FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(view).navigate(action);
    }
}