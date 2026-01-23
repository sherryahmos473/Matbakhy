package com.example.matbakhy.presentation.Favorite.view;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.matbakhy.presentation.Favorite.presenter.FaroritePresenter;
import com.example.matbakhy.presentation.Favorite.presenter.FaroritePresenterImpl;
import com.example.matbakhy.presentation.Favorite.presenter.FavoriteView;
import com.example.matbakhy.presentation.MealsList.views.MealClickListener;

import java.util.List;


public class FavoriteFragment extends Fragment implements  FavoriteOnClickListner, FavoriteView, MealClickListener {

    View view;
    //ProgressBar progressBar;
    FavoriteAdapter favoriteAdapter;
    FaroritePresenter faroritePresenter;

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

        //progressBar = view.findViewById(R.id.progress_circular);
        recyclerView = view.findViewById(R.id.recycleView);


        favoriteAdapter = new FavoriteAdapter(this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoriteAdapter);

        faroritePresenter = new FaroritePresenterImpl(getContext(), this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FavoriteFragment", "onViewCreated called");
        view.post(() -> {
            Log.d("FavoriteFragment", "Forcing layout measurement");
            recyclerView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );

            Log.d("FavoriteFragment", "After measure - Height: " + recyclerView.getMeasuredHeight());

            recyclerView.requestLayout();
        });
        faroritePresenter.getFavMeal()
                .observe(getViewLifecycleOwner(), meals -> {
                    Log.d("FavoriteFragment", "LiveData received: " +
                            (meals != null ? meals.size() : "null") + " items");

                    if (meals != null && !meals.isEmpty()) {
                        for (Meal meal : meals) {
                            Log.d("FavoriteFragment", "Meal: " + meal.getName() + " ID: " + meal.getId());
                        }
                    }

                    favoriteAdapter.setMealList(meals);

                    Log.d("FavoriteFragment", "RecyclerView visibility: " + recyclerView.getVisibility());
                    Log.d("FavoriteFragment", "RecyclerView height: " + recyclerView.getHeight());
                    Log.d("FavoriteFragment", "RecyclerView isShown: " + recyclerView.isShown());
                });
    }

    @Override
    public void onMealDeleted() {
        new MyToast(getContext(),"Meal Deleted");
    }

    @Override
    public void deleteFav(Meal meal) {
        faroritePresenter.deleteMeal(meal);
    }

    @Override
    public void onMealClick(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal_object", meal);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_favoriteFragment_to_mealDetailsFragment, bundle);
    }
}