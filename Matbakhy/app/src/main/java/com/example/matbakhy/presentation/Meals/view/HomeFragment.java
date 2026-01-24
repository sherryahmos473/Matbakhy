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
import android.os.Parcelable; import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Initializable;
import com.example.matbakhy.MainActivity;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenter;
import com.example.matbakhy.presentation.Meals.presenter.HomePresenterImpl;

import java.util.ArrayList;
import java.util.List;
public class HomeFragment extends Fragment implements HomeView , CategoryListener, CountryListener {
    HomePresenter homePresenter;
    CategoryListAdapter categoryListAdapter;
    CountryListAdapter countryListAdapter;
    RecyclerView categoryList,countryList;
    ProgressBar progressBar,progressBar2;
    TextView MealOfTheDayName, MealOfTheDayArea,MealOfTheDayCategory;
    ImageView MealOfTheDayImage;
    CardView imageCard;
    View view;
    Button btn;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Initialization();
        recycleViewSetup();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                homePresenter.logout();
            } });
        return view;
    }

    private void recycleViewSetup() {
        countryListAdapter = new CountryListAdapter(this);
        countryList = view.findViewById(R.id.countryList);
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
        btn = view.findViewById(R.id.button);
        categoryListAdapter = new CategoryListAdapter(this);
        categoryList = view.findViewById(R.id.categoryList);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter.attachView(this);
        homePresenter.getMealOfTheDay();
        homePresenter.getAllCategories();
        homePresenter.getAllCountries();
    }
    @Override public void getMealOfTheDay(Meal meal)
        {
            MealOfTheDayName.setText(meal.getName());
            MealOfTheDayArea.setText(meal.getArea() + " " +meal.getIngredients().size() + " ingredients");
            MealOfTheDayCategory.setText(meal.getCategory());
            Glide.with(view) .load(meal.getThumbnail()) .into(MealOfTheDayImage);
            imageCard.setOnClickListener(v -> navigateToMealDetails(meal));
        }
    private void navigateToMealDetails(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("meal_object", meal);
        Navigation.findNavController(requireView()) .navigate(R.id.action_homeFragment_to_mealDetailsFragment, bundle);
    }
    @Override public void onFailure(String errorMeassge) {

        new MyToast(getContext(),"Can't Load");
    }
    @Override public void getAllCategories(List<Category> categories) {
        Log.d("TAG", "getAllCategories: " + categories.size());
        progressBar.setVisibility(view.GONE);
        categoryListAdapter.setCategoryList(categories);
    }
    @Override public void getAllCountries(List<Area> countries)
    { progressBar2.setVisibility(view.GONE);
        countryListAdapter.setCountryList(countries);
    }


    @Override
    public void onSuccess(List<Meal> mealList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("meals", new ArrayList<>(mealList));

        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_mealListFragment, bundle);
    }

    @Override public void navigateToLogin() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override public void onDestroy() {
        super.onDestroy(); homePresenter.detachView();
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