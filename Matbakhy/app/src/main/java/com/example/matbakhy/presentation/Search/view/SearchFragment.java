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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.presentation.Meals.view.CategoryListAdapter;
import com.example.matbakhy.presentation.Meals.view.CategoryListener;
import com.example.matbakhy.presentation.Meals.view.CountryListAdapter;
import com.example.matbakhy.presentation.Meals.view.CountryListener;
import com.example.matbakhy.presentation.Meals.view.IngredientListAdapter;
import com.example.matbakhy.presentation.Meals.view.IngredientListener;
import com.example.matbakhy.presentation.MealsList.views.MealClickListener;
import com.example.matbakhy.presentation.MealsList.views.MealListAdapter;
import com.example.matbakhy.presentation.Search.presenter.SearchPresenter;
import com.example.matbakhy.presentation.Search.presenter.SearchPresenterImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class SearchFragment extends Fragment implements CategoryListener, CountryListener, IngredientListener, SearchView, MealClickListener {
    private View root, noInternet;
    private ChipGroup chipGroup;
    private RecyclerView filterList, mealsRecycler;
    private TextInputEditText searchEditText;
    private Button retryBtn;

    private MealListAdapter mealListAdapter;
    private CategoryListAdapter categoryAdapter;
    private CountryListAdapter countryAdapter;
    private IngredientListAdapter ingredientAdapter;

    private SearchPresenter presenter;

    private List<Meal> fullMealList = new ArrayList<>();
    private boolean isFilterMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        chipGroup = view.findViewById(R.id.chipGroup);
        filterList = view.findViewById(R.id.filterList);
        mealsRecycler = view.findViewById(R.id.recycleView);
        searchEditText = view.findViewById(R.id.textInputEditText);
        noInternet = view.findViewById(R.id.internetErrorOverlay);
        retryBtn = view.findViewById(R.id.button);

        presenter = new SearchPresenterImpl(requireContext());
        presenter.attachView(this);

        mealListAdapter = new MealListAdapter(new MealListAdapter.MealClickListener() {
            @Override
            public void onMealClick(Meal meal) {

                getMealByName(meal);
            } });
        categoryAdapter = new CategoryListAdapter(this);
        countryAdapter = new CountryListAdapter(this);
        ingredientAdapter = new IngredientListAdapter(this);

        mealsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mealsRecycler.setAdapter(mealListAdapter);

        filterList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        retryBtn.setOnClickListener(v -> checkNetwork());

        setupChips();
        setupSearch();
        checkNetwork();
    }

    private void setupChips() {
        Chip category = root.findViewById(R.id.category);
        Chip country = root.findViewById(R.id.country);
        Chip ingredient = root.findViewById(R.id.ingredient);

        category.setOnClickListener(v -> {
            resetSearch();
            isFilterMode = true;
            presenter.getAllCategories();
        });

        country.setOnClickListener(v -> {
            resetSearch();
            isFilterMode = true;
            presenter.getAllCountries();
        });

        ingredient.setOnClickListener(v -> {
            resetSearch();
            isFilterMode = true;
            presenter.getAllIngredients();
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    mealListAdapter.setMealList(fullMealList);
                    return;
                }

                if (fullMealList == null || fullMealList.isEmpty()) return;

                filterList.setVisibility(View.GONE);
                isFilterMode = false;

                String query = s.toString().toLowerCase();

                Observable.fromIterable(fullMealList)
                        .filter(meal ->
                                meal.getName() != null &&
                                        meal.getName().toLowerCase().contains(query)
                        )
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mealListAdapter::setMealList);
            }
        });
    }

    private void resetSearch() {
        searchEditText.setText("");
        filterList.setVisibility(View.VISIBLE);
    }

    private void checkNetwork() {
        if (presenter.isNetworkAvailable(getContext())) {
            noInternet.setVisibility(View.GONE);
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getCategories(List<Category> categories) {
        filterList.setAdapter(categoryAdapter);
        categoryAdapter.setCategoryList(categories);
    }

    @Override
    public void getCountries(List<Area> countries) {
        filterList.setAdapter(countryAdapter);
        countryAdapter.setCountryList(countries);
    }

    @Override
    public void getIngredients(List<String> ingredients) {
        filterList.setAdapter(ingredientAdapter);
        ingredientAdapter.setIngredients(ingredients);
    }

    @Override
    public void getMealOfCategory(String category) {
        presenter.getMealOfCategory(category);
    }

    @Override
    public void getMealOfCountry(String country) {
        presenter.getMealOfCountry(country);
    }

    @Override
    public void getMealOfIngredient(String ingredient) {
        presenter.getMealOfIngredient(ingredient);
    }

    @Override
    public void getMealByFLetter(List<Meal> mealList) {
        fullMealList = mealList;
        mealListAdapter.setMealList(mealList);
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        fullMealList = mealList;
        mealListAdapter.setMealList(mealList);
    }

    @Override
    public void onFailure(String errorMessage) {
        checkNetwork();
    }

    @Override
    public void getMealByName(Meal meal) {
        SearchFragmentDirections.ActionSearchFragmentToMealDetailsFragment action =
                SearchFragmentDirections.actionSearchFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(root).navigate(action);
    }

    @Override
    public void onMealClick(Meal meal) {
        getMealByName(meal);
    }
}