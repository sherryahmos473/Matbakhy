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
import com.example.matbakhy.data.model.Area;

import java.util.ArrayList;
import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryListHolder> {
    private List<Area> countryList;
    private CountryListener countryListener;

    public CountryListAdapter(CountryListener countryListener){
        this.countryList = new ArrayList<>();
        this.countryListener = countryListener;
    }

    public void setCountryList(List<Area> countryList){
        this.countryList = countryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryListAdapter.CountryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CountryListAdapter.CountryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.CountryListHolder holder, int position) {
        Area country= countryList.get(position);
        holder.bind(country);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    class CountryListHolder extends RecyclerView.ViewHolder {
        private ImageView countryImage;
        private TextView countryName;
        private CardView countryItem;

        public CountryListHolder(@NonNull View itemView) {
            super(itemView);
            countryImage = itemView.findViewById(R.id.categoryImage);
            countryName = itemView.findViewById(R.id.categoryName);
            countryItem = itemView.findViewById(R.id.categoryItem);
        }

        public void bind(Area country) {
            Log.d("Adapter", "Binding category: " + country.getStrArea());

            if (country.getStrArea() != null && !country.getStrArea().isEmpty()) {
                countryName.setText(country.getStrArea());
            } else {
                countryName.setText("Unknown Category");
            }

            if (country.getFlagUrl() != null && !country.getFlagUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(country.getFlagUrl())
                        .into(countryImage);
            }

            countryItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countryListener != null) {
                        countryListener.getMealOfCountry(country.getStrArea());
                    }
                }
            });
        }
    }
}