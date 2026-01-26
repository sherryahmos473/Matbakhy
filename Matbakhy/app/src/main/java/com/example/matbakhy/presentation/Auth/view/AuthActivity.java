package com.example.matbakhy.presentation.Auth.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.matbakhy.R;

public class AuthActivity extends AppCompatActivity {
    View noInternet;
    Button retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        noInternet = findViewById(R.id.internetErrorOverlay);
        retry = findViewById(R.id.button);
        retry.setOnClickListener(v -> retryConnection());
        retryConnection();
    }
    public boolean isNetworkAvailable( ) {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = cm.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            android.net.NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
    private void showNoInternet() {
        if (noInternet != null) noInternet.setVisibility(View.VISIBLE);
    }

    private void hideNoInternet() {
        if (noInternet != null) noInternet.setVisibility(View.GONE);
    }

    private void retryConnection() {
        if (isNetworkAvailable()) {
            hideNoInternet();
        }else{
            showNoInternet();
        }
    }
}