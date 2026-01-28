package com.example.matbakhy.data.datasources.remote;

import com.example.matbakhy.data.model.Category;

import java.util.List;

public interface CategoriesRemoteResponse {
    void onSuccess(List<Category> categories);
    void onFailure(String errorMessage);
}
