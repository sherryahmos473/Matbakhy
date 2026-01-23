package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import com.bumptech.glide.Glide;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Meal;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.Meals.presenter.MealDetailsPresenter;
import com.example.matbakhy.presentation.Meals.presenter.MealDetailsPresenterImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MealDetailsFragment extends Fragment implements MealDetailsView {

    private Meal meal;
    private ImageView mealImage;
    private TextView mealName, mealCategory, mealArea, mealInstructions, mealIngredients;
    private FloatingActionButton favbtn;
    private MealDetailsPresenter mealDetailsPresenter;
    private boolean isFavorite = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);

        mealImage = view.findViewById(R.id.mealOfTheDayImage);
        mealName = view.findViewById(R.id.mealofTheDayName);
        mealCategory = view.findViewById(R.id.mealCategory);
        mealArea = view.findViewById(R.id.mealOfTheDayArea);
        mealInstructions = view.findViewById(R.id.instructions);
        mealIngredients = view.findViewById(R.id.ingredients);
        favbtn = view.findViewById(R.id.fabFavorite);

        mealDetailsPresenter = new MealDetailsPresenterImpl(getContext(), this);

        favbtn.setOnClickListener(v -> {
            if (meal != null) {
                if (!isFavorite) {
                    mealDetailsPresenter.addMealToFav(meal);
                    checkIsFav();
                } else {
                    mealDetailsPresenter.removeMealFromFav(meal);
                    checkIsFav();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("MealDetailsCrash", "Crash in details fragment");

        if (getArguments() != null) {
            meal = getArguments().getParcelable("meal_object");
            if (meal != null) {
                updateUI(meal);
                checkIsFav();
            } else {
                showErrorAndNavigateBack();
            }
        } else {
            showErrorAndNavigateBack();
        }
    }

    private void showErrorAndNavigateBack() {
        new MyToast(requireContext(), "Meal details not available");
        requireActivity().onBackPressed();
    }

    private void checkIsFav() {
        if (meal != null && meal.getId() != null) {
            mealDetailsPresenter.isFavorite(meal.getId());
        }
    }

    private void updateUI(Meal meal) {
        if (meal == null) return;

        if (mealName != null) mealName.setText(meal.getName() != null ? meal.getName() : "");
        if (mealCategory != null) mealCategory.setText(meal.getCategory() != null ? meal.getCategory() : "");

        String areaText = meal.getArea() != null ? meal.getArea() : "";
        int ingredientCount = meal.getIngredients() != null ? meal.getIngredients().size() : 0;
        if (mealArea != null) mealArea.setText(areaText + " " + ingredientCount + " ingredients");

        StringBuilder ingredientsBuilder = new StringBuilder();
        if (meal.getIngredients() != null) {
            for (String ingredient : meal.getIngredients()) {
                if (ingredient != null && !ingredient.isEmpty()) {
                    ingredientsBuilder.append("• ").append(ingredient).append("\n");
                }
            }
        }
        if (mealInstructions != null) mealInstructions.setText(meal.getInstructions() != null ? meal.getInstructions() : "");
        if (mealIngredients != null) mealIngredients.setText(ingredientsBuilder.toString());

        if (mealImage != null && meal.getThumbnail() != null && !meal.getThumbnail().isEmpty()) {
            try {
                Glide.with(requireContext())
                        .load(meal.getThumbnail())
                        .error(R.drawable.meal)
                        .into(mealImage);
            } catch (Exception e) {
                Log.e("MealDetails", "Glide loading failed", e);
                mealImage.setImageResource(R.drawable.meal);
            }
        } else if (mealImage != null) {
            mealImage.setImageResource(R.drawable.meal);
        }
    }

    private void updateFavButtonColor() {
        if (getContext() == null || favbtn == null) return;

        if (isFavorite) {
            favbtn.setImageTintList(
                    ContextCompat.getColorStateList(getContext(), R.color.light_orange)
            );
            favbtn.setImageResource(R.drawable.ic_heart);
        } else {
            favbtn.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public void onAddToFav() {
        if (getContext() != null) {
            new MyToast(getContext(), "Added to Favorite");
        }
        isFavorite = true;
        updateFavButtonColor();
    }

    @Override
    public void isFav(boolean isFav) {
        isFavorite = isFav;
        updateFavButtonColor();
    }

    @Override
    public void removeMealFromFav() {
        isFavorite = false;
        updateFavButtonColor();
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }
}