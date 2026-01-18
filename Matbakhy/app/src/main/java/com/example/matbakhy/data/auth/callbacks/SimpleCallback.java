package com.example.matbakhy.data.auth.callbacks;

public interface SimpleCallback {
    void onSuccess(String message);
    void onFailure(String error);
}