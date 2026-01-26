package com.example.matbakhy.presentation.Search.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.data.Meals.model.MealList;
import com.example.matbakhy.presentation.Meals.view.CategoryListAdapter;
import com.example.matbakhy.presentation.Meals.view.CategoryListener;
import com.example.matbakhy.presentation.Meals.view.CountryListAdapter;
import com.example.matbakhy.presentation.Meals.view.CountryListener;
import com.example.matbakhy.presentation.Meals.view.HomeFragmentDirections;
import com.example.matbakhy.presentation.Meals.view.IngredientListAdapter;
import com.example.matbakhy.presentation.Meals.view.IngredientListener;
import com.example.matbakhy.presentation.MealsList.views.MealClickListener;
import com.example.matbakhy.presentation.MealsList.views.MealListAdapter;
import com.example.matbakhy.presentation.MealsList.views.MealListFragmentDirections;
import com.example.matbakhy.presentation.Search.presenter.SearchPresenter;
import com.example.matbakhy.presentation.Search.presenter.SearchPresenterImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment implements CategoryListener, CountryListener, SearchView, IngredientListener, MealClickListener {
    private static final String TAG = "SearchFragment";
    View view, noInternet;
    ChipGroup filter;
    RecyclerView filterList,recyclerView;
    CategoryListAdapter categoryListAdapter;
    CountryListAdapter countryListAdapter;
    IngredientListAdapter ingredientListAdapter;
    SearchPresenter searchPresenter;
    TextInputEditText search;
    MealListAdapter mealListAdapter;
    List<Meal> mealList;
    Button retry;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter = view.findViewById(R.id.chipGroup);
        filterList = view.findViewById(R.id.filterList);
        search = view.findViewById(R.id.textInputEditText);
        recyclerView = view.findViewById(R.id.recycleView);
        noInternet = view.findViewById(R.id.internetErrorOverlay);
        retry = view.findViewById(R.id.button);

        mealListAdapter = new MealListAdapter(new MealListAdapter.MealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                searchPresenter.getMealByName(meal.getName());
            }
        });
        searchPresenter = new SearchPresenterImpl(requireContext());
        searchPresenter.attachView(this);
        categoryListAdapter = new CategoryListAdapter(this);
        countryListAdapter = new CountryListAdapter(this);
        ingredientListAdapter = new IngredientListAdapter(this);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        filterList.setLayoutManager(LayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(mealListAdapter);
        retry.setOnClickListener(v -> retryConnection());
        retryConnection();
        setupChips();
        search();
    }
    private void showNoInternet() {
        if (noInternet != null) noInternet.setVisibility(View.VISIBLE);
    }

    private void hideNoInternet() {
        if (noInternet != null) noInternet.setVisibility(View.GONE);
    }

    private void retryConnection() {
        if (searchPresenter.isNetworkAvailable(getContext())) {
            hideNoInternet();
        }else{
            showNoInternet();
        }
    }
    private void setupChips() {
        Chip categoryChip = view.findViewById(R.id.category);
        Chip countryChip = view.findViewById(R.id.country);
        Chip ingredientChip = view.findViewById(R.id.ingredient);
        categoryChip.setOnClickListener(v -> searchPresenter.getAllCategories());
        countryChip.setOnClickListener(v -> searchPresenter.getAllCountries());
        ingredientChip.setOnClickListener(v -> searchPresenter.getAllIngredients());
    }
    void search() {
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList.setVisibility(View.GONE);
                String query = s.toString().toLowerCase().trim();
                if (query.length() == 1) {
                    searchPresenter.getMealByFLetter(query);
                    return;
                }

                Observable.fromIterable(mealList)
                        .filter(meal ->
                                meal.getName() != null &&
                                        meal.getName().toLowerCase().contains(query)
                        )
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                filteredList -> mealListAdapter.setMealList(filteredList),
                                throwable -> Log.e("Search", throwable.getMessage())
                        );
            }
        };

        search.addTextChangedListener(textWatcher);
    }

    @Override
    public void getMealOfCategory(String category) {
        searchPresenter.getMealOfCategory(category);
    }

    @Override
    public void getMealOfCountry(String country) {
        searchPresenter.getMealOfCountry(country);
    }

    @Override
    public void getCategories(List<Category> categories) {
        filterList.setVisibility(View.VISIBLE);
        filterList.setAdapter(categoryListAdapter);
        categoryListAdapter.setCategoryList(categories);
    }

    @Override
    public void getCountries(List<Area> countries) {
        filterList.setVisibility(View.VISIBLE);
        filterList.setAdapter(countryListAdapter);
        countryListAdapter.setCountryList(countries);

    }

    @Override
    public void getIngredients(List<String> ingredients) {
        filterList.setAdapter(ingredientListAdapter);
        ingredientListAdapter.setIngredients(ingredients);
    }

    @Override
    public void getMealByName(Meal meal) {
        SearchFragmentDirections.ActionSearchFragmentToMealDetailsFragment action = SearchFragmentDirections.actionSearchFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public void getMealByFLetter(List<Meal> mealList) {
        this.mealList = mealList;
        mealListAdapter.setMealList(mealList);
    }

    @Override
    public void onFailure(String errorMessage) {
        retryConnection();
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        this.mealList = mealList;
        mealListAdapter.setMealList(mealList);
    }

    @Override
    public void getMealOfIngredient(String ingredient) {
        filterList.setVisibility(View.VISIBLE);
        searchPresenter.getMealOfIngredient(ingredient);
    }

    @Override
    public void onMealClick(Meal meal) {
        SearchFragmentDirections.ActionSearchFragmentToMealDetailsFragment action = SearchFragmentDirections.actionSearchFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(view).navigate(action);
    }
}