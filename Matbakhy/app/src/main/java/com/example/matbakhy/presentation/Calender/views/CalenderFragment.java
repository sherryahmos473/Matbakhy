package com.example.matbakhy.presentation.Calender.views;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.matbakhy.R;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.Calender.presenter.CalenderPresenter;
import com.example.matbakhy.presentation.Calender.presenter.CalenderPresenterImpl;
import com.example.matbakhy.presentation.MealsList.views.MealClickListener;


public class CalenderFragment extends Fragment  implements CalenderView , CalenderOnClickListener, MealClickListener {
    View view;
    ProgressBar progressBar;
    CalenderAdapter calenderAdapter;
    CalenderPresenter calenderPresenter;
    private View emptyStateView;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calender, container, false);
        progressBar = view.findViewById(R.id.progress_circular);
        progressBar.setVisibility(VISIBLE);
        recyclerView = view.findViewById(R.id.recycleView);
        emptyStateView = view.findViewById(R.id.emptyView);
        calenderAdapter = new CalenderAdapter(this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(calenderAdapter);

        calenderPresenter = new CalenderPresenterImpl(getContext(), this);

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
        calenderPresenter.getCalMeal()
                .observe(getViewLifecycleOwner(), meals -> {
                    if (meals != null && !meals.isEmpty()) {
                        hideEmptyState();
                    }else{
                        showEmptyState();
                    }
                    progressBar.setVisibility(View.GONE);
                    calenderAdapter.setMealList(meals);
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
        calenderPresenter.deleteMeal(meal);
    }

    @Override
    public void onMealClick(Meal meal) {

        CalenderFragmentDirections.ActionCalenderFragmentToMealDetailsFragment action = CalenderFragmentDirections.actionCalenderFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(view).navigate(action);
    }
}