package com.example.matbakhy.presentation.MealDetails.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matbakhy.MainActivity;
import com.example.matbakhy.R;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.model.MealList;
import com.example.matbakhy.helper.ErrorSnackBar;
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.MealDetails.presenter.MealDetailsPresenter;
import com.example.matbakhy.presentation.MealDetails.presenter.MealDetailsPresenterImpl;
import com.example.matbakhy.presentation.Meals.view.IngredientListAdapter;
import com.example.matbakhy.presentation.Meals.view.IngredientListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class MealDetailsFragment extends Fragment implements MealDetailsView , IngredientListener {
    private static final String TAG = "MealDetailsFragment";

    private Meal meal;
    private ImageView mealImage;
    private TextView mealName, mealCategory, mealArea, mealInstructions;
    private FloatingActionButton favbtn, calBtn;
    private MealDetailsPresenter mealDetailsPresenter;
    IngredientListAdapter ingredientListAdapter;
    RecyclerView recyclerView;
    YouTubePlayerView youTubePlayerView;
    ImageView youtubeBtn, btnBack;;
    private YouTubePlayer youTubePlayer;
    private boolean isPlayerReady = false, isGuest, isFavorite = false, isPlanned = false;
    View view;
    FrameLayout youtubeFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_meal_details, container, false);

        mealImage = view.findViewById(R.id.mealOfTheDayImage);
        mealName = view.findViewById(R.id.mealofTheDayName);
        mealCategory = view.findViewById(R.id.mealCategory);
        mealArea = view.findViewById(R.id.mealOfTheDayArea);
        mealInstructions = view.findViewById(R.id.instructions);
        recyclerView = view.findViewById(R.id.ingredientsList);
        calBtn = view.findViewById(R.id.fabCalendar);
        youtubeFrame = view.findViewById(R.id.youtubeFrame);
        ingredientListAdapter = new IngredientListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(ingredientListAdapter);
        recyclerView.setLayoutManager(layoutManager);
        favbtn = view.findViewById(R.id.fabFavorite);
        youtubeBtn = view.findViewById(R.id.videoBtn);
        youTubePlayerView = view.findViewById(R.id.youtubeVideo);
        btnBack = view.findViewById(R.id.btnBack);
        getLifecycle().addObserver(youTubePlayerView);
        mealDetailsPresenter = new MealDetailsPresenterImpl(getContext(), this);

       setUpListeners();
        return view;
    }

    private void setUpListeners() {
        favbtn.setOnClickListener(v -> {
            if (!mealDetailsPresenter.isGuest()) {
                mealDetailsPresenter.favOnClick(meal,isFavorite);
            }else{
                guestDialog();
            }
        });
        calBtn.setOnClickListener(v -> {
            if (!mealDetailsPresenter.isGuest()) {
                mealDetailsPresenter.calenderOnClick(getParentFragmentManager(),meal,isPlanned);
            }else {
                guestDialog();
            }
        });
        youtubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.setVisibility(View.VISIBLE);
                youtubeBtn.setVisibility(View.GONE);
                if (isPlayerReady && youTubePlayer != null) {
                    youTubePlayer.play();
                }
            }
        });
        btnBack.setOnClickListener(v -> mealDetailsPresenter.onBackClicked());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MealDetailsFragmentArgs args = MealDetailsFragmentArgs.fromBundle(getArguments());
        meal = args.getMeal();
        Log.d(TAG, "Meal loaded successfully: " + meal.getName());
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                MealDetailsFragment.this.youTubePlayer = youTubePlayer;
                isPlayerReady = true;
                String videoId = mealDetailsPresenter.extractYouTubeVideoId(meal.getYoutubeUrl());
                if(videoId != null){
                    youTubePlayer.cueVideo(videoId, 0);
                    youTubePlayer.setVolume(100);
                }else{
                    youtubeFrame.setVisibility(View.GONE);
                }
            }
        });
        updateUI(meal);
        checkLocalMeals();
    }
    private void checkLocalMeals() {
        mealDetailsPresenter.isFavorite(meal.getId());
        mealDetailsPresenter.isCal(meal.getId());
    }

    private void updateUI(Meal meal) {
        mealName.setText(meal.getName() != null ? meal.getName() : "Unknown");
        mealCategory.setText(meal.getCategory() != null ? meal.getCategory() : "");
        String areaText = meal.getArea() != null ? meal.getArea() : "";
        int ingredientCount = meal.getIngredients() != null ? meal.getIngredients().size() : 0;
        mealArea.setText(areaText + " • " + ingredientCount + " ingredients");

        ingredientListAdapter.setIngredients(meal.getIngredients());
        mealInstructions.setText(meal.getInstructions() != null ?
                meal.getInstructions() : "No instructions available");
        Glide.with(requireContext())
                .load(meal.getThumbnail())
                .placeholder(R.drawable.meal)
                .error(R.drawable.meal)
                .into(mealImage);
    }

    private void updateFavButtonColor() {
        favbtn.setImageTintList(
                ContextCompat.getColorStateList(requireContext(), isFavorite ? R.color.light_orange: R.color.gray)
        );
        favbtn.setImageResource(isFavorite ? R.drawable.ic_heart : R.drawable.ic_favorite_border);
    }
    private void updateCalButtonColor() {
        calBtn.setImageTintList(
                ContextCompat.getColorStateList(requireContext(),isPlanned ? R.color.light_orange :
                        R.color.gray)
        );
        calBtn.setImageResource(R.drawable.ic_calender);
    }

    @Override
    public void onAddToFav() {
        if (isAdded() && getContext() != null) {
            new MySnackBar(view, "Added to Favorite");
        }
        isFavorite = true;
        updateFavButtonColor();
    }

    @Override
    public void onAddToCal() {
        new MySnackBar(view, "Added to Planned Meals");
        isPlanned = true;
        updateCalButtonColor();
    }

    @Override
    public void isFav(boolean isFav) {
        isFavorite = isFav;
        updateFavButtonColor();
    }

    @Override
    public void isCal(boolean isPlan) {
        isPlanned = isPlan;
        updateCalButtonColor();
    }

    @Override
    public void removeMealFromFav() {
        new MySnackBar(view, "Removed from Favorite");
        isFavorite = false;
        updateFavButtonColor();

    }

    @Override
    public void removeMealFromCal() {
        new MySnackBar(view, "Removed from Planned Meal");
        isPlanned = false;
        updateFavButtonColor();
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    public void onSuccess(List<Meal> meals) {
        MealList mealsList = new MealList(meals);
        MealDetailsFragmentDirections.ActionMealDetailsFragmentToMealListFragment action = MealDetailsFragmentDirections.actionMealDetailsFragmentToMealListFragment(mealsList);
        Navigation.findNavController(view).navigate(action);
    }

    @Override
    public void onFailure(String errorMessage) {
        new ErrorSnackBar(view, errorMessage);
    }

    @Override
    public void guestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_guest_mode, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Button btnLogin = dialogView.findViewById(R.id.btnLogin);
        TextView btnContinue = dialogView.findViewById(R.id.btnContinue);

        btnLogin.setOnClickListener(v -> {
            dialog.dismiss();
            mealDetailsPresenter.login();
        });

        btnContinue.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Override
    public void navToLogin() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mealImage = null;
        mealName = null;
        mealCategory = null;
        mealArea = null;
        mealInstructions = null;
        favbtn = null;
    }
    @Override
    public void navigateBack() {
        if (getView() != null) {
            Navigation.findNavController(getView()).popBackStack();
        }
    }
    @Override
    public void getMealOfIngredient(String ingredient) {
        mealDetailsPresenter.getMealOfIngredient(ingredient);
    }
}