package com.example.matbakhy.presentation.MealsList.views;


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
import com.example.matbakhy.data.Meals.model.Meal;
import java.util.ArrayList;
import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {
    private List<Meal> mealList;
    private MealClickListener listener;

    public MealListAdapter(MealClickListener listener) {
        this.mealList = new ArrayList<>();
        this.listener = listener;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        private ImageView mealImage;
        private TextView mealName;
        private CardView mealCard;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.iv_poster);
            mealName = itemView.findViewById(R.id.tv_name);
            mealCard = itemView.findViewById(R.id.mealCard);
        }

        public void bind(Meal meal) {
            Log.d("TAG", "bind: " + meal);
            mealName.setText(meal.getName());
            Glide.with(itemView.getContext())
                    .load(meal.getThumbnail())
                    .into(mealImage);
            mealCard.setOnClickListener(v -> listener.onMealClick(meal));
        }
    }

    public interface MealClickListener {
        void onMealClick(Meal meal);
    }
}