package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.matbakhy.R;
import com.example.matbakhy.data.AuthRepository;
import com.example.matbakhy.databinding.ActivityHomeBinding;

import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<String> userEmail = new AtomicReference<>("");
        if (getIntent() != null && getIntent().hasExtra("userEmail")) {
            userEmail.set(getIntent().getStringExtra("userEmail"));
        } else {
             sharedPrefManager.getCurrentUserEmail().subscribe(
                     email -> userEmail.set(email)
             );
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

                        if (destId == R.id.mealListFragment  || destId == R.id.mealDetailsFragment || destId == R.id.loginFragment || destId == R.id.registerFragment || destId == R.id.forgotPasswordFragment) {
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
    }

    private void showBottomNavigation() {
        binding.bottomNavigation.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}