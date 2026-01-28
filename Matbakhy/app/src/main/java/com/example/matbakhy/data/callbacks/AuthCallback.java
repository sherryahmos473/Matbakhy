package com.example.matbakhy.data.callbacks;


import com.example.matbakhy.data.model.User;

public interface AuthCallback {
    void onSuccess(User user);
    void onFailure(String error);
}