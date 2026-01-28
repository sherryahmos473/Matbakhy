package com.example.matbakhy.presentation.Favorite.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matbakhy.R;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.presentation.MealsList.views.MealClickListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {
    private List<Meal> favMeal;
    private FavoriteOnClickListener favoriteOnClickListener;
    MealClickListener mealClickListener;

    public FavoriteAdapter(FavoriteOnClickListener favoriteOnClickListener, MealClickListener mealClickListener) {
        this.favMeal = new ArrayList<>();
        this.favoriteOnClickListener = favoriteOnClickListener;
        this.mealClickListener = mealClickListener;
        Log.d("FavoriteAdapter", "Adapter created");
    }

    public void setMealList(List<Meal> mealsList) {
        Log.d("FavoriteAdapter", "setMealList called. Previous size: " +
                (favMeal != null ? favMeal.size() : 0) +
                ", New size: " + (mealsList != null ? mealsList.size() : 0));

        if (mealsList == null) {
            this.favMeal = new ArrayList<>();
        } else {
            this.favMeal = mealsList;
        }

        Log.d("FavoriteAdapter", "Calling notifyDataSetChanged");
        notifyDataSetChanged();

        Log.d("FavoriteAdapter", "After notify: getItemCount() = " + getItemCount());
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("FavoriteAdapter", "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteHolder holder, int position) {
        Log.d("FavoriteAdapter", "onBindViewHolder position = " + position +
                ", Item: " + favMeal.get(position).getName());
        Meal meal = favMeal.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        int count = favMeal != null ? favMeal.size() : 0;
        Log.d("FavoriteAdapter", "getItemCount() = " + count);
        return count;
    }

    public class FavoriteHolder extends RecyclerView.ViewHolder {
        private ImageView mealImageView;
        private TextView mealTitleTextView;
        private ImageView addToFavoritesButton;

        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            mealImageView = itemView.findViewById(R.id.image);
            mealTitleTextView = itemView.findViewById(R.id.name);
            addToFavoritesButton = itemView.findViewById(R.id.ivFavorite);
            addToFavoritesButton.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction(() ->
                            addToFavoritesButton.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start()
                    ).start();
        }

        public void bind(Meal meal) {
            mealTitleTextView.setText(meal.getName());
            Glide.with(itemView)
                    .load(meal.getThumbnail())
                    .into(mealImageView);
            addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteOnClickListener.deleteFav(meal);
                }
            });
            mealImageView.setOnClickListener(v -> mealClickListener.onMealClick(meal));
        }
    }
}

