package com.example.matbakhy.presentation.Meals.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.matbakhy.R;
import com.example.matbakhy.data.auth.AuthRepository;

public class HomeActivity extends AppCompatActivity {

    private AuthRepository sharedPrefManager;

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

        loadHomeFragment(userEmail);
    }

    private void loadHomeFragment(String userEmail) {
        HomeFragment homeFragment = new HomeFragment();

        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);
        homeFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.commit();
    }
}