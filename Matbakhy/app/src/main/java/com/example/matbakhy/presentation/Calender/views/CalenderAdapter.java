package com.example.matbakhy.presentation.Calender.views;

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

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.CalenderListHolder> {
    private List<Meal> mealList;
    private CalenderOnClickListener calenderOnClickListener;
    MealClickListener mealClickListener;

    public CalenderAdapter(CalenderOnClickListener calenderOnClickListener, MealClickListener mealClickListener) {
        this.mealList = new ArrayList<>();
        this.calenderOnClickListener = calenderOnClickListener;
        this.mealClickListener = mealClickListener;
    }

    public void setMealList(List<Meal> mealsList) {
        if (mealsList == null) {
            this.mealList = new ArrayList<>();
        } else {
            this.mealList = mealsList;
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalenderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calender_item, parent, false);
        return new CalenderListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderAdapter.CalenderListHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        int count = mealList != null ? mealList.size() : 0;
        return count;
    }

    public class CalenderListHolder extends RecyclerView.ViewHolder {
        private ImageView mealImageView;
        private TextView mealTitleTextView;
        private TextView mealdateTextView;
        private ImageView addToCalButton;

        public CalenderListHolder(@NonNull View itemView) {
            super(itemView);
            mealImageView = itemView.findViewById(R.id.image);
            mealTitleTextView = itemView.findViewById(R.id.name);
            mealdateTextView = itemView.findViewById(R.id.date);
            addToCalButton = itemView.findViewById(R.id.ivCal);
            addToCalButton.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction(() ->
                            addToCalButton.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start()
                    ).start();
        }

        public void bind(Meal meal) {
            mealTitleTextView.setText(meal.getName());
            mealdateTextView.setText(meal.getPlanDate());
            Glide.with(itemView)
                    .load(meal.getThumbnail())
                    .into(mealImageView);
            addToCalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calenderOnClickListener.deleteFav(meal);
                }
            });
            mealImageView.setOnClickListener(v -> mealClickListener.onMealClick(meal));
        }
    }
}

