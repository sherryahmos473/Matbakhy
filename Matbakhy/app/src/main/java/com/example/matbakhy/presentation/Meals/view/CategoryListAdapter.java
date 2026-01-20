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
import com.example.matbakhy.data.Meals.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListHolder> {
    private List<Category> categoryList;
    private CategoryListener categoryListener;

    public CategoryListAdapter(CategoryListener categoryListener){
        this.categoryList = new ArrayList<>();
        this.categoryListener = categoryListener;
    }

    public void setCategoryList(List<Category> categoryList){
        Log.d("Adapter", "setCategoryList: " + categoryList.size());
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryListHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView categoryName;
        private CardView categoryItem;

        public CategoryListHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryItem = itemView.findViewById(R.id.categoryItem);
        }

        public void bind(Category category) {
            Log.d("Adapter", "Binding category: " + category.getName());

            // Check if category data is valid
            if (category.getName() != null && !category.getName().isEmpty()) {
                categoryName.setText(category.getName());
            } else {
                categoryName.setText("Unknown Category");
            }

            if (category.getThumbnail() != null && !category.getThumbnail().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(category.getThumbnail())
                        .into(categoryImage);
            }

            categoryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoryListener != null) {
                        categoryListener.getMealOfCategory(category.getName());
                    }
                }
            });
        }
    }
}