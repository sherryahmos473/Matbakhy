package com.example.matbakhy.data.Meals.dataSource;

import com.example.matbakhy.data.Meals.model.Category;

import java.util.List;

public interface CategoriesRemoteResponse {
    void onSuccess(List<Category> categories);
    void onFailure(String errorMessage);
}
