package com.example.matbakhy.data.auth.callbacks;

import com.example.matbakhy.data.auth.model.User;

public interface AuthCallback {
    void onSuccess(User user);
    void onFailure(String error);
}