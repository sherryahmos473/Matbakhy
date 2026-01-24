package com.example.matbakhy.data.auth.callbacks;

public interface BackupCallback {
    void onSuccess(int backedUpCount, String message);
    void onError(String errorMessage);
}