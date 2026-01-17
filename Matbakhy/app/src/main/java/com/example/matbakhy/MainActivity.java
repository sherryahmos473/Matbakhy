package com.example.matbakhy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseServices firebaseServices;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        firebaseServices = FirebaseServices.getInstance(this);
        new Handler(Looper.getMainLooper()).postDelayed(this::NavigateBasedOnLoginStatus, 2000);
    }

    private void NavigateBasedOnLoginStatus() {
        Intent intent;

        if (firebaseServices.isUserLoggedIn()) {
            String userEmail = firebaseServices.getCurrentUserEmail();
            if (userEmail.isEmpty()) {
                userEmail = firebaseServices.getCurrentUserEmail() != null ?
                        firebaseServices.getCurrentUserEmail(): "User";
            }
            intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userEmail", userEmail);
        } else {
            Log.d(TAG, "User is not logged in, navigating to AuthActivity");
            intent = new Intent(MainActivity.this, AuthActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}