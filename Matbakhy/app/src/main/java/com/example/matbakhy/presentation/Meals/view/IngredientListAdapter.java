package com.example.matbakhy.presentation.Meals.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matbakhy.R;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListHolder> {
    List<String> ingredients;
    IngredientListener ingredientListener;

    public IngredientListAdapter(IngredientListener ingredientListener) {
        this.ingredientListener = ingredientListener;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        Log.d("SearchFragment", "setIngredients: " + ingredients.size());

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

        private CardView ingredientItem;

        public IngredientListHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImage = itemView.findViewById(R.id.categoryImage);
            ingredientName = itemView.findViewById(R.id.categoryName);
            ingredientItem = itemView.findViewById(R.id.categoryItem);
        }

        public void bind(String ingredient) {

            if (ingredient != null) {
                ingredientName.setText(ingredient);
                Glide.with(itemView.getContext())
                        .load("https://www.themealdb.com/images/ingredients/" +
                                ingredient.toLowerCase().replaceAll("\\s+", "_") +
                                "-small.png")
                        .into(ingredientImage);
            } else {
                ingredientName.setText("Unknown Category");
            }
            ingredientItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ingredientListener != null) {
                        ingredientListener.getMealOfIngredient(ingredient);
                    }
                }
            });
        }
    }
}
