package com.example.matbakhy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPrefManager = SharedPrefManager.getInstance(this);

        String userEmail = "";
        if (getIntent() != null && getIntent().hasExtra("userEmail")) {
            userEmail = getIntent().getStringExtra("userEmail");
        } else {
            userEmail = sharedPrefManager.getUserEmail();
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