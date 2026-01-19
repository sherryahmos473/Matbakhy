package com.example.matbakhy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Meal;

public class MealDetailsFragment extends Fragment {

    Meal meal;
    private ImageView mealImage;

    private TextView mealName, mealCategory, mealArea, mealInstructions, mealIngredients;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("MealDetailsCrash", "Crash in details fragment");
        if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable("meal_object");
            if (meal != null) {
                updateUI(meal);
            }
        }

    }

    private void updateUI(Meal meal) {
        mealName.setText(meal.getName());
        mealCategory.setText(meal.getCategory());
        mealArea.setText(meal.getArea() + " " +meal.getIngredients().size() + " ingredients");
        StringBuilder ingredientsBuilder = new StringBuilder();
        for (String ingredient : meal.getIngredients()) {
            if (!ingredient.isEmpty()) {
                ingredientsBuilder.append("• ").append(ingredient).append("\n");
            }
        }
        mealInstructions.setText(meal.getInstructions());
        mealIngredients.setText(ingredientsBuilder.toString());

        if (meal.getThumbnail() != null && !meal.getThumbnail().isEmpty()) {
            try {
                Glide.with(requireContext())
                        .load(meal.getThumbnail())
                        .error(R.drawable.meal) // Add error placeholder
                        .into(mealImage);
            } catch (Exception e) {
                Log.e("MealDetails", "Glide loading failed", e);
                mealImage.setImageResource(R.drawable.meal);
            }
        }

    }
}