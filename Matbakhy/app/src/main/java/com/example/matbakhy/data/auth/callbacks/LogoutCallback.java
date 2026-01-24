package com.example.matbakhy.data.auth.callbacks;

public interface LogoutCallback {
    void onLogoutComplete(boolean backupSuccess, String message);
}