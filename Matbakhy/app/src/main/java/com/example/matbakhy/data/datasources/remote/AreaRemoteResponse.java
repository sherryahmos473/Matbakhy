package com.example.matbakhy.data.datasources.remote;

import com.example.matbakhy.data.model.Area;

import java.util.List;

public interface AreaRemoteResponse {
    void onSuccess(List<Area> countries);
    void onFailure(String errorMessage);
}
