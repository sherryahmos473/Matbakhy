package com.example.matbakhy.data.Meals.dataSource;

import com.example.matbakhy.data.Meals.model.Area;
import com.example.matbakhy.data.Meals.model.Category;

import java.util.List;

public interface AreaRemoteResponse {
    void onSuccess(List<Area> countries);
    void onFailure(String errorMessage);
}
