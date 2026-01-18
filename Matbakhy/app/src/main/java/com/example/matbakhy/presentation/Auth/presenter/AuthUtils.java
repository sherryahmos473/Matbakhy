package com.example.matbakhy.presentation.Auth.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;

import com.example.matbakhy.helper.MyToast;
import com.example.matbakhy.presentation.HomeActivity;

public class AuthUtils {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static void showToast(Context context, String message) {
        if (context != null) {
            new MyToast(context, message);
        }
    }

    public static void navigateToHome(Context context, String email) {
        try {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra("userEmail", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                context.startActivity(intent);
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).finish();
                }
            }, 300);

        } catch (Exception e) {
            showToast(context, "Navigation error. Please restart app.");
        }
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }
}