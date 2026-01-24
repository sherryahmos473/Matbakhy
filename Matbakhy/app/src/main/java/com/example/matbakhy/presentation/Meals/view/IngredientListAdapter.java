package com.example.matbakhy.presentation.Meals.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matbakhy.R;
import com.example.matbakhy.data.Meals.model.Category;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListHolder> {
    List<String> ingredients;

    public IngredientListAdapter() {
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new IngredientListAdapter.IngredientListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListHolder holder, int position) {
        String ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientListHolder extends RecyclerView.ViewHolder {
        private ImageView ingredientImage;
        private TextView ingredientName;

        public IngredientListHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.categoryImage);
            ingredientName = itemView.findViewById(R.id.categoryName);
        }

        public void bind(String ingredient) {

            if (ingredient != null) {
                ingredientName.setText(ingredient);
            } else {
                ingredientName.setText("Unknown Category");
            }
            Glide.with(itemView.getContext())
                    .load("https://www.themealdb.com/images/ingredients/" +
                            ingredient.toLowerCase().replaceAll("\\s+", "_") +
                            "-small.png")
                    .into(ingredientImage);
        }
    }
}
