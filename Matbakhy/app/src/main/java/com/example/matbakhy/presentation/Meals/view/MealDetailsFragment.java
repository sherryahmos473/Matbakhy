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
    private static final String TAG = "MealDetailsFragment";

    private Meal meal;
    private ImageView mealImage;
    private TextView mealName, mealCategory, mealArea, mealInstructions, mealIngredients;
    private FloatingActionButton favbtn;
    private MealDetailsPresenter mealDetailsPresenter;
    private boolean isFavorite = false;

    public static MealDetailsFragment newInstance(Meal meal) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("meal_object", meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        try {
            View view = inflater.inflate(R.layout.fragment_meal_details, container, false);

            // تهيئة الـ Views
            mealImage = view.findViewById(R.id.mealOfTheDayImage);
            mealName = view.findViewById(R.id.mealofTheDayName);
            mealCategory = view.findViewById(R.id.mealCategory);
            mealArea = view.findViewById(R.id.mealOfTheDayArea);
            mealInstructions = view.findViewById(R.id.instructions);
            mealIngredients = view.findViewById(R.id.ingredients);
            favbtn = view.findViewById(R.id.fabFavorite);

            // التحقق من أن جميع الـ Views موجودة
            if (mealImage == null) Log.e(TAG, "mealImage is null");
            if (mealName == null) Log.e(TAG, "mealName is null");
            if (favbtn == null) Log.e(TAG, "favbtn is null");

            // تهيئة Presenter
            mealDetailsPresenter = new MealDetailsPresenterImpl(getContext(), this);

            // إعداد onClickListener للـ FAB
            favbtn.setOnClickListener(v -> {
                if (meal != null && getContext() != null) {
                    if (!isFavorite) {
                        mealDetailsPresenter.addMealToFav(meal);
                    } else {
                        mealDetailsPresenter.removeMealFromFav(meal);
                    }
                }
            });

            return view;

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            throw e; // Re-throw للاطلاع على الـ Stack Trace
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated - Starting...");

        try {

            Bundle args = getArguments();
            if (args == null) {
                Log.e(TAG, "No arguments found");
                showErrorAndNavigateBack("No meal data received");
                return;
            }

            meal = args.getParcelable("meal_object");

            Log.d(TAG, "Meal loaded successfully: " + meal.getName());

            updateUI(meal);

            checkIsFav();

        } catch (Exception e) {
            Log.e(TAG, "CRASH in onViewCreated: " + e.getMessage(), e);
            e.printStackTrace();

            showErrorAndNavigateBack("Error loading meal: " + e.getMessage());
        }
    }

    private void showErrorAndNavigateBack(String message) {
        try {
            Log.e(TAG, "Error: " + message);

            if (isAdded() && getContext() != null) {
                new MyToast(getContext(), message);

                new android.os.Handler().postDelayed(() -> {
                    try {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Cannot navigate back: " + e.getMessage());
                    }
                }, 1500);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in showErrorAndNavigateBack: " + e.getMessage());
        }
    }

    private void checkIsFav() {
        try {
            if (meal != null && meal.getId() != null && mealDetailsPresenter != null) {
                mealDetailsPresenter.isFavorite(meal.getId());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in checkIsFav: " + e.getMessage());
        }
    }

    private void updateUI(Meal meal) {
        try {
            if (meal == null || getActivity() == null) {
                Log.w(TAG, "Cannot update UI: meal or activity is null");
                return;
            }

            if (mealName != null) {
                mealName.setText(meal.getName() != null ? meal.getName() : "Unknown");
            }

            if (mealCategory != null) {
                mealCategory.setText(meal.getCategory() != null ? meal.getCategory() : "");
            }

            String areaText = meal.getArea() != null ? meal.getArea() : "";
            int ingredientCount = meal.getIngredients() != null ? meal.getIngredients().size() : 0;
            if (mealArea != null) {
                mealArea.setText(areaText + " • " + ingredientCount + " ingredients");
            }

            StringBuilder ingredientsBuilder = new StringBuilder();
            if (meal.getIngredients() != null) {
                for (String ingredient : meal.getIngredients()) {
                    if (ingredient != null && !ingredient.trim().isEmpty()) {
                        ingredientsBuilder.append("• ").append(ingredient.trim()).append("\n");
                    }
                }
            }

            if (mealInstructions != null) {
                mealInstructions.setText(meal.getInstructions() != null ?
                        meal.getInstructions() : "No instructions available");
            }

            if (mealIngredients != null) {
                mealIngredients.setText(ingredientsBuilder.toString());
            }

            if (mealImage != null && meal.getThumbnail() != null && !meal.getThumbnail().isEmpty()) {
                try {
                    Glide.with(requireContext())
                            .load(meal.getThumbnail())
                            .placeholder(R.drawable.meal)
                            .error(R.drawable.meal)
                            .into(mealImage);
                } catch (Exception e) {
                    Log.e(TAG, "Glide loading error: " + e.getMessage());
                    mealImage.setImageResource(R.drawable.meal);
                }
            } else if (mealImage != null) {
                mealImage.setImageResource(R.drawable.meal);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in updateUI: " + e.getMessage(), e);
        }
    }

    private void updateFavButtonColor() {
        try {
            if (!isAdded() || getContext() == null || favbtn == null) {
                return;
            }

            if (isFavorite) {
                favbtn.setImageTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.light_orange)
                );
                favbtn.setImageResource(R.drawable.ic_heart);
            } else {
                favbtn.setImageResource(R.drawable.ic_favorite_border);
                favbtn.setImageTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.gray)
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in updateFavButtonColor: " + e.getMessage());
        }
    }

    @Override
    public void onAddToFav() {
        try {
            if (isAdded() && getContext() != null) {
                new MyToast(getContext(), "Added to Favorite");
            }
            isFavorite = true;
            updateFavButtonColor();
        } catch (Exception e) {
            Log.e(TAG, "Error in onAddToFav: " + e.getMessage());
        }
    }

    @Override
    public void isFav(boolean isFav) {
        try {
            isFavorite = isFav;
            updateFavButtonColor();
        } catch (Exception e) {
            Log.e(TAG, "Error in isFav: " + e.getMessage());
        }
    }

    @Override
    public void removeMealFromFav() {
        try {
            if (isAdded() && getContext() != null) {
                new MyToast(getContext(), "Removed from Favorite");
            }
            isFavorite = false;
            updateFavButtonColor();
        } catch (Exception e) {
            Log.e(TAG, "Error in removeMealFromFav: " + e.getMessage());
        }
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        // استخدم getViewLifecycleOwner() بدلاً من this
        return getViewLifecycleOwner();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");

        // تنظيف الـ References
        mealImage = null;
        mealName = null;
        mealCategory = null;
        mealArea = null;
        mealInstructions = null;
        mealIngredients = null;
        favbtn = null;

        if (mealDetailsPresenter != null) {
            // إذا كان Presenter يحتاج إلى تنظيف
            // mealDetailsPresenter.cleanup();
        }
    }
}