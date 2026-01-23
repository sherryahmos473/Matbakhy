package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.matbakhy.R;
import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private AuthRepository sharedPrefManager;
    private NavController navController;
    private ActivityHomeBinding binding;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new AuthRepository(this);

        String userEmail = "";
        if (getIntent() != null && getIntent().hasExtra("userEmail")) {
            userEmail = getIntent().getStringExtra("userEmail");
        } else {
            userEmail = sharedPrefManager.getCurrentUserEmail();
        }

        setupNavigation();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            navController.setGraph(R.navigation.home_nav_graph);

            NavigationUI.setupWithNavController(
                    binding.bottomNavigation,
                    navController
            );

            navController.addOnDestinationChangedListener(
                    (controller, destination, arguments) -> {

                        int destId = destination.getId();

                        if ( destId == R.id.mealListFragment) {

                            hideBottomNavigation();

                        } else {
                            showBottomNavigation();
                        }
                    }
            );
        }
    }


    private void hideBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.GONE);
        binding.frame.setLayoutParams(new androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT,
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
        ));
        binding.frame.setPadding(0, 0, 0, 0);
    }

    private void showBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams params =
                (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) binding.frame.getLayoutParams();
        params.height = 0;
        params.bottomToTop = R.id.bottomNavigation;
        binding.frame.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}