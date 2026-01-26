package com.example.matbakhy.presentation.Auth.presenter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Patterns;

import com.example.matbakhy.presentation.Meals.view.HomeActivity;

public class AuthUtils {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    public static void navigateToHome(Context context, String email) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("userEmail", email);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }
}