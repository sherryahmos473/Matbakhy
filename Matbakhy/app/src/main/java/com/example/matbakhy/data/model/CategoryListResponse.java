package com.example.matbakhy.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListResponse {
    @SerializedName("categories")
    private List<Category> categories;

    public CategoryListResponse() {
    }

    public CategoryListResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }
}
