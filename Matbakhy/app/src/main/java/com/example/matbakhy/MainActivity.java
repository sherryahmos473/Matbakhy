package com.example.matbakhy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.matbakhy.di.AuthModule;
import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.Auth.presenter.MainPresenter;
import com.example.matbakhy.presentation.Auth.presenter.MainPresenterImpl;
import com.example.matbakhy.presentation.Auth.view.AuthActivity;
import com.example.matbakhy.presentation.Auth.view.MainView;
import com.example.matbakhy.presentation.HomeActivity;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    private MainPresenter presenter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize presenter
        presenter = new MainPresenterImpl(AuthModule.provideAuthRepository(this));
        presenter.attachView(this);

        // Start splash delay
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> presenter.checkLoginStatus(), SPLASH_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        presenter.onDestroy();
        presenter.detachView();
    }

    // ==================== MainView Interface Implementation ====================

    @Override
    public void navigateToHome(String email) {
        try {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userEmail", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to home: " + e.getMessage());
            navigateToAuth();
        }
    }

    @Override
    public void navigateToAuth() {
        try {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to auth: " + e.getMessage());
            showError("Navigation error. Please restart app.");
        }
    }

    @Override
    public void showError(String message) {
        new MyToast(this, message);
    }
}