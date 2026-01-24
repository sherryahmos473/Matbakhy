package com.example.matbakhy.presentation.Search.view;

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
import android.widget.Button;
import android.widget.Toast;
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
import com.example.matbakhy.presentation.Search.presenter.SearchPresenter;
import com.example.matbakhy.presentation.Search.presenter.SearchPresenterImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements CategoryListener, CountryListener, SearchView, IngredientListener {
    private static final String TAG = "SearchFragment";
    View view;
    ChipGroup filter;
    RecyclerView filterList;
    CategoryListAdapter categoryListAdapter;
    CountryListAdapter countryListAdapter;
    IngredientListAdapter ingredientListAdapter;
    SearchPresenter searchPresenter;
    Button searchBtn;
    TextInputEditText search;

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

        Log.d(TAG, "onViewCreated called");

        filter = view.findViewById(R.id.chipGroup);
        filterList = view.findViewById(R.id.filterList);
        searchBtn = view.findViewById(R.id.button);
        search = view.findViewById(R.id.textInputEditText);
        searchPresenter = new SearchPresenterImpl(requireContext());
        searchPresenter.attachView(this);
        categoryListAdapter = new CategoryListAdapter(this);
        countryListAdapter = new CountryListAdapter(this);
        ingredientListAdapter = new IngredientListAdapter(this);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        filterList.setLayoutManager(LayoutManager);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPresenter.getMealByName(search.getText().toString());
            }
        });
        setupChips();
    }

    private void setupChips() {
        Chip categoryChip = view.findViewById(R.id.category);
        Chip countryChip = view.findViewById(R.id.country);
        Chip ingredientChip = view.findViewById(R.id.ingredient);


        if (categoryChip != null) {
            categoryChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Category chip clicked");
                    searchPresenter.getAllCategories();
                }
            });

        } else {
            Toast.makeText(requireContext(), "Category chip not found", Toast.LENGTH_SHORT).show();
        }

        if (countryChip != null) {
            countryChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Country chip clicked");
                    searchPresenter.getAllCountries();
                }
            });
        } else {
            Toast.makeText(requireContext(), "Country chip not found", Toast.LENGTH_SHORT).show();
        }
        if (ingredientChip != null) {
            ingredientChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "ingredient chip clicked");
                    searchPresenter.getAllIngredients();
                }
            });
        } else {
            Toast.makeText(requireContext(), "ingredient chip not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getMealOfCategory(String category) {
        try {
            searchPresenter.getMealOfCategory(category);
        } catch (Exception e) {
            Log.e(TAG, "Error in getMealOfCategory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void getMealOfCountry(String country) {
        try {
            searchPresenter.getMealOfCountry(country);
        } catch (Exception e) {
            Log.e(TAG, "Error in getMealOfCountry: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void getCategories(List<Category> categories) {
        filterList.setAdapter(categoryListAdapter);
        categoryListAdapter.setCategoryList(categories);
    }

    @Override
    public void getCountries(List<Area> countries) {
        filterList.setAdapter(countryListAdapter);
        countryListAdapter.setCountryList(countries);

    }

    @Override
    public void getIngredients(List<String> ingredients) {
        Log.d("SearchFragment", "getIngredients: "+ ingredients.size());
        filterList.setAdapter(ingredientListAdapter);
        ingredientListAdapter.setIngredients(ingredients);
    }

    @Override
    public void getMealByName(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal_object", meal);
        Navigation.findNavController(requireView())
                .navigate(R.id.action_searchFragment_to_mealDetailsFragment, bundle);
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("meals", new ArrayList<>(mealList));
        Log.d("SearchFragment", "onSuccess: " + mealList.size());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_searchFragment_to_mealListFragment, bundle);
    }

    @Override
    public void getMealOfIngredient(String ingredient) {
        searchPresenter.getMealOfIngredient(ingredient);
    }
}