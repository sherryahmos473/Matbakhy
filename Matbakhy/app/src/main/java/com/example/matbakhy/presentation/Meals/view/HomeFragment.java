package com.example.matbakhy.presentation.Meals.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.matbakhy.MainActivity;
import com.example.matbakhy.R;
import com.example.matbakhy.data.model.Area;
import com.example.matbakhy.data.model.Category;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.model.MealList;

import com.example.matbakhy.presentation.Meals.presenter.HomePresenter;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenterImpl;

import java.util.List;
public class HomeFragment extends Fragment implements HomeView , CategoryListener, CountryListener {
    HomePresenter homePresenter;
    CategoryListAdapter categoryListAdapter;
    CountryListAdapter countryListAdapter;
    RecyclerView categoryList,countryList;
    ProgressBar progressBar,progressBar2;
    TextView MealOfTheDayName, MealOfTheDayArea,MealOfTheDayCategory;
    ImageView MealOfTheDayImage;
    CardView imageCard, searchCard;
    View view, noInternet;
    Button btn,retry;
    boolean isGuest;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Initialization();
        recycleViewSetup();

        return view;
    }

    private void recycleViewSetup() {
        categoryListAdapter = new CategoryListAdapter(this);
        countryListAdapter = new CountryListAdapter(this);

        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager countryLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

        categoryList.setLayoutManager(categoryLayoutManager);
        categoryList.setAdapter(categoryListAdapter);
        countryList.setLayoutManager(countryLayoutManager);
        countryList.setAdapter(countryListAdapter);
        homePresenter = new HomePresenterImpl(getContext());
    }

    private void Initialization() {
        MealOfTheDayName = view.findViewById(R.id.mealofTheDayName);
        MealOfTheDayCategory = view.findViewById(R.id.mealOfTheDayCategory);
        MealOfTheDayArea = view.findViewById(R.id.mealOfTheDayArea);
        MealOfTheDayImage = view.findViewById(R.id.mealOfTheDayImage);
        progressBar = view.findViewById(R.id.progress_circular);
        progressBar2 = view.findViewById(R.id.progress_circular2);
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        imageCard = view.findViewById(R.id.mealCard);
        btn = view.findViewById(R.id.logout);
        categoryList = view.findViewById(R.id.categoryList);
        countryList = view.findViewById(R.id.countryList);
        searchCard = view.findViewById(R.id.searchCard);
        noInternet = view.findViewById(R.id.internetErrorOverlay);
        retry = view.findViewById(R.id.button);

    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter.attachView(this);
        checkNetworkAndLoad();
        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_searchFragment);
            }
        });

        homePresenter.isGuest();
        if (isGuest) {
            btn.setText("Login");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(homePresenter.isNetworkAvailable(getContext())){
                    Log.d("Logout", "onClick: ");
                    homePresenter.logout();

                }else{
                    checkNetworkAndLoad();
                }
            } });
        retry.setOnClickListener(v -> retryConnection());
    }
    private void checkNetworkAndLoad() {
        if (homePresenter.isNetworkAvailable(getContext())) {
            hideNoInternet();
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            homePresenter.getMealOfTheDay();
            homePresenter.getAllCategories();
            homePresenter.getAllCountries();
        } else {
            showNoInternet();
        }
    }
    private void showNoInternet() {
        if (noInternet != null) noInternet.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
    }

    private void hideNoInternet() {
        if (noInternet != null) noInternet.setVisibility(View.GONE);
    }

    private void retryConnection() {
        if (homePresenter.isNetworkAvailable(getContext())) {
            hideNoInternet();
            checkNetworkAndLoad();
        }
    }
    @Override public void getMealOfTheDay(Meal meal)
    {
            hideNoInternet();
            MealOfTheDayName.setText(meal.getName());
            MealOfTheDayArea.setText(meal.getArea() + " " +meal.getIngredients().size() + " ingredients");
            MealOfTheDayCategory.setText(meal.getCategory());
            Glide.with(view) .load(meal.getThumbnail()) .into(MealOfTheDayImage);
            imageCard.setOnClickListener(v -> navigateToMealDetails(meal));
    }
    private void navigateToMealDetails(Meal meal) {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(meal);
        Navigation.findNavController(view).navigate(action);
    }
    @Override public void onFailure(String errorMeassge) {
        showNoInternet();
    }
    @Override public void getAllCategories(List<Category> categories) {
        hideNoInternet();
        progressBar.setVisibility(view.GONE);
        categoryListAdapter.setCategoryList(categories);
    }
    @Override public void getAllCountries(List<Area> countries)
    {
        hideNoInternet();
        progressBar2.setVisibility(view.GONE);
        countryListAdapter.setCountryList(countries);
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        hideNoInternet();
        MealList mealsList = new MealList(mealList);
        HomeFragmentDirections.ActionHomeFragmentToMealListFragment action = HomeFragmentDirections.actionHomeFragmentToMealListFragment(mealsList);
        Navigation.findNavController(view).navigate(action);
    }

    @Override public void navigateToLogin() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onGuestStatus(boolean isGuest) {
        this.isGuest = isGuest;
        if (isGuest) {
            btn.setText("Login");
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        homePresenter.detachView();
    }
    @Override public void getMealOfCategory(String category)
    {
        homePresenter.getMealOfCategory(category);
    }

    @Override
    public void getMealOfCountry(String country) {
        homePresenter.getMealOfCountry(country);
    }
}