package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.matbakhy.R;
import com.example.matbakhy.data.auth.AuthRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private AuthRepository sharedPrefManager;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPrefManager = new AuthRepository(this, com.example.matbakhy.di.AuthModule.provideFirebaseAuth(com.example.matbakhy.di.AuthModule.provideSharedPref(this)), com.example.matbakhy.di.AuthModule.provideSharedPref(this));

        String userEmail = "";
        if (getIntent() != null && getIntent().hasExtra("userEmail")) {
            userEmail = getIntent().getStringExtra("userEmail");
        } else {
            userEmail = sharedPrefManager.getCurrentUserEmail();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Setup BottomNavigationView if you have one
            BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
            if (bottomNav != null) {
                NavigationUI.setupWithNavController(bottomNav, navController);
            }
        }
    }

    public NavController getNavController() {
        return navController;
    }

}