package com.example.matbakhy.helper;

import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class ErrorSnackBar {
    public ErrorSnackBar(View view, String messsage){
        Snackbar.make(view, messsage, Snackbar.LENGTH_SHORT).setTextColor(Color.WHITE).setBackgroundTint(Color.RED)
                .show();
    }
}
