package com.example.matbakhy.data.auth.datasources.remote;

public interface SimpleCallback {
    void onSuccess(String message);
    void onFailure(String errorMessage);
}
